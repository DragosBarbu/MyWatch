package app.mywatch.com.repos;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import app.mywatch.com.dao.AppDatabase;
import app.mywatch.com.models.AppModel;
import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by dragos on 2/19/18.
 */

public class AppRepository {
    private static AppRepository instance;

    private enum EVENT {
        AppModelAdded,
        AppModelRemoved
    }

    private List<AppModel> apps;

    public static AppRepository getInstance() {
        if (instance == null)
            instance = new AppRepository();
        return instance;
    }

    private AppRepository() {
        apps = new ArrayList<>();
    }

    public List<AppModel> getAllApps() {
        return apps;
    }

    public Task<List<AppModel>> getAllAppsAsync(final Context context) {
        return Task.callInBackground(new Callable<List<AppModel>>() {
            public List<AppModel> call() {
                apps = AppDatabase.getInstance(context).getAppModelDao().getAll();
                return apps;
            }
        });
    }

    public Task<Void> addAppAsync(final Context context, String appName, String packageName) {
        final AppModel appModel = new AppModel(appName, packageName);
        apps.add(appModel);

        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase.getInstance(context).getAppModelDao().insertAll(appModel);
                return null;
            }
        });
    }

    public Task<Void> removeAppAsync(final Context context, final String packageName) {
        AppModel appModel = null;
        for (AppModel a : apps) {
            if (a.getPackageName().equals(packageName)) {
                appModel = a;
                break;
            }
        }
        if (appModel == null)
            throw new NullPointerException("App model not found");

        final AppModel finalAppModel = appModel;
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase.getInstance(context).getAppModelDao().delete(finalAppModel);
                return null;
            }
        });
    }

    public Task<Void> addDefaultApps(final Context context) {
        //FB
        apps.add(new AppModel("Facebook", ApplicationPackageNames.FACEBOOK));
        //FB messenger
        AppModel fbMess = new AppModel("Facebook Messenger", ApplicationPackageNames.FACEBOOK_MESSENGER);
        ArrayList<String> fbMessIgnore = new ArrayList<>();
        fbMessIgnore.add("Messenger: chat heads active");
        fbMessIgnore.add("chat heads active");
        fbMess.setIgnoreList(fbMessIgnore);
        fbMess.setAllowNotifications(true);
        apps.add(fbMess);
        //Whatsapp
        apps.add(new AppModel("Whatsapp", ApplicationPackageNames.WHATSAPP));
        //Instagram
        apps.add(new AppModel("Instagram", ApplicationPackageNames.INSTAGRAM));

        final AppModel[] appsArray = apps.toArray(new AppModel[0]);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase.getInstance(context).getAppModelDao().insertAll(appsArray);
                apps.addAll(AppDatabase.getInstance(context).getAppModelDao().getAll());
                return null;
            }
        });
    }

    public Task<Void> updateAppModelAsync(final Context context, final AppModel appModel) {

        for (AppModel a : apps) {
            if (a.getPackageName().equals(appModel.getPackageName())) {
                a.setAllowNotifications(appModel.getAllowNotifications());
                a.setIgnoreList(appModel.getIgnoreList());
            }
        }

        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDatabase.getInstance(context).getAppModelDao().update(appModel);
                return null;
            }
        });
    }

    private static final class ApplicationPackageNames {
        public static final String FACEBOOK = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER = "com.facebook.orca";
        public static final String WHATSAPP = "com.whatsapp";
        public static final String INSTAGRAM = "com.instagram.android";
    }
}
