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

    public boolean shouldUseNotification(String packageName) {
        return apps.contains(new AppModel("", packageName));
    }


    private void addDefaultApps() {
        apps.add(new AppModel("Facebook", ApplicationPackageNames.FACEBOOK));
        apps.add(new AppModel("Facebook Messenger", ApplicationPackageNames.FACEBOOK_MESSENGER));
        apps.add(new AppModel("Whatsapp", ApplicationPackageNames.WHATSAPP));
        apps.add(new AppModel("Instagram", ApplicationPackageNames.INSTAGRAM));
    }

    private static final class ApplicationPackageNames {
        public static final String FACEBOOK = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER = "com.facebook.orca";
        public static final String WHATSAPP = "com.whatsapp";
        public static final String INSTAGRAM = "com.instagram.android";
    }
}