package com.japoapps.hwplus.notificationService;

/**
 * Created by dragos on 2/12/18.
 */

public class AcceptedNotification {
    private String appName;
    private String packageName;
    private String title;
    private String text;
    private boolean isValid;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayedText() {

        if (appName == null)
            appName = "";
        if (title == null)
            title = "";
        if (text == null)
            text = "";

        return String.format("%s: %s- %s", appName, title, text);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
