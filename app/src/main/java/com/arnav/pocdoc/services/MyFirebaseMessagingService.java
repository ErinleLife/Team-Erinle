package com.arnav.pocdoc.services;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.LogUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String conversationId;
    public static boolean isChatScreenOpen;

    public static void setOnChatMessageListener(boolean b, String cId) {
        isChatScreenOpen = b;
        conversationId = cId;
    }

    @Override
    public void onNewToken(String s) {
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
//        String title = "", message = "", badge = "";
//        DataNotification dataNotification = new DataNotification();
//        boolean needToClearScreens = true;
//
//        if (remoteMessage.getNotification() != null) {
//            if (remoteMessage.getNotification().getTitle() != null)
//                title = remoteMessage.getNotification().getTitle();
//            if (remoteMessage.getNotification().getBody() != null)
//                message = remoteMessage.getNotification().getBody();
//        }
//        String pushType;
//        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        if (remoteMessage.getData().size() > 0) {
//            if (remoteMessage.getData().get(Constants.push_type) != null) {
//                pushType = remoteMessage.getData().get(Constants.push_type);
//                dataNotification.setPush_type(pushType);
//                if (pushType != null && pushType.equals(Constants.push_type_message)) {
//                    String chatId = remoteMessage.getData().get(Constants.chat_id);
//                    if (isChatScreenOpen && chatId != null && chatId.equals(conversationId)) {
//                        //Chat is running, No need to send push notification
//                        return;
//                    }
//                }
//
//                dataNotification.setIs_group_chat(remoteMessage.getData().containsKey(Constants.is_group_chat) ?
//                        remoteMessage.getData().get(Constants.is_group_chat) : "");
//                dataNotification.setUser_pic(remoteMessage.getData().containsKey(Constants.user_pic) ?
//                        remoteMessage.getData().get(Constants.user_pic) : "");
//                dataNotification.setChat_id(remoteMessage.getData().containsKey(Constants.chat_id) ?
//                        remoteMessage.getData().get(Constants.chat_id) : "");
//                dataNotification.setUser_id(remoteMessage.getData().containsKey(Constants.user_id) ?
//                        remoteMessage.getData().get(Constants.user_id) : "");
//                dataNotification.setUser_name(remoteMessage.getData().containsKey(Constants.user_name) ?
//                        remoteMessage.getData().get(Constants.user_name) : "");
//                dataNotification.setPush_type(pushType);
//
//                badge = remoteMessage.getData().containsKey(Constants.badge) ?
//                        String.valueOf(remoteMessage.getData().get(Constants.badge)) : "";
//
//
//                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED) &&
//                        sharedPrefData != null && sharedPrefData.isUserLoggedIn()) {
//                    switch (Objects.requireNonNull(pushType)) {
//                        case Constants.push_type_message: {
//                            resultIntent = new Intent(this, MessagesActivity.class);
//                            resultIntent.putExtra(Constants.chat_id, dataNotification.getChat_id());
//                            resultIntent.putExtra(Constants.user_name, dataNotification.getUser_name());
//                            resultIntent.putExtra(Constants.user_pic, dataNotification.getUser_pic());
//                            resultIntent.putExtra(Constants.groupChatOrNOt, dataNotification.getIs_group_chat().equals("1"));
//                            resultIntent.putExtra(Constants.is_read, true);
//                            resultIntent.putExtra(Constants.fcmIds, Constants.fcmIdsArray);
//                            needToClearScreens = false;
//                        }
//                        break;
//                        case Constants.push_type_follow_request: {
//                            resultIntent = new Intent(this, FriendsRequestActivity.class);
//                            resultIntent.putExtra(Constants.user_id, sharedPrefData.getUID());
//                            needToClearScreens = false;
//                        }
//                        break;
//                        case Constants.push_type_follow_request_accept:
//                        case Constants.push_type_follow: {
//                            if (!Utils.isEmpty(dataNotification.getUser_id())) {
//                                resultIntent = new Intent(this, FollowingFollowersActivity.class);
//                                resultIntent.putExtra(Constants.user_id, sharedPrefData.getUID());
//                                needToClearScreens = false;
//                            }
//                        }
//                        break;
//                        case Constants.push_type_order_confirm:
//                        case Constants.push_type_order_received: {
//                            resultIntent = new Intent(this, PastPurchasesActivity.class);
//                            resultIntent.putExtra(Constants.from, sharedPrefData.isBusinessUser() ?
//                                    Constants.isBusiness : Constants.isUser);
//                            needToClearScreens = false;
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//        resultIntent.putExtra(Constants.from, Constants.fromPush);
//        resultIntent.putExtra(Constants.DATA, dataNotification);
//        showNotificationMessage(getApplicationContext(),
//                (title != null && !title.equals("")) ? title : getString(R.string.app_name),
//                (message != null && !message.equals("")) ? message : getString(R.string.app_name),
//                resultIntent, badge, needToClearScreens);
    }

    /**
     * SHOWING NOTIFICATION WITH TEXT ONLY
     */
    private void showNotificationMessage(Context context, String title, String message,
                                         Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }
}