package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionAppResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private VersionApp versionApp;

    public VersionAppResponse(String status, VersionApp versionApp) {
        this.status = status;
        this.versionApp = versionApp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VersionApp getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(VersionApp versionApp) {
        this.versionApp = versionApp;
    }
}
