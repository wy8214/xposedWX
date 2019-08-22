package com.hwyt.xpwx.Bean;

public class ApkBean {
//    `id` int(11) NOT NULL AUTO_INCREMENT,
//  `mer_id` int(11) NOT NULL,
//  `apk_name` varchar(50) DEFAULT NULL,
//  `apk_package_name` varchar(50) DEFAULT NULL,
//  `apk_icon` varchar(255) DEFAULT NULL,
//  `apk_oss_file_url` varchar(255) DEFAULT NULL,
//  `apk_local_path` varchar(255) DEFAULT NULL,
//  `file_name` varchar(100) DEFAULT NULL,
//  `apk_file_size` varchar(10) DEFAULT NULL,
//  `status` tinyint(1) DEFAULT NULL,
//  `created_at` datetime DEFAULT NULL,
//            `updated_at` datetime DEFAULT NULL,
    private String ID;
    private String merID;
    private String apkName;
    private String apkPackageName;
    private String apkIcon;
    private String apkOssFileUrl;
    private String apkLocalPath;
    private String fileName;
    private String apkFileSize;
    private String createdAt;
    private String updatedAt;
    private String deleteddAt;

    private String installedNum;

    private String uploadProgress;

    private boolean isExist;

    private int state;

    private String stateName;

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeleteddAt() {
        return deleteddAt;
    }

    public void setDeleteddAt(String deleteddAt) {
        this.deleteddAt = deleteddAt;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(String uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public boolean isExist() {
        return isExist;
    }

    public String getInstalledNum() {
        return installedNum;
    }

    public void setInstalledNum(String installedNum) {
        this.installedNum = installedNum;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMerID() {
        return merID;
    }

    public void setMerID(String merID) {
        this.merID = merID;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public String getApkIcon() {
        return apkIcon;
    }

    public void setApkIcon(String apkIcon) {
        this.apkIcon = apkIcon;
    }

    public String getApkOssFileUrl() {
        return apkOssFileUrl;
    }

    public void setApkOssFileUrl(String apkOssFileUrl) {
        this.apkOssFileUrl = apkOssFileUrl;
    }

    public String getApkLocalPath() {
        return apkLocalPath;
    }

    public void setApkLocalPath(String apkLocalPath) {
        this.apkLocalPath = apkLocalPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getApkFileSize() {
        return apkFileSize;
    }

    public void setApkFileSize(String apkFileSize) {
        this.apkFileSize = apkFileSize;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
