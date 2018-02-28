package app.mywatch.com;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dragos on 2/19/18.
 */

public class AppModel implements Parcelable {

    private String name;
    private String packageName;
    private ArrayList<String> ignoreList;
    private boolean allowNotifications;

    public AppModel(String appName, String packageName) {
        name = appName;
        this.packageName = packageName;
    }

    protected AppModel(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        if (in.readByte() == 0x01) {
            ignoreList = new ArrayList<String>();
            in.readList(ignoreList, String.class.getClassLoader());
        } else {
            ignoreList = null;
        }
        allowNotifications = in.readByte() != 0x00;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIgnoreList() {
        return ignoreList;
    }

    public void setIgnoreList(ArrayList<String> ignoreList) {
        this.ignoreList = ignoreList;
    }

    public boolean getAllowNotifications() {
        return allowNotifications;
    }

    public void setAllowNotifications(Boolean allowNotifications) {
        this.allowNotifications = allowNotifications;
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
        if (ignoreList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ignoreList);
        }
        dest.writeByte((byte) (allowNotifications ? 0x01 : 0x00));
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
