package app.mywatch.com;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dragos on 2/19/18.
 */

public class AppModel implements Parcelable {

    private String name;
    private String packageName;

    public AppModel(String appName, String packageName) {
        name = appName;
        this.packageName = packageName;
    }

    protected AppModel(Parcel in) {
        name = in.readString();
        packageName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AppModel> CREATOR = new Parcelable.Creator<AppModel>() {
        @Override
        public AppModel createFromParcel(Parcel in) {
            return new AppModel(in);
        }

        @Override
        public AppModel[] newArray(int size) {
            return new AppModel[size];
        }
    };
}
