package com.hwyt.xpwx.Bean;


import com.xuhao.didi.core.iocore.interfaces.ISendable;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestSendData implements ISendable {
    private String str = "";

    public TestSendData(JSONObject jsonObject) {

        try {
            str = new String(jsonObject.toString().getBytes(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        byte[] body = str.getBytes();
        ByteBuffer bb = ByteBuffer.allocate(4 +  body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}