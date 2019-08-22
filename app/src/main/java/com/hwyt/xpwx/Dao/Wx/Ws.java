package com.hwyt.xpwx.Dao.Wx;

import com.hwyt.xpwx.App;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONObject;

public class Ws {

    App app = null;
    public  Ws(App app)
    {
        this.app = app;

    }
    public void  init(JSONObject jb)
    {
        try {
            app.setMerID(jb.get("mer_id").toString());
            app.setWSFd(jb.get("ws_fd").toString());
            app.setFd(jb.get("m_fd").toString());
        } catch (Exception ex) {
            LogUtils.i(ex.toString());
        }
    }
}
