package com.hwyt.xpwx.Dao.Wx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.hwyt.xpwx.App;
import com.hwyt.xpwx.Bean.TestSendData;
import com.hwyt.xpwx.Utils.MobileUtils;
import com.hwyt.xpwx.Utils.common.MessageUtil;
import com.hwyt.xpwx.log.LogUtils;
import com.xuhao.didi.socket.client.sdk.OkSocket;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Sns {

    private final App app;

    private Timer timer = new Timer();

    private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/Wx/images";//保存的确切位置

    private  int index = 0;
    private  String filePath = "";

    private  JSONObject jsonObject = null;

    public  Sns(App app)
    {
        this.app = app;
    }

    public void snsUpload(JSONObject jb)
    {
        try{

            this.jsonObject = jb;

            String[] pics = jb.get("sns_pics").toString().split(",");

            final int picLength = pics.length;

            index = 0;


            for (String pic : pics) {
                String url = pic;
                String fileName = pic.substring(url.lastIndexOf("/") + 1);

                if(!filePath.equals(""))
                    filePath+=",";

                filePath += SAVE_REAL_PATH+"/"+fileName;

                AndroidNetworking.download(url, SAVE_REAL_PATH, fileName)
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                // do anything with progress
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                index++;
                                if(index==picLength)
                                {
                                    try {
                                        jsonObject.put("sns_pics",filePath);

                                        MessageUtil.saveJbArticle(jsonObject.toString(),"Wx","message");
                                    }catch (Exception e)
                                    {}
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
            }



            TimerTask task = new TimerTask() {

                private  int n = 0;
                @Override
                public void run() {
                    if(n>10)
                    {
                        timer.cancel();
                        return;
                    }
                    n++;
                    try {
                        JSONObject jsonObject = MessageUtil.getJbArticle("Wx/snsUploadResultOut");
                        if(jsonObject!=null) {
                            MessageUtil.saveJbArticle("","Wx","snsUploadResultOut");

                            jsonObject.put("fun", "client");
                            jsonObject.put("model", "Wx");
                            jsonObject.put("controller", "Sns");
                            jsonObject.put("action", "snsUploadResult");
                            jsonObject.put("result", jsonObject.get("result").toString());
                            jsonObject.put("ws_fd", app.getWSFd());
                            jsonObject.put("m_fd", app.getFd());
                            jsonObject.put("m_id", app.getMID());
                            jsonObject.put("mer_id", app.getMerID());
                            jsonObject.put("mobile_serila", MobileUtils.getMoblieIMEI(app.getContext()));
                            OkSocket.open(app.getSocketinfo()).send(new TestSendData(jsonObject));
                            n=11;
                        }


                    }catch (Exception e) {
                        LogUtils.i("uploadContact" + e.toString());
                        return;
                    }
                }
            };
            timer.schedule(task, 3000,3000);

        }catch (Exception e)
        {
            LogUtils.i("findContact "+e.toString());
        }
    }



//    下面就是保存的方法，传入参数就可以了：

    public  void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = SAVE_REAL_PATH + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        File file = new File(path + fileName);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        app.getContext().sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }

}
