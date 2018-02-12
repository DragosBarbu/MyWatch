package app.mywatch.com;

/**
 * Created by dragos on 1/8/18.
 */

public class NotificationSenderFactory {

    public enum NotificationType {
        CALENDAR,
        SMS
    }

    public static NotificationSender createInstance(NotificationType type) {
        NotificationSender sender;
        if (type == NotificationType.CALENDAR)
            sender = new CalendarEvent();
        else
            throw new NullPointerException("SMS not yet supported");

        return sender;
    }
}
