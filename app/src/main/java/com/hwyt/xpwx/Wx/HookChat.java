package com.hwyt.xpwx.Wx;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookChat {

    public static void init(final XC_LoadPackage.LoadPackageParam cl, final Map<String,Object> wxObjectMap)
    {

        final Class<?> hookclass1 ;
//        final Class<?> hookclass3 ;
//        final Class<?> hookclass4 ;
//        final Class<?> hookclass5 ;
        try {
            hookclass1 = cl.classLoader.loadClass("com.tencent.mm.ui.conversation.h");
//            hookclass4 = cl.classLoader.loadClass("com.tencent.mm.cf.f");
//            hookclass5 = cl.classLoader.loadClass("com.tencent.wcdb.database.SQLiteDatabase");
        } catch (Exception e) {
            LogUtils.i("hookChatRoom error " + e.toString());
            return;
        }


//        XposedBridge.hookAllMethods(hookclass5,"rawQueryWithFactory", new XC_MethodHook(){
//
//
//            @Override
//            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//
//                for(int i=0;i<param.args.length;i++)
//                {
//
//                    LogUtils.i( "com.tencent.wcdb.database.SQLiteDatabase.rawQueryWithFactory query " + i + "   " + param.args[i] );
//                }
//
//                if(param.getResult()==null)
//                    return;
//                Cursor cursor = (Cursor)param.getResult();
//
//                if(cursor != null) {
//                    while (cursor.moveToNext()) {
//                      String[] cols =   cursor.getColumnNames();
//
//                      String paramStr1 = "";
//                      for (String col : cols)
//                      {
//                          String val =cursor.getString(cursor.getColumnIndex(col));
//
//                          paramStr1 += " ||| " + col + "    " + val;
//                      }
//
//                        LogUtils.i( "com.tencent.wcdb.database.SQLiteDatabase.rawQueryWithFactory Cursor  " + paramStr1);
//                    }
//                }
//
//
//                }
//        });
//
//
//        XposedBridge.hookAllMethods(hookclass4,"a", new XC_MethodHook(){
//            @Override
//            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//
//
//                for(int i=0;i<param.args.length;i++)
//                {
//
//                    LogUtils.i( "com.tencent.mm.cf.f.a " + i + "   " + param.args[i] );
//                }
//            }
//        });




        XposedBridge.hookAllMethods(hookclass1,"i", new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                for(int i=0;i<param.args.length;i++)
                {
                    LogUtils.i( "com.tencent.mm.ui.conversation.h " + i + "   " + param.args[i] );
                }
                Object o1 = param.args[0];
                Field[] fs1 = o1.getClass().getFields();

                String paramStr1 = "";

                JSONArray  ja = MessageUtil.getJAArticle("Wx/chatListOut");
                if(ja==null)
                    ja = new JSONArray();

                JSONObject jb = new JSONObject();

                boolean is_chat_info = true;

                for (Field f1 : fs1) {
                    f1.setAccessible(true);
                    paramStr1 += " ||| " + f1.getName() + "    " + f1.get(o1);

                    if(f1.get(o1)==null)
                        continue;

                    if(f1.get(o1).equals("newsapp")||f1.get(o1).equals("weixin"))
                    {
                        is_chat_info=false;
                    }

                    if(f1.getName().equals("field_content")||f1.getName().equals("field_digest")||f1.getName().equals("field_username")||f1.getName().equals("field_unReadMuteCount")||f1.getName().equals("field_conversationTime")||f1.getName().equals("field_msgType")) {

                        jb.put(f1.getName(), f1.get(o1));
                    }
                    //群聊天信息
                    if(f1.get(o1).toString().contains("chatroom"))
                    {
                        jb.put("chat_type","chatroom");
//                        wxObjectMap.put("com.tencent.mm.chatroom.ui.ChatroomInfoUI",o1);
                    }else
                    {
                        jb.put("chat_type","chat");
                    }




                }
                LogUtils.i( "com.tencent.mm.ui.conversation.h paramStr1 " +  paramStr1 + " ||| is_chat_info "+ is_chat_info);

                if(is_chat_info) {
                    ja.put(jb);
                    LogUtils.i( "com.tencent.mm.ui.conversation.h paramStr1 " +  ja.toString());
                    MessageUtil.saveJbArticle(ja.toString(), "Wx", "chatListOut");
                }

                jb.put("wxid", jb.get("field_username"));
                HookContact.hookContactAvatar(cl,wxObjectMap,jb);

            }

        });
    }
}
