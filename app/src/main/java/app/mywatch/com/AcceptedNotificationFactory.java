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
        notification.setValid(shouldUseNotification(packageName));
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

    private static boolean shouldUseNotification(String packageName) {
        return packageName.equals(ApplicationPackageNames.FACEBOOK_PACK_NAME)
                || packageName.equals(ApplicationPackageNames.FACEBOOK_MESSENGER_PACK_NAME)
                || packageName.equals(ApplicationPackageNames.WHATSAPP_PACK_NAME)
                || packageName.equals(ApplicationPackageNames.INSTAGRAM_PACK_NAME);
    }


    private static final class ApplicationPackageNames {
        public static final String FACEBOOK_PACK_NAME = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER_PACK_NAME = "com.facebook.orca";
        public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        public static final String INSTAGRAM_PACK_NAME = "com.instagram.android";
    }
}
