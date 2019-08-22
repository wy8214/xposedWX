package com.hwyt.xpwx.Dao.Wx;

import android.os.Environment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.hwyt.xpwx.App;
import com.hwyt.xpwx.Bean.TestSendData;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Message {

    private final App app;

    private Timer timer = new Timer();

    public  Message(App app)
    {
        this.app = app;
    }

    public void uploadReciveMessage()
    {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                try {
                    JSONObject jsonObject = MessageUtil.getJbArticle("Wx/reciveMessageOut");
                    if(jsonObject!=null) {

                        MessageUtil.saveJbArticle("","Wx","reciveMessageOut");

                        jsonObject.put("fun", "client");
                        jsonObject.put("model", "Wx");
                        jsonObject.put("controller", "Message");
                        jsonObject.put("action", "uploadMessage");
                        jsonObject.put("username", jsonObject.get("r_wxid").toString());
                        jsonObject.put("r_wxid", jsonObject.get("r_wxid").toString());
                        jsonObject.put("content", jsonObject.get("content").toString());
                        jsonObject.put("ws_fd", app.getWSFd());
                        jsonObject.put("m_fd", app.getFd());
                        jsonObject.put("m_id", app.getMID());
                        jsonObject.put("mer_id", app.getMerID());
                        jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                        OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));


                    }


                }catch (Exception e) {
                    LogUtils.i("uploadContact" + e.toString());
                    return;
                }
            }
        };
        timer.schedule(task, 3000,3000);
    }


    public void sendMessage(JSONObject jb)
    {
        try{

            MessageUtil.saveJbArticle(jb.toString(),"Wx","message");
            TimerTask task = new TimerTask() {

                private  int n = 0;
                @Override
                public void run() {
                    if(n>10)
                    {
                        timer.cancel();
                        return;
                    }
                    n++;
                    try {
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/sendMessageOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","sendMessageOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", jsonObject.get("Wx").toString());
                            jsonObject.put("controller", jsonObject.get("Contact").toString());
                            jsonObject.put("action", "sendMessageResult");
                            jsonObject.put("r_wxid", jsonObject.get("r_wxid").toString());
                            jsonObject.put("result", jsonObject.get("result").toString());
                            jsonObject.put("ws_fd", app.getWSFd());
                            jsonObject.put("m_fd", app.getFd());
                            jsonObject.put("m_id", app.getMID());
                            jsonObject.put("mer_id", app.getMerID());
                            jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                            OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));
                            n=11;
                        }


                    }catch (Exception e) {
                        LogUtils.i("uploadContact" + e.toString());
                        return;
                    }
                }
            };
            timer.schedule(task, 3000,3000);
        }catch (Exception e)
        {
            LogUtils.i("getContactList "+e.toString());
        }
    }

    private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Wx/images";//保存的确切位置

    private  int index = 0;
    private  String filePath = "";

    private  JSONObject jsonObject = null;

    public void sendImgMessage(JSONObject jb)
    {
        try{

            this.jsonObject = jb;

            String[] imgs = jb.get("imgs").toString().split(",");

            final int picLength = imgs.length;

            index = 0;

            for (String pic : imgs) {
                String url = pic;
                String fileName = pic.substring(url.lastIndexOf("/") + 1);

                if(!filePath.equals(""))
                    filePath+=",";

                filePath += SAVE_REAL_PATH+"/"+fileName;

                AndroidNetworking.download(url, SAVE_REAL_PATH, fileName)
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                // do anything with progress
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                index++;
                                if(index==picLength)
                                {
                                    try {
                                        jsonObject.put("imgs",filePath);

                                        MessageUtil.saveJbArticle(jsonObject.toString(),"Wx","message");
                                    }catch (Exception e)
                                    {}
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
            }



            TimerTask task = new TimerTask() {

                private  int n = 0;
                @Override
                public void run() {
                    if(n>10)
                    {
                        timer.cancel();
                        return;
                    }
                    n++;
                    try {
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/sendImgMessageResultOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","sendImgMessageResultOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", "Wx");
                            jsonObject.put("controller", "Message");
                            jsonObject.put("action", "sendImgMessageResult");
                            jsonObject.put("result", jsonObject.get("result").toString());
                            jsonObject.put("ws_fd", app.getWSFd());
                            jsonObject.put("m_fd", app.getFd());
                            jsonObject.put("m_id", app.getMID());
                            jsonObject.put("mer_id", app.getMerID());
                            jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                            OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));
                            n=11;
                        }


                    }catch (Exception e) {
                        LogUtils.i("uploadContact" + e.toString());
                        return;
                    }
                }
            };
            timer.schedule(task, 3000,3000);

        }catch (Exception e)
        {
            LogUtils.i("findContact "+e.toString());
        }
    }
}
