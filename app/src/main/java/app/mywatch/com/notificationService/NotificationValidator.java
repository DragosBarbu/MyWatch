package app.mywatch.com.notificationService;

import android.content.Context;

import app.mywatch.com.models.AppModel;
import app.mywatch.com.repos.AppRepository;

/**
 * Created by dragos on 2/26/18.
 */

public class NotificationValidator {

    private static NotificationValidator instance;

    public static NotificationValidator getInstance() {
        if (instance == null)
            instance = new NotificationValidator();
        return instance;
    }

    private NotificationValidator() {
    }

    public boolean isValid(Context context, String packageName, String message) {

        AppModel appModel = null;
        boolean isValid = false;

        for (AppModel app : AppRepository.getInstance().getAllApps())
            if (app.getPackageName().equals(packageName))
                appModel = app;

        if (message == null)
            return false;
        message = message.toLowerCase().trim();

        if (appModel != null) {
            // if app does not allow notifications return false
            if (!appModel.getAllowNotifications())
                return false;

            //if the ignore list contains a string that matches the notification is not valid
            if (appModel.getIgnoreList() != null)
                for (String str : appModel.getIgnoreList()) {
                    str = str.toLowerCase().trim();
                    int shortest = str.length() < message.length() ? str.length() : message.length();
                    if (str.substring(0, shortest).equals(message.substring(0, shortest))) {
                        isValid = false;
                        break;
                    } else isValid = true;
                }
            else isValid = true;
        } else isValid = false;
        return isValid;
    }
}
