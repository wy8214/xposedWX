package com.hwyt.xpwx.Utils.common;




import com.hwyt.xpwx.Utils.logger.MyLog;

import java.io.UnsupportedEncodingException;


/**
 * Created by yuanshenghong on 16/5/3.
 */
public class EncodeingUtil {
    public static String getString(byte[] data, int offset, int length, String charset) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        } else if (charset != null && charset.length() != 0) {
            try {
                return new String(data, offset, length, charset);
            } catch (UnsupportedEncodingException var5) {
                MyLog.w("Unsupported encoding: " + charset + ". System encoding used");

                return new String(data, offset, length);
            }
        } else {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
    }

    public static String getString(byte[] data, String charset) {
        try {
            return getString(data, 0, data.length, charset);
        }catch (IllegalArgumentException e){
            return "empty";
        }
    }
}
