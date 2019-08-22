package com.hwyt.xpwx.Wx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findField;

public class HookContact {

    public static void init(final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap)
    {
        hookSearchContactInit(mlpparam,wxObjectMap);

        hookContactInit(mlpparam,wxObjectMap);
    }

    public static  void getContactList(final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap)
    {
        try {

            LogUtils.i(  " getContactList  hooking........");
             XposedHelpers.callMethod(wxObjectMap.get("com.tencent.mm.storage.aj"),"a",wxObjectMap.get("com.tencent.mm.storage.aj.p0"),wxObjectMap.get("com.tencent.mm.storage.aj.p1"),wxObjectMap.get("com.tencent.mm.storage.aj.p2"),wxObjectMap.get("com.tencent.mm.storage.aj.p3"),wxObjectMap.get("com.tencent.mm.storage.aj.p4"),wxObjectMap.get("com.tencent.mm.storage.aj.p5"));
        }catch (Exception e)
        {
            LogUtils.i(  " error "+e.toString());
        }
    }


    public static  void hookContactCall(final XC_LoadPackage.LoadPackageParam mlpparam,Map<String,Object> wxObjectMap)
    {
        try {
            Object LauncherUIObj =  wxObjectMap.get("LauncherUI");

            Field homeUi=  LauncherUIObj.getClass().getDeclaredField("xwg");

            Object homeUiObject = homeUi.get(LauncherUIObj);

            Object MainTabUI = XposedHelpers.callMethod(homeUiObject,"getMainTabUI");

            LogUtils.i("MainTabUI : " + MainTabUI.toString());
            XposedHelpers.callMethod(MainTabUI,"Me",1);

        }catch (Exception e)
        {
            LogUtils.i(  " error "+e.toString());
        }
    }

