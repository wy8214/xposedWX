package com.hwyt.xpwx;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


import com.hwyt.xpwx.Utils.common.DeviceUtil;
import com.hwyt.xpwx.Utils.logger.MyLog;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;


import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * sdcard/fengchaoyun  约定用来存放apk的位置
 * //todo app启动的时候检查这个目录是否存在，如果没有则需要建立这个目录
 */
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInit();
        instance = this;
        String processName = DeviceUtil.getProcessName(getInstance(), android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(BuildConfig.APPLICATION_ID);
            //防止子进程也进行初始化
            if (defaultProcess) {
                init();
            }
        }
    }

    public void appInit()
    {
        setSocketIp("0.0.0.0"); //初始化全局变量
        setSocketPort(9507);
        setDeviceType("0");
        setMerID("0");

        setWxObjectMap(new HashMap<String,Object>());
    }

    private void init() {
        MyLog.init();
        //todo 初始化socket连接
    }

    /**
     * 从apk文件中获取apk信息
     * @param apkFilePath
     * @return
     */
    public static ApkMeta getApkInfoFromApkFile(String apkFilePath) {
        try (ApkFile apkFile = new ApkFile(new File(apkFilePath))) {
            return apkFile.getApkMeta();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String wsfd;
    public String getWSFd(){
        return wsfd;
    }
    public void setWSFd(String wsfd){

        this.wsfd = wsfd;
    }

    private String fd;
    public String getFd(){
        return fd;
    }
    public void setFd(String fd){

        this.fd = fd;
    }

    private String mID;
    public String getMID(){
        return mID;
    }
    public void setMID(String mID){

        this.mID = mID;
    }

    private String merID;
    public String getMerID(){
        return merID;
    }
    public void setMerID(String merID){
        this.merID = merID;
    }

    private String deviceType;
    public String getDeviceType(){
        return deviceType;
    }
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }


    private Context context;

    public Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context = context;
    }


    private ConnectionInfo socketinfo;

    public ConnectionInfo getSocketinfo(){
        return socketinfo;
    }
    public void setSocketinfo(ConnectionInfo socketinfo){
        this.socketinfo = socketinfo;
    }





    private Map<String,Object> wxObjectMap;
    public  Map<String,Object> getWxObjectMap(){
        return wxObjectMap;
    }
    public void setWxObjectMap( Map<String,Object> list) {
        this.wxObjectMap = list;
    }


    private String socketIp;
    public String getSocketIp(){
        return socketIp;
    }
    public void setSocketIp(String socketIp){
        this.socketIp = socketIp;
    }

    private String merPhone;
    public String getMerPhone(){
        return merPhone;
    }
    public void setMerPhone(String merPhone){
        this.merPhone = merPhone;
    }

    private int socketPort;
    public int getSocketPort(){
        return socketPort;
    }
    public void setSocketPort(int socketPort){
        this.socketPort = socketPort;
    }


    private Handler handler;

    public Handler getHandler(){
        return handler;
    }
    public void setHandler(Handler handler){
        this.handler = handler;
    }
}

