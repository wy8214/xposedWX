package com.hwyt.xpwx.Bean;

public class PhoneBean {

    private int id;
    private int fd;
    private String mobile_serila;
    private boolean is_master;
    private boolean is_selected;

    private String phone_number;
    private String group;
    private String location_x;
    private String location_y;
    private String mer_id;
    private String status;

    private String runCmdID;

    private String verison;

    private String mobileCode;

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getMobile_serila() {
        return mobile_serila;
    }

    public void setMobile_serila(String mobile_serila) {
        this.mobile_serila = mobile_serila;
    }

    public String getVerison() {
        return verison;
    }

    public void setVerison(String verison) {
        this.verison = verison;
    }

    public String getRunCmdID() {
        return runCmdID;
    }

    public void setRunCmdID(String runCmdID) {
        this.runCmdID = runCmdID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerID() {
        return mer_id;
    }

    public void setMerID(String mer_id) {
        this.mer_id = mer_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setLocationX(String location_x) {
        this.location_x = location_x;
    }

    public void setLocationY(String location_y) {
        this.location_y = location_y;
    }

    public String getGroup() {

        return group;
    }

    public String getLocationX() {
        return location_x;
    }

    public String getLocationY() {
        return location_y;
    }



    public int getId() {
        return id;
    }

    public int getFd() {
        return fd;
    }

    public String getSerila() {
        return mobile_serila;
    }

    public boolean isMaster() {
        return is_master;
    }

    public boolean isSelected() {
        return is_selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public void setSerila(String mobile_serila) {
        this.mobile_serila = mobile_serila;
    }

    public void setIsMaster(boolean is_master) {
        this.is_master = is_master;
    }

    public void setIsSelected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
