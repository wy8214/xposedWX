package com.hwyt.xpwx.Bean;

public class CmdBean {

    private int ID;
    private int MerID;
    private String Name;
    private String CmdCode;
    private String FileName;
    private String ApkPackageName;
    private String OssFilePath;
    private String LocalPath;
    private String FileSize;
    private String CreatedAt;
    private String DeletedAt;
    private String Status;

    private String cmdPhoneID;
    private boolean isExist;

    private boolean isSelected;

    public String getStatus() {
        return Status;
    }

    public String getCmdPhoneID() {
        return cmdPhoneID;
    }

    public void setCmdPhoneID(String cmdPhoneID) {
        this.cmdPhoneID = cmdPhoneID;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCmdCode() {
        return CmdCode;
    }

    public void setCmdCode(String cmdCode) {
        CmdCode = cmdCode;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMerID() {
        return MerID;
    }

    public void setMerID(int merID) {
        MerID = merID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getApkPackageName() {
        return ApkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        ApkPackageName = apkPackageName;
    }

    public String getOssFilePath() {
        return OssFilePath;
    }

    public void setOssFilePath(String ossFilePath) {
        OssFilePath = ossFilePath;
    }

    public String getLocalPath() {
        return LocalPath;
    }

    public void setLocalPath(String localPath) {
        LocalPath = localPath;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    private String UpdatedAt;

    public String getDeletedAt() {
        return DeletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        DeletedAt = deletedAt;
    }
}
