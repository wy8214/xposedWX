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

public class Index {

    App app = null;
    private Timer timer = new Timer();
    public  Index(App app)
    {
        this.app = app;

    }
    public void  init(JSONObject jb)
    {
        try {
            app.setMerID(jb.get("mer_id").toString());
            app.setWSFd(jb.get("ws_fd").toString());
            app.setFd(jb.get("m_fd").toString());
            app.setMID(jb.get("m_id").toString());
        } catch (Exception ex) {
            LogUtils.i(ex.toString());
        }
    }

    private int count = 0;
    private String wxStatus = "";
    public void wxpulse()
    {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                count++;

                if(wxStatus!="2")
                    wxStatus  = "3";//掉线
                try {
                    JSONObject jsonObject = MessageUtil.getJbArticle("Wx/wxpulse");

                    if(count>10)
                    {
                        count = 0;
                        wxStatus  = "2";//重启中
                        MobileUtils.execShellCmd("am force-stop com.tencent.mm");
                        Thread.sleep(3000);

                        MobileUtils.execShellCmd("am start -n com.tencent.mm/com.tencent.mm.ui.LauncherUI");
                    }

                    if(jsonObject!=null) {
                        count = 0;
                        wxStatus  = "1";
                        MessageUtil.saveJbArticle("","Wx","wxpulse");

                    }else {
                        jsonObject = new JSONObject();
                    }

                    jsonObject.put("fun", "client");
                    jsonObject.put("model", "Wx");
                    jsonObject.put("controller", "Index");
                    jsonObject.put("action", "wxPulse");
                    jsonObject.put("wx_status", wxStatus);
                    jsonObject.put("ws_fd", app.getWSFd());
                    jsonObject.put("m_fd", app.getFd());
                    jsonObject.put("m_id", app.getMID());
                    jsonObject.put("mer_id", app.getMerID());
                    jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                    OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));


                }catch (Exception e) {
                    LogUtils.i("uploadContact" + e.toString());
                    return;
                }
            }
        };
        timer.schedule(task, 3000,10000);
    }
}
