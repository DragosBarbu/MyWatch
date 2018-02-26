package app.mywatch.com;

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

    public boolean isValid(String packageName, String message) {

        AppModel appModel = null;
        boolean isValid = false;

        for (AppModel app : AppRepository.getInstance().getAddedApps())
            if (app.getPackageName().equals(packageName))
                appModel = app;

        if (message == null)
            return false;
        message = message.toLowerCase().trim();

        if (appModel != null) {
            if (appModel.getIgnoreList() != null)
                for (String str : appModel.getIgnoreList()) {
                    str = str.toLowerCase().trim();
                    int shortest = str.length() < message.length() ? str.length() : message.length();
                    if (str.substring(0, shortest).equals(message.substring(0, shortest))) {
                        isValid = false;
                        break;
                    } else
                        isValid = true;
                }
            else isValid = true;
        } else
            isValid = false;
        return isValid;
    }
}
