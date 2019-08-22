package com.hwyt.xpwx.Utils.common;

import android.graphics.Bitmap;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import de.robv.android.xposed.XposedHelpers;

public class MessageUtil {

    public static   void  deleteFile(String dir,String fileName)
    {
        File file = new File( Environment.getExternalStorageDirectory(), dir+"/"+fileName);
        file.delete();
    }

    public static   void  saveImage(String dir,String fileName,Bitmap bitmap)
    {
        try {

            File dFile = new File(Environment.getExternalStorageDirectory(),  dir );
            if(!dFile.exists())
            {
                dFile.mkdir();
            }
            File file = new File( Environment.getExternalStorageDirectory(), dir+"/"+fileName+".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        }catch(Exception e){
            //TODOAuto-generatedcatchblock
            e.printStackTrace();
        }

    }

    public static   void  saveJbArticle(String jb,String dir,String fileName)
    {
        FileOutputStream fos=null;
        try{

            File dFile = new File(Environment.getExternalStorageDirectory(),  dir );
            if(!dFile.exists())
            {
                dFile.mkdir();
            }

            File mFile = new File(Environment.getExternalStorageDirectory(),  dir+"/"+fileName+".txt" );
            if(!mFile.exists())
            {
                mFile.createNewFile();
            }
            fos=new FileOutputStream(mFile);
            fos.write(jb.getBytes());
        }catch(Exception e){
            //TODOAuto-generatedcatchblock
            e.printStackTrace();
        }finally{
            try{
                if(fos!=null) {
                    fos.close();
                }
            }catch(IOException e){
                //TODOAuto-generatedcatchblock
                e.printStackTrace();
            }
        }

    }

    public static JSONObject getJbArticle(String fileName)
    {
        FileInputStream fis=null;
        String s = "";

        JSONObject jb = null;
        try{
            File mFile = new File(Environment.getExternalStorageDirectory(),  fileName+".txt" );
            if(!mFile.exists())
            {
                return null;
            }
            fis =  new FileInputStream(mFile);
            byte[] bytes= new byte[1024];
            //得到实际读取的长度
            int n=0;
            //循环读取
            while((n=fis.read(bytes))!=-1){
                s += new String(bytes,0,n);
            }
            jb = s.equals("")? null:new JSONObject(s);
        }catch(Exception e){
            //TODO Auto-generatedcatchblock
            e.printStackTrace();
        }finally{
            try{
                if(fis!=null) {
                    fis.close();
                }

            }catch(IOException e){
                //TODO Auto-generatedcatchblock
                e.printStackTrace();
            }
        }
        return  jb;

    }

    public static JSONArray getJAArticle(String fileName)
    {
        FileInputStream fis=null;
        String s = "";

        JSONArray jb = null;
        try{
            File mFile = new File(Environment.getExternalStorageDirectory(),  fileName+".txt" );
            if(!mFile.exists())
            {
                return null;
            }
            fis =  new FileInputStream(mFile);
            byte[] bytes= new byte[1024];
            //得到实际读取的长度
            int n=0;
            //循环读取
            while((n=fis.read(bytes))!=-1){
                s += new String(bytes,0,n);
            }


            jb = s.equals("")? null:new JSONArray(s);
        }catch(Exception e){
            //TODO Auto-generatedcatchblock
            e.printStackTrace();
        }finally{
            try{
                if(fis!=null) {
                    fis.close();
                }

            }catch(IOException e){
                //TODO Auto-generatedcatchblock
                e.printStackTrace();
            }
        }
        return  jb;

    }



}
