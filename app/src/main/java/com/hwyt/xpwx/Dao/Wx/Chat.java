package com.hwyt.xpwx.Dao.Wx;

import com.hwyt.xpwx.App;
import com.hwyt.xpwx.Bean.TestSendData;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Chat {
    App app = null;
    private Timer timer = new Timer();
    public  Chat(App app)
    {
        this.app = app;

    }

    public void chatIndex()
    {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                try {
                    JSONArray ja = MessageUtil.getJAArticle("Wx/chatListOut");
                    if(ja!=null) {

                        MessageUtil.saveJbArticle("","Wx","chatListOut");

                        if(ja.length()>0){
                            for(int i=0;i<ja.length();i++){
                                JSONObject jsonObject = ja.getJSONObject(i);

                                jsonObject.put("fun", "client");
                                jsonObject.put("model", "Wx");
                                jsonObject.put("controller", "Chat");
                                jsonObject.put("action", "index");
                                jsonObject.put("ws_fd", app.getWSFd());
                                jsonObject.put("m_fd", app.getFd());
                                jsonObject.put("m_id", app.getMID());
                                jsonObject.put("mer_id", app.getMerID());
                                jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                                jsonObject.put("username", jsonObject.get("field_username"));

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
    }
}
