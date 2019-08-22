package com.hwyt.xpwx.Wx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class HookMessage {

    private  static Intent intent ;

    private static String GalleryUI_ToUser = "";
    private static String GalleryUI_FromUser = "";

    private static ArrayList<String> CropImage_OutputPath_List;

    public static void init(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap)
    {
        hookMessage(mlpparam);

        hookSendImgMessage(mlpparam,wxObjectMap);
    }

    public static  void sendMessage(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap, JSONObject jb)
    {

        boolean result = false;
        try {
            ClassLoader classLoader = mlpparam.classLoader;
            String receiveWxId = jb.get("r_wxid").toString();
            String msg = jb.get("content").toString();
            //通过公开静态无参方法DF获取我们发图的a方法
            Class<?> au = XposedHelpers.findClassIfExists("com.tencent.mm.model.av", classLoader);
            Method DF = au.getMethod("Pw");
            Object o = DF.invoke(null);
            //得到第一个参数
            Class<?> i = XposedHelpers.findClassIfExists("com.tencent.mm.modelmulti.h", classLoader);
            Constructor<?> constructor = i.getConstructor(String.class, String.class, int.class, int.class, Object.class);
            Object iVar = constructor.newInstance(receiveWxId, msg, 1, 0, null);
            result = (boolean) XposedHelpers.callMethod(o, "a", iVar, 0);

            jb.put("result",result+"");

            MessageUtil.saveJbArticle(jb.toString(),"Wx","sendMessageOut");
            LogUtils.i( "发送文字消息返回值：" + result);
        } catch (Exception e) {
            LogUtils.i( e.toString());
        }
    }

    public static  void sendImgMessage(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap, JSONObject jb)
    {
        try {
            GalleryUI_ToUser    = jb.get("to_user").toString();
            GalleryUI_FromUser  = jb.get("from_user").toString();

            CropImage_OutputPath_List = new ArrayList<>();

            String[] pics = jb.get("imgs").toString().split(",");

            for (String s : pics)
            {
                CropImage_OutputPath_List.add(s);
            }
            MobileUtils.execShellCmd("am start -n com.tencent.mm/com.tencent.mm.ui.chatting.SendImgProxyUI");

        }catch (Exception e)
        {
            LogUtils.i("sendImgMessage   error " + e.toString());
        }
    }

    public static void hookSendImgMessage(final XC_LoadPackage.LoadPackageParam mlpparam, final Map<String,Object> wxObjectMap)
    {
        try {
            final Class<?> class2 = mlpparam.classLoader.loadClass("com.tencent.mm.ui.chatting.SendImgProxyUI");
            final Class<?> class1 = mlpparam.classLoader.loadClass("com.tencent.mm.ui.chatting.c.ah");

            XposedBridge.hookAllConstructors(class1,new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    wxObjectMap.put("com.tencent.mm.ui.chatting.c.ah",param.thisObject);
                }
            });

            XposedBridge.hookAllMethods(class2, "onCreate",
                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            LogUtils.i("com.tencent.mm.ui.chatting.SendImgProxyUI paramStr1 " + param.args[0]);

                            intent = (Intent) XposedHelpers.callMethod(param.thisObject,"getIntent");

                            TimerTask task1 = new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        Bundle bundle = intent.getExtras();

                                        ArrayList<Integer> GalleryUI_ImgIdList = new ArrayList<>();
                                        GalleryUI_ImgIdList.add(-1);

                                        bundle.putIntegerArrayList("GalleryUI_ImgIdList",GalleryUI_ImgIdList);

                                        bundle.putBoolean("GalleryUI_IsSendImgBackground",true);

                                        bundle.putStringArrayList("key_select_video_list",new ArrayList<String>());
                                        bundle.putInt("CropImage_limit_Img_Size",26214400);
                                        bundle.putString("GalleryUI_FromUser",GalleryUI_FromUser);
                                        bundle.putBoolean("CropImage_Compress_Img",true);
                                        bundle.putString("GalleryUI_ToUser",GalleryUI_ToUser);

                                        bundle.putStringArrayList("CropImage_OutputPath_List",CropImage_OutputPath_List);

                                        intent.putExtras(bundle);

                                        Object object1 = wxObjectMap.get("com.tencent.mm.ui.chatting.c.ah");
                                        XposedHelpers.callMethod(object1,"a",object1,intent,GalleryUI_ToUser);

                                        JSONObject jb = new JSONObject();
                                        jb.put("result",true);

                                        MessageUtil.saveJbArticle(jb.toString(),"Wx","sendImgMessageResultOut");

                                    } catch (Exception e) {
                                        LogUtils.i("com.tencent.mm.ui.chatting.SendImgProxyUI error " + e.toString());
                                        return;
                                    }
                                }
                            };
                            Timer timer1 = new Timer();
                            timer1.schedule(task1, 3000);
                        }

                    });

        }catch (Exception e)
        {
            LogUtils.i("HookMessage send img  error " + e.toString());
        }
    }

    public static void hookMessage(final XC_LoadPackage.LoadPackageParam mlpparam) {

        final Class<?> afClass = XposedHelpers.findClass("com.tencent.mm.sdk.platformtools.af", mlpparam.classLoader);
        //获取收到消息的标记通知栏
        Class<?> b$1Class = XposedHelpers.findClass("com.tencent.mm.booter.notification.b$1", mlpparam.classLoader);
        XposedHelpers.findAndHookMethod(b$1Class, "handleMessage", Message.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Message message = (Message) param.args[0];
                String recvieWxid = message.getData().getString("notification.show.talker");
                String content = message.getData().getString("notification.show.message.content");
                int i = message.getData().getInt("notification.show.message.type");
                int i2 = message.getData().getInt("notification.show.tipsflag");
                LogUtils.i(recvieWxid, content, i, i2, afClass);


                JSONObject jb = new JSONObject();
                jb.put("r_wxid",recvieWxid);
                jb.put("content",content);

                MessageUtil.saveJbArticle(jb.toString(),"Wx","reciveMessageOut");
                LogUtils.i("send ok");
            }
        });

    }

}
