package com.hellokiki.autoupdate.bean;

public class Apkinfo {

    public static final int UPDATE_TYPE_TIPS = 1;
    public static final int UPDATE_TYPE_MANDATORY = 2;
    public static final int UPDATE_TYPE_WIFI = 3;

    public static final int PROGRESS_TYPE_DIALOG = 1;
    public static final int PROGRESS_TYPE_NOTIFY = 2;

    private String name;

    private String packageName;

    private String VersionCode;

    private String VersionName;

    private String minSdkVersion;

    private int updateType; // 1:提示更新，2：强制更新，3：wifi静默下载

    private int progressNotifyType; //1: 对话框进度  2：通知栏

    private String updateContent;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(String minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public int getProgressNotifyType() {
        return progressNotifyType;
    }

    public void setProgressNotifyType(int progressNotifyType) {
        this.progressNotifyType = progressNotifyType;
    }
}
