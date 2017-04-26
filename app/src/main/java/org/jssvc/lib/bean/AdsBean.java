package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 广告bean
 */

public class AdsBean implements Serializable {
  private String uid;// 广告ID
  private String type;// 广告类型
  private String title;// 广告标题
  private String pic;// 广告图片地址链接
  private String url;// 跳转页面地址

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public AdsBean(String uid, String type, String title, String pic, String url) {
    this.uid = uid;
    this.type = type;
    this.title = title;
    this.pic = pic;
    this.url = url;
  }
}
