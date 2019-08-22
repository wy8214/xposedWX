package com.hwyt.xpwx.Bean;


import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class PulseData implements IPulseSendable {
    private String str = "pulse";

    @Override
    public byte[] parse() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fun", "client");
            jsonObject.put("model", "Wx");
            jsonObject.put("controller", "Index");
            jsonObject.put("action", "client_pulse");
            str = new String(jsonObject.toString().getBytes(), "UTF-8");

        }catch (Exception e)
        {}

        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();

    }
}

