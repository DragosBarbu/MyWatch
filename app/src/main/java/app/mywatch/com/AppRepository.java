package app.mywatch.com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragos on 2/19/18.
 */

public class AppRepository {
    private static AppRepository instance;

    public static AppRepository getInstance() {
        if (instance == null)
            instance = new AppRepository();
        return instance;
    }

    private List<AppModel> apps;

    private AppRepository() {
        apps = new ArrayList<>();
        addDefaultApps();
    }


    public List<AppModel> getAddedApps() {
        return apps;
    }

    public void addApp(String appName, String packageName) {
        apps.add(new AppModel(appName, packageName));
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

    private static final class ApplicationPackageNames {
        public static final String FACEBOOK = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER = "com.facebook.orca";
        public static final String WHATSAPP = "com.whatsapp";
        public static final String INSTAGRAM = "com.instagram.android";
    }
}
