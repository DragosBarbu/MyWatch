package com.japoapps.hwplus.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.japoapps.hwplus.models.AppModel;

/**
 * Created by dragos on 3/6/18.
 */
@Dao
public interface AppModelDao {
    @Query("SELECT * FROM AppModel")
    List<AppModel> getAll();

    @Query("SELECT * FROM AppModel WHERE package_name == (:pkgName)")
    AppModel getByPackageName(String pkgName);

    @Insert
    void insertAll(AppModel... apps);

    @Delete
    void delete(AppModel app);

    @Update
    void update(AppModel appModel);
}
