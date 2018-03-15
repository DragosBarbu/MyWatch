package com.japoapps.hwplus.notificationService;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;


/**
 * Created by dragos on 1/21/18.
 */

public class AndroidNotificationListener extends NotificationListenerService {

    private String previousNotificationKey;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (TextUtils.isEmpty(previousNotificationKey) || !TextUtils.isEmpty(previousNotificationKey) && !sbn.getKey().equals(previousNotificationKey)) {
            previousNotificationKey = sbn.getKey();
            Bundle extras = sbn.getNotification().extras;
            String title = "";
            String text = "";
            if (extras.containsKey("android.title"))
                title = extras.getString("android.title");
            if (extras.containsKey("android.text"))
                if (extras.getCharSequence("android.text") != null)
                    text = extras.getCharSequence("android.text").toString();

            AcceptedNotification notification = AcceptedNotificationFactory.getInstance(getApplicationContext(), sbn.getPackageName(), title, text);
            if (notification.isValid())
                sendNotification(notification);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    private void sendNotification(AcceptedNotification notification) {
        NotificationSenderFactory.createInstance(NotificationSenderFactory.NotificationType.CALENDAR)
                .send(notification.getDisplayedText(), getApplicationContext());
    }
}
