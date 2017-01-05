package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * Created by lygdh on 2017/1/5.
 */

public class MsgBean implements Serializable {
    private String id;
    private String title;
    private String avatar;
    private String author;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MsgBean(String id, String title, String avatar, String author, String date) {
        this.id = id;
        this.title = title;
        this.avatar = avatar;
        this.author = author;
        this.date = date;
    }
}
