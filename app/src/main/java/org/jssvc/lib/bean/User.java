package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 图书馆用户信息
 * Created by lygdh on 2016/9/30.
 */

public class User implements Serializable {
    private String userid;
    private String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
