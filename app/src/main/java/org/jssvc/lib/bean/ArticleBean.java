package org.jssvc.lib.bean;

/**
 * Created by lygdh on 2016/9/26.
 */

public class ArticleBean {
    private String id;
    private String type;
    private String pic;
    private String title;
    private String body;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArticleBean(String id, String type, String pic, String title, String body, String author, String date) {
        this.id = id;
        this.type = type;
        this.pic = pic;
        this.title = title;
        this.body = body;
        this.author = author;
        this.date = date;
    }
}
