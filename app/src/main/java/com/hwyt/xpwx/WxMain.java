package com.hwyt.xpwx;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.Wx.HookAccount;
import com.hwyt.xpwx.Wx.HookChat;
import com.hwyt.xpwx.Wx.HookChatRoom;
import com.hwyt.xpwx.Wx.HookContact;
import com.hwyt.xpwx.Wx.HookMessage;
import com.hwyt.xpwx.Wx.HookSns;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * @author hwyt_wy
 * Created at 2019/2/25.
 * @Describe 微信版本为7.0.3  使用请修改手机号／微信号／QQ号
 */
public class WxMain implements IXposedHookLoadPackage {

    public static final String PACKAGE_NAME = "com.tencent.mm";
    private XC_LoadPackage.LoadPackageParam mlpparam = null;
    private  Context mwxContext;

    private final Map<String,Object> wxObjectMap=new HashMap<String,Object>();

    private TimerTask task = null;
    private  Timer timer =null;

    private ArrayList<String> users= null;

    private void callLoop(){

        if(task==null&&mlpparam.packageName.contains("com.tencent.mm")) {

            task = new TimerTask() {
                @Override
                public void run() {

                    try {

                        if (mlpparam.packageName.equals("com.tencent.mm")) {

                            JSONObject jbCall = MessageUtil.getJbArticle("Wx/callLoop");
                            if(jbCall.get("is_run").toString().equals("0")||wxObjectMap.get("com.tencent.mm.storage.aj")==null)
                            {
                                task.cancel();
                                timer.cancel();
                                return;
                            }

                            JSONObject jbpulse = new JSONObject();
                            jbpulse.put("fun", "client");
                            jbpulse.put("model", "Wx");
                            jbpulse.put("controller", "Index");
                            jbpulse.put("action", "wxPulse");
                            jbpulse.put("wx_pulse", "pulse");
                            MessageUtil.saveJbArticle(jbpulse.toString(), "Wx", "wxpulse");

                            LogUtils.i("wx callLoop ing ... " + wxObjectMap.get("com.tencent.mm.storage.aj"));

                            JSONObject jb = MessageUtil.getJbArticle("Wx/message");
                            if (jb != null) {
                                MessageUtil.saveJbArticle("", "Wx", "message");
                                String model = jb.get("model").toString();
                                if (model.equals("Wx")) {

                                    LogUtils.i("Wx callLoop ing ... "+jb.toString());

                                    String className = "com.hwyt.xpwx." + jb.get("model") + ".Hook" + jb.get("controller")+"."+jb.get("action");

                                    if("com.hwyt.xpwx.Wx.HookContact.getContactList".equals(className))
                                    {
                                        HookContact.getContactList(mlpparam,wxObjectMap);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookMessage.sendMessage".equals(className))
                                    {
                                        HookMessage.sendMessage(mlpparam,wxObjectMap,jb);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookMessage.sendImgMessage".equals(className))
                                    {
                                        HookMessage.sendImgMessage(mlpparam,wxObjectMap,jb);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookContact.findContact".equals(className))
                                    {

                                        jb.put("addContact",0);
                                        HookContact.findContact(mlpparam,wxObjectMap,jb);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookContact.addContact".equals(className))
                                    {
                                        jb.put("addContact",1);

                                        HookContact.findContact(mlpparam,wxObjectMap,jb);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookChatRoom.createChatRoom".equals(className))
                                    {
                                        String[] wxids = jb.get("wxids").toString().split(",");
                                        users = new ArrayList();
                                        for (String s : wxids)
                                        {
                                            users.add(s);
                                        }
                                        HookChatRoom.createChatRoom(mlpparam,wxObjectMap,users);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookChatRoom.deleteChatRoom".equals(className))
                                    {
                                        String chatroom_name = jb.get("chatroom_name").toString();

                                        HookChatRoom.deleteChatRoom(mlpparam,wxObjectMap,chatroom_name);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookChatRoom.deleteChatRoom".equals(className))
                                    {
                                        String chatroom_name = jb.get("chatroom_name").toString();

                                        HookChatRoom.deleteChatRoom(mlpparam,wxObjectMap,chatroom_name);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookSns.snsUplad".equals(className))
                                    {
                                        wxObjectMap.put("sns_pics",jb.get("sns_pics").toString());
                                        wxObjectMap.put("sns_content",jb.get("sns_content").toString());
                                        HookSns.snsUpload(mlpparam,wxObjectMap);
                                    }

                                    if("com.hwyt.xpwx.Wx.HookContact.getContactAvatar".equals(className))
                                    {
                                        String wxid = jb.get("wxid").toString();

                                        HashMap<String,JSONObject> avatarMap= (HashMap)wxObjectMap.get("user_avatar");

                                        if(avatarMap.get(wxid)!=null&&!wxid.equals("")) {
                                            avatarMap.remove(wxid);
                                            HookContact.hookContactAvatar(mlpparam,wxObjectMap,jb);
                                        }
                                        if(avatarMap.get(wxid)!=null&&wxid.equals("")) {
                                            wxObjectMap.put("user_avatar" ,new HashMap<String, JSONObject>());

                                            for (String key : avatarMap.keySet()) {

                                                JSONObject jsonObject = avatarMap.get("key");
                                                jsonObject.put("local_path","");
                                                jsonObject.put("url_path","");

                                                HookContact.hookContactAvatar(mlpparam,wxObjectMap,jsonObject);
                                            }

                                        }

                                    }

                                }
                            }
                        }
                    } catch (Exception e) {
                        LogUtils.i("callLoop error " + e.toString());
                        return;
                    }
                }
            };
            timer = new Timer();
            timer.schedule(task, 40000, 3000);
        }

    }
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        LogUtils.i(loadPackageParam.packageName);
        if (loadPackageParam.packageName.contains("com.hwyt.xpwx")) {
            wxObjectMap.put("com.hwyt.xpwx",loadPackageParam);
        }

        if (loadPackageParam.packageName.equals("com.tencent.mm")) {



            wxObjectMap.put("com.tencent.mm",loadPackageParam);

            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    final Context context = ((Context) param.args[0]);
                    LogUtils.i("Application "+"afterHookedMethod========" + context.getPackageName() + ":" + context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionName);
                    mlpparam = loadPackageParam;



                    //启动微信监听指令
                    JSONObject jb = new JSONObject();
                    jb.put("is_run","0");
                    MessageUtil.saveJbArticle(jb.toString(), "Wx", "callLoop");
                    TimerTask task1 = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jb = new JSONObject();
                                jb.put("is_run","1");
                                MessageUtil.saveJbArticle(jb.toString(), "Wx", "callLoop");
                                callLoop();
                            } catch (Exception e) {
                                LogUtils.i("callLoop error " + e.toString());
                                return;
                            }
                        }
                    };
                    Timer timer1 = new Timer();
                    timer1.schedule(task1, 1000);


                    //初始化微信好友头像
                    HashMap<String,JSONObject>  avatarMap = new HashMap<>();

                    JSONArray ja = MessageUtil.getJAArticle("Wx/contactAvatarOut");
                    if(ja!=null&&ja.length()>0)
                    {
                        for (int i=0;i<ja.length();i++)
                        {
                            JSONObject jsonObject = ja.getJSONObject(i);
                            avatarMap.put(jsonObject.get("wxid").toString(),jsonObject);
                        }
                    }
                    wxObjectMap.put("user_avatar",avatarMap);


                    HookSns.init(mlpparam,wxObjectMap);

                    //微信账号管理
                    HookAccount.init(mlpparam,wxObjectMap);

                    //微信消息管理
                    HookMessage.init(mlpparam,wxObjectMap);
                    //通讯录管理
                    HookContact.init(mlpparam,wxObjectMap);

                    HookChat.init(mlpparam,wxObjectMap);

                    HookChatRoom.init(mlpparam,wxObjectMap);

                }
            });
        }
    }


}
