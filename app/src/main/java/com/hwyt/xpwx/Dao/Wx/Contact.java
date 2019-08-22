package com.hwyt.xpwx.Dao.Wx;

import android.os.Environment;

import com.hwyt.xpwx.App;
import com.hwyt.xpwx.Bean.TestSendData;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.Utils.common.OssFileMethod;
import com.hwyt.xpwx.log.LogUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Contact {

    private final App app;

    private Timer timer = new Timer();

    public  Contact(App app)
    {
        this.app = app;
    }

    public void getContactList(JSONObject jb)
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
                        JSONArray ja = MessageUtil.getJAArticle("Wx/contactListOut");
                        if(ja!=null) {

                            MessageUtil.saveJbArticle("","Wx","contactListOut");

                            if(ja.length()>0){
                                for(int i=0;i<ja.length();i++){
                                    JSONObject jsonObject = ja.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象

                                    jsonObject.put("fun", "client");
                                    jsonObject.put("model", "Wx");
                                    jsonObject.put("controller", "Contact");
                                    jsonObject.put("action", "uploadContact");
                                    jsonObject.put("ws_fd", app.getWSFd());
                                    jsonObject.put("m_fd", app.getFd());
                                    jsonObject.put("m_id", app.getMID());
                                    jsonObject.put("mer_id", app.getMerID());
                                    jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                                    OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));
                                }
                            }

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

    public void findContact(JSONObject jb)
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
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/findContactResultOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","findContactResultOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", "Wx");
                            jsonObject.put("controller", "Contact");
                            jsonObject.put("action", "findContactResult");
                            jsonObject.put("result", jsonObject.get("result").toString());
                            jsonObject.put("phone", jsonObject.get("phone").toString());
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



    public void addContact(JSONObject jb)
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
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/addContactResultOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","addContactResultOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", "Wx");
                            jsonObject.put("controller", "Contact");
                            jsonObject.put("action", "addContactResult");
                            jsonObject.put("result", jsonObject.get("result").toString());
                            jsonObject.put("phone", jsonObject.get("phone").toString());
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





    public void getContactAvatar(JSONObject jb)
    {
        try{
//            final String wxid =  jb.get("wxid").toString();
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

                        JSONArray ja = MessageUtil.getJAArticle("Wx/contactAvatarOut");
                        if(ja!=null&&ja.length()>0)
                        {
                            for (int i=0;i<ja.length();i++)
                            {
                                JSONObject jsonObject = ja.getJSONObject(i);

                                if(jsonObject.get("url_path").toString().equals(""));
                                {
                                    String wxid = jsonObject.get("wxid").toString();//"wxid_9c5k5l3gnvkj22";
                                    String localFileName = Environment.getExternalStorageDirectory() + "/Wx/images/" + wxid + ".jpg";

                                    final  String ossFilePath  = MobileUtils.getMoblieIMEI(app.getContext())+"/" + "wx_avatar"+"/" + wxid+".jpg";
                                    OssFileMethod ofm = new OssFileMethod(app.getContext());
                                    ofm.uploadAsyncFile(ossFilePath,localFileName);

                                    String url = "http://"+ ofm.bucketName + "."+ ofm.domain+"/"+ossFilePath;

                                    jsonObject.put("url_path",url);

                                    jsonObject.put("fun", "client");
                                    jsonObject.put("model", "Wx");
                                    jsonObject.put("controller", "Contact");
                                    jsonObject.put("action", "getContactAvatarResult");
                                    jsonObject.put("avatar_url", url);
                                    jsonObject.put("wxid", wxid);
                                    jsonObject.put("ws_fd", app.getWSFd());
                                    jsonObject.put("m_fd", app.getFd());
                                    jsonObject.put("m_id", app.getMID());
                                    jsonObject.put("mer_id", app.getMerID());
                                    jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                                    OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));

                                }

                            }

                            MessageUtil.saveJbArticle(ja.toString(), "Wx", "contactAvatarOut");
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
