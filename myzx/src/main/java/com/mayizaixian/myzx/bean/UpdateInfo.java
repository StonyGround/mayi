package com.mayizaixian.myzx.bean;

/**
 * APP版本信息
 */
public class UpdateInfo {

    private String version;
    private String apkUrl;
    private String desc;
    private String versionCode;

    public UpdateInfo(String version, String apkUrl, String desc, String versionCode) {
        super();
        this.version = version;
        this.apkUrl = apkUrl;
        this.desc = desc;
        this.versionCode = versionCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "UpdateInfo [version=" + version + ", apkUrl=" + apkUrl
                + ", desc=" + desc + "]";
    }
}