    public static  void  hookContactInit (final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap)
    {
        Class ajClazz =XposedHelpers.findClass("com.tencent.mm.storage.aj", mlpparam.classLoader); ;
        XposedBridge.hookAllMethods(ajClazz, "a",  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.i(" com.tencent.mm.storage.aj "+ param.args.length);

                wxObjectMap.put("com.tencent.mm.storage.aj",param.thisObject);

                if(param.args.length!=6)
                    return;

                wxObjectMap.put("com.tencent.mm.storage.aj.p0",param.args[0]);
                wxObjectMap.put("com.tencent.mm.storage.aj.p1",param.args[1]);
                wxObjectMap.put("com.tencent.mm.storage.aj.p2",param.args[2]);
                wxObjectMap.put("com.tencent.mm.storage.aj.p3",param.args[3]);
                wxObjectMap.put("com.tencent.mm.storage.aj.p4",param.args[4]);
                wxObjectMap.put("com.tencent.mm.storage.aj.p5",param.args[5]);


                List<JSONObject> userList = new ArrayList<JSONObject>() ;
                Cursor cursor = (Cursor)param.getResult();
                String[] fileds =new String[]{"username","nickname","alias","conRemark","verifyFlag","showHead","weiboFlag","rowid","deleteFlag","lvbuff","descWordingId","openImAppid"};
                while(cursor.moveToNext())
                {
                    JSONObject jb = new JSONObject();
                    for (String filed : fileds)
                    {
                        try {
                            LogUtils.i(   " com.tencent.mm.storage.aj filed "+filed);
                            jb.put(filed,cursor.getString(cursor.getColumnIndex(filed)));
                        }catch (Exception e)
                        {
                            LogUtils.i(  " error "+e.toString());
                        }

                    }
                    LogUtils.i(  " com.tencent.mm.storage.aj jb"+jb.toString());
                    userList.add(jb);
                }

                MessageUtil.saveJbArticle(userList.toString(),"Wx","contactListOut");
                LogUtils.i(  " com.tencent.mm.storage.aj userList"+userList.toString());
            }
        });

    }


    public static  void findContact(final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap,JSONObject jb)
    {
        try {
            searchPhone = jb.get("phone").toString();
            addContact = jb.get("addContact").toString();

            if(addContact.equals("1")) {
                titleContact = jb.get("title").toString();
                descContact = jb.get("desc").toString();
            }
            MobileUtils.execShellCmd("am start -n com.tencent.mm/com.tencent.mm.plugin.fts.ui.FTSAddFriendUI");

        }catch (Exception e)
        {
            LogUtils.i(  "findContact  error "+e.toString());
        }
    }

    public static  void hookContactAvatar(final XC_LoadPackage.LoadPackageParam mlpparam,Map<String,Object> wxObjectMap,JSONObject jb)
    {
        try {
            String user_name = jb.get("wxid").toString();
            HashMap<String,JSONObject> avatarMap = (HashMap) wxObjectMap.get("user_avatar");

            if(avatarMap.get(user_name)!=null)
            {
                return;
            }
            final Class<?> hookclass6 ;
            hookclass6 = mlpparam.classLoader.loadClass("com.tencent.mm.ag.d");

            Bitmap bitmap = (Bitmap) XposedHelpers.callStaticMethod(hookclass6,"pE",user_name);
            MessageUtil.saveImage("Wx/images",user_name,bitmap);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("wxid",user_name);
            jsonObject.put("local_path","Wx/images/"+user_name+".jpg");
            jsonObject.put("url_path","");

            avatarMap.put(user_name,jsonObject);
            JSONArray ja = new JSONArray();
            for (String key : avatarMap.keySet()) {
                ja.put(avatarMap.get(key));
            }

            MessageUtil.saveJbArticle(ja.toString(), "Wx", "contactAvatarOut");

        }catch (Exception e)
        {
            LogUtils.i(  "com.tencent.mm.ag.ds error "+e.toString());
        }
    }
    private static boolean isFromAddFriend = false;
    private static String searchContactThumbUrl = "";
    private static String searchContactUrl = "";

    private static String Contact_Nick = "";
    private static String Contact_RegionCode = "";

    private static String addContact = "0";
    private static String titleContact = "";
    private static String descContact = "";
    private static String searchPhone = "";



    private static void hookSearchContactInit(final XC_LoadPackage.LoadPackageParam mlpparam,final Map<String,Object> wxObjectMap) {




        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI", mlpparam.classLoader,"onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.i(" com.tencent.mm.plugin.fts.ui.FTSAddFriendUI onCreate");
                wxObjectMap.put("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI",param.thisObject);

                if(searchPhone!="") {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isFromAddFriend = true;
                            XposedHelpers.setObjectField(param.thisObject, "query", searchPhone);
                            XposedHelpers.callMethod(param.thisObject, "Mf", searchPhone);

                        }
                    }, 1000);
                }

            }
        });

        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.tencent.mm.ag.i",mlpparam.classLoader),"b", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                try {
                    LogUtils.i("hookSearchContact hookAllMethods" + param.args.length);
                    for (int i=0;i<param.args.length;i++) {
                        if(param.args[i]!=null){
                            LogUtils.i("hookSearchContact hookAllMethods " + i +" " + param.args[i]);
                        }

                        Object o = param.args[i];
                        Field[] fs = param.args[i].getClass().getDeclaredFields();

                        for (Field f : fs) {
                            f.setAccessible(true);
                            LogUtils.i("hookSearchContact param " + f.getName() + "    " + f.get(o));

                            if(f.getName().equals("fcD"))
                            {
                                searchContactThumbUrl = f.get(o).toString();
                            }

                            if(f.getName().equals("fcE"))
                            {
                                searchContactUrl = f.get(o).toString();
                            }
                        }
                    }

                }catch (Exception e)
                {
                    LogUtils.i( "hookSearchContact errot  "+ e.toString());
                }
            }
        });


        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.tencent.mm.plugin.messenger.a.f",mlpparam.classLoader),"a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                try {
                    LogUtils.i("hookSearchContact com.tencent.mm.plugin.messenger.a.f" + param.args.length);

                    if(param.args.length<6)
                        return;

                    String result = "false";
                    if(!param.args[3].toString().contains("OK"))
                    {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("phone", searchPhone);
                        jsonObject.put("result",result);

                        MessageUtil.saveJbArticle(jsonObject.toString(),"Wx","findContactResultOut");
                        searchPhone = "";
                    }

                }catch (Exception e)
                {
                    LogUtils.i( "hookSearchContact errot  "+ e.toString());
                }

            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.profile.ui.ContactInfoUI", mlpparam.classLoader,"onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtils.i(" com.tencent.mm.plugin.profile.ui.ContactInfoUI  onCreate" + searchPhone);
                wxObjectMap.put("com.tencent.mm.plugin.profile.ui.ContactInfoUI",param.thisObject);

                if(searchPhone!="") {
                    Intent intent =(Intent) XposedHelpers.callMethod(param.thisObject,"getIntent");

                    String[] fileds = new String[]{"Contact_Nick","Contact_Province","Contact_City","Contact_Province","Contact_Signature","Contact_VUser_Info","Contact_Distance","Contact_KWeibo","Contact_KWeiboNick","Contact_KFacebookName","Contact_BrandIconURL","Contact_RegionCode",};

                    for (String s : fileds) {
                        LogUtils.i( "hookSearchContact hookAllMethods  "+ s +" : " + intent.getStringExtra(s));
                    }

                    Contact_Nick = intent.getStringExtra("Contact_Nick");
                    Contact_RegionCode = intent.getStringExtra("Contact_RegionCode");

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("phone", searchPhone);
                    jsonObject.put("result","true");
                    jsonObject.put("contact_nick",Contact_Nick);
                    jsonObject.put("contact_regioncode",Contact_RegionCode);
                    jsonObject.put("contact_thumburl",searchContactThumbUrl);
                    jsonObject.put("contact_url",searchContactUrl);

                    MessageUtil.saveJbArticle(jsonObject.toString(),"Wx","findContactResultOut");
                    searchPhone = "";

                }

                if(addContact.equals("1"))
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Object oHt = XposedHelpers.findField(mlpparam.classLoader.loadClass("com.tencent.mm.plugin.profile.ui.ContactInfoUI"), "oHt").get(param.thisObject);

                                XposedHelpers.callMethod(oHt, "EF", "contact_profile_add_contact");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, 1000);

                }

            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI", mlpparam.classLoader,"onCreate", Bundle.class,  new XC_MethodHook() {



            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (addContact.equals("0")) {
                    return;
                }

                wxObjectMap.put("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI",param.thisObject);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            EditText editText = (EditText) XposedHelpers.findField( mlpparam.classLoader.loadClass("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI"), "oLC").get(param.thisObject);
                            editText.setText(titleContact);
                            EditText editText1 = (EditText) XposedHelpers.findField(mlpparam.classLoader.loadClass("com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI"), "oLD").get(param.thisObject);
                            editText1.setText(descContact);


                        } catch (Exception e) {
                            LogUtils.i(e.toString());
                        }
                    }
                }, 100);
            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.ui.q",mlpparam.classLoader,"onCreateOptionsMenu", Menu.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (TextUtils.equals(XposedHelpers.findField( mlpparam.classLoader.loadClass("com.tencent.mm.ui.q"), "xyi").get(param.thisObject).getClass().getSimpleName(), "SayHiWithSnsPermissionUI") && addContact.equals("1")) {


                    LogUtils.i("onCreateOptionsMenu--" +
                            "list(0).xrP=" + XposedHelpers.findField(XposedHelpers.findClass("com.tencent.mm.ui.q$a", mlpparam.classLoader), "xrP").get(((LinkedList) findField( mlpparam.classLoader.loadClass("com.tencent.mm.ui.q"), "xrG").get(param.thisObject)).get(0)) + "===" +
                            "list(0).xyM==null="
                    );

                    final Object menuItem = ((Menu) param.args[0]).getItem(0);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Object c2171a = ((LinkedList) findField( mlpparam.classLoader.loadClass("com.tencent.mm.ui.q"), "xrG").get(param.thisObject)).get(0);
                                Object menuItemClickListener = findField(XposedHelpers.findClass("com.tencent.mm.ui.q$a", mlpparam.classLoader), "gkj").get(c2171a);
                                XposedHelpers.callMethod(menuItemClickListener, "onMenuItemClick", menuItem);

                                addContact = "0";
                                searchPhone = "";
                                descContact = "";
                                titleContact = "";

                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("phone", searchPhone);
                                jsonObject.put("result","true");
                                jsonObject.put("contact_nick",Contact_Nick);
                                jsonObject.put("contact_regioncode",Contact_RegionCode);
                                jsonObject.put("contact_thumburl",searchContactThumbUrl);
                                jsonObject.put("contact_url",searchContactUrl);

                                MessageUtil.saveJbArticle(jsonObject.toString(),"Wx","addContactResultOut");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, 300);
                }
            }
        });

    }
}
