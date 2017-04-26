package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 我的书架
 */

public class BookShelfBean implements Serializable {
  private String id;
  private String url;
  private String name;
  private String count;
  private String date;
  private String remark;
  private String deleteurl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDeleteurl() {
    return deleteurl;
  }

  public void setDeleteurl(String deleteurl) {
    this.deleteurl = deleteurl;
  }
}
