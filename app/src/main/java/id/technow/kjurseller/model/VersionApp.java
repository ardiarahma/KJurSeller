package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionApp {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("app_name")
    @Expose
    private String appName;

    @SerializedName("version")
    @Expose
    private int version;

    @SerializedName("sub_version")
    @Expose
    private int versionSub;

    @SerializedName("sub_sub_version")
    @Expose
    private int versionSubSub;

    @SerializedName("year")
    @Expose
    private int year;

    public VersionApp(int id, String appName, int version, int versionSub, int versionSubSub, int year) {
        this.id = id;
        this.appName = appName;
        this.version = version;
        this.versionSub = versionSub;
        this.versionSubSub = versionSubSub;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersionSub() {
        return versionSub;
    }

    public void setVersionSub(int versionSub) {
        this.versionSub = versionSub;
    }

    public int getVersionSubSub() {
        return versionSubSub;
    }

    public void setVersionSubSub(int versionSubSub) {
        this.versionSubSub = versionSubSub;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
