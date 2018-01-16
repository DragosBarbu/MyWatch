package app.mywatch.com;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import java.io.Console;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by dragos on 1/7/18.
 */

public class CalendarEvent implements NotificationSender {

    private static long CALENDAR_ID = 420;
    String accountName;
    String accountType;

    public CalendarEvent() {
        accountName = "com.mywatch";
        accountType = CalendarContract.ACCOUNT_TYPE_LOCAL;
    }


    @Override
    public void send(String message, Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {

            long calID = getCalendarId(context);
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
//            beginTime.set(2018, 0, 16, 19, 55);
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.add(Calendar.MINUTE, 1);
//            endTime.set(2018, 0, 16, 19, 56);
            endMillis = endTime.getTimeInMillis();


            final ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            values.put(CalendarContract.Events.TITLE, message);
            values.put(CalendarContract.Events.DESCRIPTION, message);
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.HAS_ALARM, 1);


            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());

            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 1);

            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("MissingPermission")
                @Override
                public void run() {
                    cr.delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events.CALENDAR_ID + " = ?", new String[]{String.valueOf(CALENDAR_ID)});

                }
            }, 10000);

            System.out.println("AAAAA: " + eventID);
        }
    }

    @SuppressLint("MissingPermission")
    private long getCalendarId(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String appName = context.getResources().getString(R.string.app_name);


        final Cursor cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME}), null, null, null);

        while (cursor.moveToNext()) {
            final String id = cursor.getString(0);
            final String displayName = cursor.getString(1);

            if (displayName.equals(appName)) {
//                clearCalendar(contentResolver,displayName);
                if (id.equals(String.format("%d", CALENDAR_ID)) && displayName.equals(appName))
                    return Integer.parseInt(id);
            }
        }
        //Create a calendar if it does not exist

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType);
        values.put(CalendarContract.Calendars.NAME, appName);
        values.put(CalendarContract.Calendars._ID, CALENDAR_ID);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, appName);
        values.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT");
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        Uri calUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                .build();
        Uri newCalendar = context.getContentResolver().insert(calUri, values);

        return -1;
    }

    void clearCalendar(ContentResolver contentResolver, String displayName) {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                .build();
        contentResolver.delete(calUri, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?", new String[]{String.valueOf(displayName)});

    }
}