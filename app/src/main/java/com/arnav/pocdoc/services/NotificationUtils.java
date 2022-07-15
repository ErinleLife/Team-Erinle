package com.arnav.pocdoc.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationUtils {

    private final Context mContext;
    final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * METHOD CHECKS IF THE APP IS IN BACKGROUND OR NOT
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }
        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void showNotificationMessage(final String title, final String message, Intent intent, boolean needToClearScreens) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        // notification icon
        final int icon = R.mipmap.ic_launcher;
        if (needToClearScreens)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        createID(),
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

        String strNotificationId = createNID();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, strNotificationId);

        showSmallNotification(mBuilder, title, message, resultPendingIntent, strNotificationId);
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, String title,
                                       String message, PendingIntent resultPendingIntent,
                                       String strNotificationId) {
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    strNotificationId, title, NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription(message);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
//            notificationChannel.setShowBadge(false);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(notificationChannel);
        }
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(getNotificationSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
//                .setNumber(4)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setVibrate(new long[]{1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker(message)
                .setChannelId(strNotificationId)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_MAX);
        } else {
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
//            playNotificationSound();
        }

        if (notificationManager != null)
            notificationManager.notify(createID(), mBuilder.build());
    }

    private int createID() {
        Date now = new Date();
        return Integer.parseInt(new SimpleDateFormat(Constants.DATE_PUSH_NOTIFICATION_FORMAT, Locale.US).format(now));
    }

    private String createNID() {
        Date cDate = new Date();
        return new SimpleDateFormat(Constants.DATE_PUSH_NOTIFICATION_FORMAT, Locale.ENGLISH).format(cDate);
    }

    private int getNotificationSmallIcon() {
        return R.mipmap.ic_launcher;
    }

    // Playing notification sound
    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}