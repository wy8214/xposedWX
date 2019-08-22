package com.hwyt.xpwx.Core;

import java.io.Serializable;

/**
 * Created by yuanshenghong on 2017/3/18.
 * <p>
 * 为了避免重复，事件名称可以随意定义，但是值根据开发者不同而指定不同的区间避免重复
 */

public enum EventTag implements Serializable {
    NoEvent(1),
    sim_changed(2),
    ContectSuccess(110),
    socket_disconnet(111),
    EventSomething(2);

    private int value;

    private EventTag(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventTag{" +
                "value=" + value +
                '}';
    }
}
