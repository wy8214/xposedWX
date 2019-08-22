package com.hwyt.xpwx.Wx;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.EditText;

import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;


import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findField;

public class HookSns {

    private static  String[] pics = null;
    private static String snsContent = "";

    public static void snsUpload(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap) {

             pics = wxObjectMap.get("sns_pics").toString().split(",");
            snsContent = wxObjectMap.get("sns_content").toString();

            MobileUtils.execShellCmd("am start -n com.tencent.mm/com.tencent.mm.plugin.sns.ui.SnsUploadUI --ei Ksnsupload_type 0 --ei sns_comment_type 1 --ez KSnsPostManu true");

    }


    public static void init(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap) {

        try {
            final Class<?> SnsUploadUIClass = mlpparam.classLoader.loadClass("com.tencent.mm.plugin.sns.ui.SnsUploadUI");
            XposedHelpers.findAndHookMethod(SnsUploadUIClass, "onCreate", Bundle.class,
                    new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            Object o1 = param.thisObject;
                            Object nRk=XposedHelpers.findField(SnsUploadUIClass, "qHY").get(o1);
                            EditText editText = (EditText) nRk;
                            editText.setText(snsContent);

                            Field[] fs1 = o1.getClass().getDeclaredFields();

                            String paramStr1 = "";

                            for (Field f1 : fs1) {
                                f1.setAccessible(true);
                                paramStr1 += " ||| " + f1.getName() + "    " + f1.get(o1);
                            }
                            LogUtils.i( "com.tencent.mm.plugin.sns.ui.SnsUploadUI paramStr1 " +  paramStr1 );

                        }
                    });


            XposedHelpers.findAndHookMethod("com.tencent.mm.ui.q",mlpparam.classLoader,"onCreateOptionsMenu", Menu.class,new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    String SimpleName = XposedHelpers.findField( mlpparam.classLoader.loadClass("com.tencent.mm.ui.q"), "xyi").get(param.thisObject).getClass().getSimpleName();
                    LogUtils.i("com.tencent.mm.plugin.sns.ui.SnsUploadUI  SimpleName " + SimpleName);
                    if (TextUtils.equals(SimpleName, "SnsUploadUI") ) {

                        final Object menuItem = ((Menu) param.args[0]).getItem(0);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Object c2171a = ((LinkedList) findField( mlpparam.classLoader.loadClass("com.tencent.mm.ui.q"), "xrG").get(param.thisObject)).get(0);
                                    Object menuItemClickListener = findField(XposedHelpers.findClass("com.tencent.mm.ui.q$a", mlpparam.classLoader), "gkj").get(c2171a);
                                    XposedHelpers.callMethod(menuItemClickListener, "onMenuItemClick", menuItem);

                                    JSONObject jb = new JSONObject();
                                    jb.put("result",true);

                                    MessageUtil.saveJbArticle(jb.toString(),"Wx","snsUploadResultOut");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 1500);
                    }
                }
            });

            final Class<?> class1 = mlpparam.classLoader.loadClass("com.tencent.mm.plugin.sns.ui.ag");
            XposedBridge.hookAllMethods(class1, "a",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            LogUtils.i( "com.tencent.mm.plugin.sns.ui.ag.a paramStr1 " +  param.args.length );

                            if(param.args.length==13)
                                return;

                            Object o = param.thisObject;

                            Field[] fs1 = o.getClass().getDeclaredFields();

                            String paramStr1 = "";

                            for (Field f1 : fs1) {
                                f1.setAccessible(true);
                                paramStr1 += " ||| " + f1.getName() + "    " + f1.get(o);

                                if(f1.getName().equals("qGM"))
                                {
                                    Field[] fs = f1.get(o).getClass().getDeclaredFields();
                                    String paramStr = "";
                                    for (Field f : fs) {
                                        f.setAccessible(true);
                                        paramStr += " ||| " + f.getName() + "    " + f.get(f1.get(o));
                                    }

                                    Object ag = (Object) f1.get(o);

                                    for (String pic : pics)
                                    {
                                        XposedHelpers.callMethod(ag,"p",pic,0,false);
                                    }

//                                    XposedHelpers.callMethod(ag,"p","/storage/emulated/0/Wx/images/11.png",0,false);
//                                    XposedHelpers.callMethod(ag,"p","/storage/emulated/0/Wx/images/22.png",0,false);
//                                    XposedHelpers.callMethod(ag,"p","/storage/emulated/0/Wx/images/333.jpg",0,false);

                                    LogUtils.i( "com.tencent.mm.plugin.sns.ui.ag.a paramStr1 " +  paramStr );
                                }

                            }
                            LogUtils.i( "com.tencent.mm.plugin.sns.ui.ag.a paramStr1 " +  paramStr1 );

                            for (int i=0;i<param.args.length;i++){
                                LogUtils.i( "com.tencent.mm.plugin.sns.ui.ag.a  paramStr1 " + i + "  " +  param.args[i] );
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
