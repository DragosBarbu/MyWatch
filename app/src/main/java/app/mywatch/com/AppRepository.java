package app.mywatch.com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragos on 2/19/18.
 */

public class AppRepository {
    private static AppRepository instance;

    private enum EVENT {
        AppModelAdded,
        AppModelRemoved
    }

    private List<AppDataChangedListener> listeners;
    private List<AppModel> apps;

    public static AppRepository getInstance() {
        if (instance == null)
            instance = new AppRepository();
        return instance;
    }


    private AppRepository() {
        apps = new ArrayList<>();
        addDefaultApps();
        listeners = new ArrayList<>();
    }

    public void addListener(AppDataChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AppDataChangedListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    public List<AppModel> getAddedApps() {
        //todo load once then return
        return apps;
    }

    public void addApp(String appName, String packageName) {
        AppModel appModel = new AppModel(appName, packageName);
        apps.add(appModel);
        notifyListeners(EVENT.AppModelAdded, appModel);
    }

    private void notifyListeners(EVENT ev, AppModel appModel) {
        for (AppDataChangedListener l : listeners) {
            switch (ev) {
                case AppModelAdded:
                    l.onAppAdded(appModel);
                    break;
                case AppModelRemoved:
//                    l.onAppRemoved(appModel);
                    break;
            }
        }
    }

    private void addDefaultApps() {
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
    }

    public void updateAppModel(AppModel appModel) {

    }

    private static final class ApplicationPackageNames {
        public static final String FACEBOOK = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER = "com.facebook.orca";
        public static final String WHATSAPP = "com.whatsapp";
        public static final String INSTAGRAM = "com.instagram.android";
    }

    public interface AppDataChangedListener {

        void onAppAdded(AppModel appModel);
    }
}
