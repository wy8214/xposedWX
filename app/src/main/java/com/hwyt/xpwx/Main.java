package com.hwyt.xpwx;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class Main implements IXposedHookLoadPackage  {
    private String TAG = "xposed hook wx ";

    private Object FTSAddFriendUIObject = null;
    private  ClassLoader classLoader = null;


    private void callLoop(){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                try {
                    XposedBridge.log(TAG +" callLoop is done ");
                    Object FTSAddFriendUIObj = XposedHelpers.newInstance(XposedHelpers.findClass("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI",classLoader));

                    XposedHelpers.callMethod(FTSAddFriendUIObj,"bwJ","18627092373");

                }catch (Exception e) {
                    XposedBridge.log(TAG +" error "+e.toString());
                    return;
                }


            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 15000);

    }
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (("com.tencent.mm").equals(loadPackageParam.packageName)) {

            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    classLoader = ((Context) param.args[0]).getClassLoader();
                    Class<?> LauncherUI = null;
                    Class<?> storageClazz = null;
                    Class<?> AddressUI = null;
                    Class<?> ajClazz = null;
                    try {
                        LauncherUI = classLoader.loadClass("com.tencent.mm.ui.LauncherUI");
                        storageClazz = classLoader.loadClass("com.tencent.mm.storage.aj");
                        AddressUI = classLoader.loadClass("com.tencent.mm.ui.contact.AddressUI");
                        ajClazz = classLoader.loadClass("com.tencent.mm.storage.aj");
                        XposedBridge.log(TAG + " "+LauncherUI.toString());
                    } catch (Exception e) {
                        XposedBridge.log(TAG + "错误"+e);
                        return;
                    }

                    XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.ui.LauncherUI.onCreate");
//                            callLoop();

                        }
                    });




                    XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.ab", classLoader, "d", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
//                            XposedBridge.log(TAG + " d "+param.args[0].toString() + "  "+param.args[1].toString());
//                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.sdk.platformtools.ab d");

                        }
                    });

                    XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.ab", classLoader, "i", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
//                            XposedBridge.log(TAG + " i "+param.args[0].toString() + "  "+param.args[1].toString());
//                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.sdk.platformtools.ab i");

                        }
                    });

                    XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.ab", classLoader, "f", String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

//                            XposedBridge.log(TAG + " f "+param.args[0].toString() + "  "+param.args[1].toString());
//
//                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.sdk.platformtools.ab f");

                        }
                    });



                    XposedBridge.hookAllMethods(ajClazz, "a",  new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            XposedBridge.log(TAG + " com.tencent.mm.storage.aj "+ param.args.length);

                            if(param.args.length!=6)
                                return;

                            List<JSONObject> userList = new ArrayList<JSONObject>() ;
                            Cursor cursor = (Cursor)param.getResult();
                            String[] fileds =new String[]{"username","nickname","alias","conRemark","verifyFlag","showHead","weiboFlag","rowid","deleteFlag","lvbuff","descWordingId","openImAppid"};
                            while(cursor.moveToNext())
                            {
                                JSONObject jb = new JSONObject();
                                for (String filed : fileds)
                                {
                                    try {
                                        XposedBridge.log(TAG + " com.tencent.mm.storage.aj filed "+filed);
                                        jb.put(filed,cursor.getString(cursor.getColumnIndex(filed)));
                                    }catch (Exception e)
                                    {
                                        XposedBridge.log(TAG + " error "+e.toString());
                                    }

                                }
                                XposedBridge.log(TAG + " com.tencent.mm.storage.aj jb"+jb.toString());
                                userList.add(jb);

//                                int nameColumnIndex = cursor.getColumnIndex("username");
//                                String strValue=cursor.getString(nameColumnIndex);
//                                XposedBridge.log(TAG + " XP已检测到 strValue:"+strValue);
//
//                                nameColumnIndex = cursor.getColumnIndex("nickname");
//                                strValue=cursor.getString(nameColumnIndex);
//                                XposedBridge.log(TAG + " XP已检测到 strValue:"+strValue);
                            }
                            XposedBridge.log(TAG + " com.tencent.mm.storage.aj userList"+userList.toString());
                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.storage.aj.a");

                        }
                    });



