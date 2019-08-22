package com.hwyt.xpwx.Wx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookAccount {

    public static  void  init (final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap) {
        hookAccountVoid(mlpparam,wxObjectMap);
    }

    public static  void  hookAccountVoid (final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap)
    {
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.tencent.mm.ui.LauncherUI", mlpparam.classLoader), "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {

                wxObjectMap.put("LauncherUI", param.thisObject);

                LogUtils.i("com.tencent.mm.ui.LauncherUI.onCreate");
                SharedPreferences sharedPreferences = ((Activity) param.thisObject).getSharedPreferences("com.tencent.mm_preferences", 0);
                String  login_weixin_username =    sharedPreferences.getString("login_weixin_username", "null");
                String last_login_nick_name =   sharedPreferences.getString("last_login_nick_name", "null");
                String login_user_name =   sharedPreferences.getString("login_user_name", "null");
                String last_login_uin =   sharedPreferences.getString("last_login_uin", "null");

                JSONObject jb = new JSONObject();
                jb.put("wxid",login_weixin_username);
                jb.put("nickname",last_login_nick_name);
                jb.put("alias",login_user_name);

                HookContact.hookContactAvatar(mlpparam,wxObjectMap,jb);

                MessageUtil.saveJbArticle(jb.toString(),"Wx","getAccountResultOut");

                LogUtils.i(login_weixin_username+"---"+last_login_nick_name+"----"+login_user_name+"----"+last_login_uin);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HookContact.hookContactCall(mlpparam,wxObjectMap);
                    }
                }, 6000);
            }
        });
    }

}
