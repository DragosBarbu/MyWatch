package app.mywatch.com;

/**
 * Created by dragos on 2/19/18.
 */

public class AppModel {

    private String name;
    private String packageName;

    public AppModel(String appName, String packageName) {
        name = appName;
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppModel) {
            AppModel other = (AppModel) obj;
            return this.getPackageName().equals(other.getPackageName());
        } else
            return false;
    }
}