//                    XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI", classLoader, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            super.afterHookedMethod(param);
//
//                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.ui.LauncherUI.onCreateOptionsMenu");
//
////                            Object m = (Object)param.args[0];
////
////                            Field[] fs=m.getClass().getDeclaredFields();//拿到数据成员
////
////                            for(Field f : fs){
////                                f.setAccessible(true);
////                                XposedBridge.log(TAG + "该类的内部变量有:"+f.getName() + ":"+f.get(m));
////                            }
////
////                            Method[] methods=m.getClass().getDeclaredMethods();//拿到函数成员
////                            int c=0;
////                            for (int i = 0; i < methods.length; i++) {
////                                c++;
////                                Class returntype=methods[i].getReturnType();//获取该类的返回值
////                                XposedBridge.log(TAG + "方法名:"+ methods[i].getName() + " 返回值:"+returntype.getSimpleName()+" ");//打印返回值
////                                Class[] prams=methods[i].getParameterTypes();//获取参数的方法
////                                for (int j = 0; j < prams.length; j++) {
////                                    XposedBridge.log(TAG + "参数名:" + prams[j].getSimpleName()+"  类型:"+ prams[j].toString());//打印参数名
////                                }
////                            }
////
////                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.ui.LauncherUI.onCreateOptionsMenu"+m.toString());
////
////                            Object xwg = XposedHelpers.findField(param.thisObject.getClass(), "xwg");
////                            Field[] fs1 = xwg.getClass().getDeclaredFields();//拿到数据成员
////
////                            for(Field f : fs1){
////                                f.setAccessible(true);
////                                XposedBridge.log(TAG + "xwg 的内部变量有:"+f.getName() + ":"+f.get(m));
////                            }
//
//                        }
//                    });

                    //添加好友
                    Class<?> fClazz = null;
                    Class<?> perClazz = null;
                    try {
                        fClazz = classLoader.loadClass("com.tencent.mm.ui.base.preference.f");
                        perClazz = classLoader.loadClass("com.tencent.mm.ui.base.preference.Preference");
                    } catch (Exception e) {
                        XposedBridge.log(TAG + "错误"+e);
                        return;
                    }


                    XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
//                            Field mePFiled =  XposedHelpers.findField(param.thisObject.getClass(),"meP");

                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.plugin.fts.ui.FTSAddFriendUI " );

                                FTSAddFriendUIObject = param.thisObject;
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {

                                        try {
//                                            XposedHelpers.setObjectField(FTSAddFriendUIObject,"meP","18627092373");
//                                            XposedHelpers.callMethod(FTSAddFriendUIObject,"bwJ");

//                                            XposedHelpers.setObjectField(param.thisObject,"bWm","18627092373");
//                                            XposedHelpers.callMethod(param.thisObject,"CM","18627092373");
                                        }catch (Exception e) {
                                            XposedBridge.log(TAG +" error "+e.toString());
                                            return;
                                        }


                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task, 3000);

                        }
                    });


                    XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI", classLoader, "bwJ",  new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod( MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
//                            Field mePFiled =  XposedHelpers.findField(param.thisObject.getClass(),"meP");

                            XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.plugin.fts.ui.FTSAddFriendUI bwJ" );
                            FTSAddFriendUIObject = param.thisObject;
                            Object m = FTSAddFriendUIObject.getClass().getDeclaredField("lmO");

                            XposedBridge.log(TAG + "该类的内部变量有:"+ m.toString());

                            Field[] fs=m.getClass().getDeclaredFields();

//                            XposedBridge.log(TAG + " vrl "+ m.getClass().getDeclaredField("vrl"));
                            for(Field f : fs){
                                f.setAccessible(true);
                                XposedBridge.log(TAG + "该类的内部变量有:"+f.getName() + ":"+f.get(m));
                            }



                        }
                    });

                    XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.fts.ui.FTSAddFriendUI$5",classLoader,"a",int .class, int .class,String.class,XposedHelpers.findClass("com.tencent.mm.ab.l",classLoader) ,
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    super.beforeHookedMethod(param);

                                    XposedBridge.log(TAG + "FTSAddFriendUI$5--0="+param.args[0]+"=1="+param.args[1]+ "=2="+param.args[2]);
                                }

                            });

                    XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.profile.ui.ContactInfoUI", classLoader, "onCreate",Bundle.class, new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                    super.afterHookedMethod(param);

                                    Intent intent = (Intent) XposedHelpers.callMethod(param.thisObject,"getIntent");

                                    Bundle bundle = intent.getExtras();
                                    Set<String> keySet = bundle.keySet();
                                    for (String key : keySet) {
                                        XposedBridge.log(TAG + "intent extras ::"+ key +" : "+ bundle.get(key));
                                    }

                                    XposedBridge.log(TAG + " XP已检测到 com.tencent.mm.plugin.profile.ui.ContactInfoU  intent" +intent.toString() );


                                }
                            });



                    }
            });


        }
    }

}
