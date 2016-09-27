package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 频道菜单
 * Created by lygdh on 2016/9/26.
 */

public class FindMenuBean implements Serializable {
    int channelId;
    String channelName;

    public FindMenuBean(int channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
