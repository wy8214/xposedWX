package com.hwyt.xpwx.Dao.Wx;

import com.hwyt.xpwx.App;
import com.hwyt.xpwx.Bean.TestSendData;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ChatRoom {

    private final App app;

    private Timer timer = new Timer();

    public  ChatRoom(App app)
    {
        this.app = app;
    }

    public void createChatRoom(JSONObject jb)
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
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/createChatRoomOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","createChatRoomOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", jsonObject.get("Wx").toString());
                            jsonObject.put("controller", jsonObject.get("ChatRoom").toString());
                            jsonObject.put("action", "createChatResult");
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
                        LogUtils.i("createChatRoom" + e.toString());
                        return;
                    }
                }
            };
            timer.schedule(task, 3000,3000);
        }catch (Exception e)
        {
            LogUtils.i("createChatRoom "+e.toString());
        }
    }


    public void deleteChatRoom(JSONObject jb)
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
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/deleteChatRoomOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","deleteChatRoomOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", jsonObject.get("Wx").toString());
                            jsonObject.put("controller", jsonObject.get("ChatRoom").toString());
                            jsonObject.put("action", "deleteChatResult");
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
                        LogUtils.i("createChatRoom" + e.toString());
                        return;
                    }
                }
            };
            timer.schedule(task, 3000,3000);
        }catch (Exception e)
        {
            LogUtils.i("createChatRoom "+e.toString());
        }
    }
}
