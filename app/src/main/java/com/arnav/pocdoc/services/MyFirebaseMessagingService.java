package com.arnav.pocdoc.services;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SplashActivity;
import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.consultant.chat.ChatActivity;
import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.data.model.conversation.DataConversation;
import com.arnav.pocdoc.implementor.PushUpdateListener;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.LogUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String conversationId = "";
    public static boolean isChatScreenOpen;

    private static PushUpdateListener listener;

    public static void setOnChatMessageListener(boolean b, String cId, PushUpdateListener updateListener) {
        isChatScreenOpen = b;
        conversationId = cId; // chat screen on pause mode.
        listener = updateListener;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        BaseApplication.preferences.putString(Constants.fcm_registration_id, s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LogUtils.Print(TAG, "onMessageReceived");
        LogUtils.Print(TAG, "getData: " + remoteMessage.getData());
        LogUtils.Print(TAG, "getNotification: " + remoteMessage.getNotification());
        setPushNotification(remoteMessage);
    }

    private void setPushNotification(RemoteMessage remoteMessage) {
        String title = "", message = "";
        DataChat dataNotification = new DataChat();
        DataConversation dataConversation = new DataConversation();
        boolean needToClearScreens = true;

        remoteMessage.getData();
        if (remoteMessage.getData().containsKey(Constants.title))
            title = remoteMessage.getData().get(Constants.title);
        if (remoteMessage.getData().containsKey(Constants.body))
            message = remoteMessage.getData().get(Constants.body);
        String pushType;
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get(Constants.push_type) != null) {
                pushType = remoteMessage.getData().get(Constants.push_type);
                if (pushType != null && pushType.equals(Constants.push_type_pharmacy)) {
                    if (remoteMessage.getData().containsKey(Constants.data)) {
                        dataNotification = new GsonBuilder().create().fromJson(remoteMessage.getData().get(Constants.data), DataChat.class);
                        if (dataNotification != null) {
                            dataConversation.setId(dataNotification.getFrom_id());
                            String convId = (dataNotification.getFrom_id() > dataNotification.getTo_id()) ?
                                    dataNotification.getTo_id() + "_" + dataNotification.getFrom_id()
                                    : dataNotification.getFrom_id() + "_" + dataNotification.getTo_id();
                            if (conversationId.equals("pause") || (isChatScreenOpen && convId.equals(conversationId))) {
                                //Chat is running, No need to send push notification
                                if (listener != null) {
                                    listener.onPushReceived(dataNotification);
                                }
                                if (!conversationId.equals("pause"))
                                    return;
                            }

                            //open login screen directly.
                            if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) &&
                                    BaseApplication.preferences.isUserLoggedIn()) {
                                Objects.requireNonNull(pushType);
                                resultIntent = new Intent(this, ChatActivity.class);
                                resultIntent.putExtra(Constants.data, dataConversation);
                                needToClearScreens = false;
                            }
                        }
                    }
                }
            }
        }
        resultIntent.putExtra(Constants.from, Constants.fromPush);
        resultIntent.putExtra(Constants.data, dataConversation);
        showNotificationMessage(getApplicationContext(),
                (title != null && !title.equals("")) ? title : getString(R.string.app_name),
                (message != null && !message.equals("")) ? message : getString(R.string.app_name),
                resultIntent, needToClearScreens);
    }

    /**
     * SHOWING NOTIFICATION WITH TEXT ONLY
     */
    private void showNotificationMessage(Context context, String title, String message,
                                         Intent intent, boolean needToClearScreens) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        if (needToClearScreens)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, needToClearScreens);
    }
}