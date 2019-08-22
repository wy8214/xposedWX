package com.hwyt.xpwx.Wx;
import android.os.Bundle;

import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.log.LogUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookChatRoom {

    private static ArrayList<String> users= null;

    public static void init(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap)
    {
        final Class<?> hookclass2 ;
        final Class<?> hookclass3 ;
        final Class<?> hookclass6 ;

        try {
            hookclass2 = mlpparam.classLoader.loadClass("com.tencent.mm.ui.contact.SelectContactUI");
            hookclass3 = mlpparam.classLoader.loadClass("com.tencent.mm.chatroom.ui.ChatroomInfoUI");
            hookclass6 = mlpparam.classLoader.loadClass("com.tencent.mm.ui.chatting.c.t");
        } catch (Exception e) {
            LogUtils.i( "hookChatRoom error " + e.toString());
            return;
        }

        XposedBridge.hookAllMethods(hookclass3,"onCreate", new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                LogUtils.i( "com.tencent.mm.chatroom.ui.ChatroomInfoUI  onCreate "  );

                if(wxObjectMap.get("is_delete_chatroom")=="true") {
                    XposedHelpers.callMethod(param.thisObject, "v", param.thisObject);
                    wxObjectMap.put("is_delete_chatroom","false");
                }
            }
        });


        XposedBridge.hookAllConstructors(hookclass6, new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                //获取打开群详情实例
                wxObjectMap.put("com.tencent.mm.ui.chatting.c.t",param.thisObject);
            }
        });


        XposedBridge.hookAllMethods(hookclass2,"onCreate", new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                LogUtils.i( "com.tencent.mm.ui.contact.SelectContactUI  onCreate" );
                wxObjectMap.put("com.tencent.mm.ui.contact.SelectContactUI",param.thisObject);
                if(users!=null) {
                    HookChatRoom.createChatRoom(mlpparam, wxObjectMap,users);
                }
            }
        });


        XposedBridge.hookAllMethods(hookclass2,"qd",new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(users);
                users = null;
            }
        });
    }

    public static void createChatRoom(final XC_LoadPackage.LoadPackageParam cl, final Map<String,Object> wxObjectMap,ArrayList<String> userList)
    {
        try {
            users = userList;
            Object o = wxObjectMap.get("com.tencent.mm.ui.contact.SelectContactUI");
            if(o==null)
            {
                MobileUtils.execShellCmd("am start -n com.tencent.mm/com.tencent.mm.ui.contact.SelectContactUI");
            }
            XposedHelpers.callMethod(o,"e",o);
        }catch (Exception e)
        {
            LogUtils.i("com.tencent.mm.ui.contact.SelectContactUI error " + e.toString());
        }
    }


    public static void deleteChatRoom(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap,String chatRoomName)
    {
        Object LauncherUI =  wxObjectMap.get("LauncherUI");
        LogUtils.i("LauncherUI startChatting " + LauncherUI);
        if(LauncherUI!=null) {

            wxObjectMap.put("is_delete_chatroom","true");
            String var1 = chatRoomName;//"10681249845@chatroom";

            Bundle var2 = new Bundle();
            var2.putInt("specific_chat_from_scene", 7);
            var2.putInt("KOpenArticleSceneFromScene", 93);

            XposedHelpers.callMethod(LauncherUI,"startChatting", var1,var2,true);

        }

        try {
            TimerTask task2 = new TimerTask() {
                @Override
                public void run() {
                    try {

                        LogUtils.i( "com.tencent.mm.ui.chatting.c.t  object " + wxObjectMap.get("com.tencent.mm.ui.chatting.c.t"));
                        if(wxObjectMap.get("com.tencent.mm.ui.chatting.c.t")!=null) {
                            Object o = XposedHelpers.newInstance(mlpparam.classLoader.loadClass("com.tencent.mm.ui.chatting.c.t$a"),wxObjectMap.get("com.tencent.mm.ui.chatting.c.t"));
                            XposedHelpers.callMethod(o, "II");
                        }

                    } catch (Exception e) {
                        LogUtils.i("com.tencent.mm.ui.chatting.c.t error " + e.toString());
                        return;
                    }
                }
            };
            Timer timer2 = new Timer();
            timer2.schedule(task2, 3000);

        }catch (Exception e)
        {
            LogUtils.i( "com.tencent.mm.ui.chatting.c.t error " + e.toString()  );
        }

    }

}
