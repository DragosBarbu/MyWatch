package com.japoapps.hwplus.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.japoapps.hwplus.models.AppModel;

/**
 * Created by dragos on 3/6/18.
 */
@Database(entities = {AppModel.class}, version = 1)
@TypeConverters({StringListConvertor.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppModelDao getAppModelDao();

    private static final String NAME = "mywatch.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        Context appContext = context.getApplicationContext();
        if (instance == null) {
            instance = create(appContext);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, NAME).build();
    }
}
