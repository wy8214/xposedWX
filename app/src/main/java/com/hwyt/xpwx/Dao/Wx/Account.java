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

public class Account {

    private App app;
    private Timer timer = new Timer();
    public  Account(App app)
    {
        this.app = app;
    }

    public void getAccount(JSONObject jb)
    {
        try{
            MessageUtil.saveJbArticle(jb.toString(),"Wx","message");

            TimerTask task = new TimerTask() {

                private  int n = 0;
                @Override
                public void run() {
                    if (n > 10) {
                        timer.cancel();
                        return;
                    }
                    n++;
                    try {
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/getAccountResultOut");
                        if (jsonObject != null) {
                            MessageUtil.saveJbArticle("", "Wx", "getAccountResultOut");

                            JSONArray ja = MessageUtil.getJAArticle("Wx/contactAvatarOut");
                            if (ja != null && ja.length() > 0) {
                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jsonObject1 = ja.getJSONObject(i);

                                    if (jsonObject1.getString("wxid").equals(jsonObject.getString("wxid")))
                                    {
                                        String url = jsonObject1.get("url_path").toString();
                                        if (url.equals("")) {
                                            String wxid = jsonObject1.get("wxid").toString();//"wxid_9c5k5l3gnvkj22";
                                            String localFileName = Environment.getExternalStorageDirectory() + "/Wx/images/" + wxid + ".jpg";

                                            final String ossFilePath = MobileUtils.getMoblieIMEI(app.getContext()) + "/" + "wx_avatar" + "/" + wxid + ".jpg";
                                            OssFileMethod ofm = new OssFileMethod(app.getContext());
                                            ofm.uploadAsyncFile(ossFilePath, localFileName);
                                            url = "http://" + ofm.bucketName + "." + ofm.domain + "/" + ossFilePath;

                                            jsonObject1.put("url_path", url);
                                        }

                                        jsonObject.put("fun", "client");
                                        jsonObject.put("model", "Wx");
                                        jsonObject.put("controller", "Account");
                                        jsonObject.put("action", "getAccountResult");
                                        jsonObject.put("avatar_url", url);
                                        jsonObject.put("ws_fd", app.getWSFd());
                                        jsonObject.put("m_fd", app.getFd());
                                        jsonObject.put("m_id", app.getMID());
                                        jsonObject.put("mer_id", app.getMerID());
                                        jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                                        OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));

                                        MessageUtil.saveJbArticle(ja.toString(), "Wx", "contactAvatarOut");
                                        break;
                                    }
                                }
                                n = 11;
                            }
                        }
                    }catch(Exception e){
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
