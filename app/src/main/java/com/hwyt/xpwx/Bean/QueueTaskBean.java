package com.hwyt.xpwx.Bean;

public class QueueTaskBean {

    private String ID;
    private String TaskName;
    private String TaskNum;
    private String TaskTime;
    private String DecviceCount;
    private String CreatedAt;

    private String Status;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskNum() {
        return TaskNum;
    }

    public void setTaskNum(String taskNum) {
        TaskNum = taskNum;
    }

    public String getTaskTime() {
        return TaskTime;
    }

    public void setTaskTime(String taskTime) {
        TaskTime = taskTime;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getDecviceCount() {
        return DecviceCount;
    }

    public void setDecviceCount(String decviceCount) {
        DecviceCount = decviceCount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
