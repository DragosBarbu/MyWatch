package com.japoapps.hwplus.notificationService;

import android.content.Context;

/**
 * Created by dragos on 1/7/18.
 */

public interface NotificationSender {
    public void send(String message, Context context);
}
