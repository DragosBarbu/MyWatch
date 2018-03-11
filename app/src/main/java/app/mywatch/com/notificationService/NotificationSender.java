package app.mywatch.com.notificationService;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dragos on 1/7/18.
 */

public interface NotificationSender {
    public void send(String message, Context context);
}
