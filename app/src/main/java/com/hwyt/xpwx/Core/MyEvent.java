package com.hwyt.xpwx.Core;

/**
 * Created by yuanshenghong on 2017/3/18.
 * 事件类，所有事件在这里注册
 */

public class MyEvent {
    private EventTag tag;

    private Object object;

    public EventTag getTag() {
        return tag;
    }

    public void setTag(EventTag tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public MyEvent(EventTag tag) {
        this.tag = tag;
    }

    public MyEvent(EventTag tag, Object object) {
        this.tag = tag;
        this.object = object;
    }
}
