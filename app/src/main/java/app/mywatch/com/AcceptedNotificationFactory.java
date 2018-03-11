package app.mywatch.com;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by dragos on 2/12/18.
 */

public class AcceptedNotificationFactory {

    public static AcceptedNotification getInstance(Context context, String packageName, String title, String text) {
        AcceptedNotification notification = new AcceptedNotification();
        notification.setAppName(getAppName(context, packageName));
        notification.setPackageName(packageName);
        notification.setText(text);
        notification.setTitle(title);
        notification.setValid(NotificationValidator.getInstance().isValid(context,packageName, notification.getDisplayedText()));
        return notification;
    }

    private static String getAppName(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "");
    }
}
