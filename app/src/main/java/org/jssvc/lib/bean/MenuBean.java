package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MenuBean implements Serializable {
  private int type; // 0链接类  !0 其他类别(参考org.jssvc.lib.data.Constants)
  private String title;
  private String web_url;
  private String icon_url;
  private int resourceId;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIcon_url() {
    return icon_url;
  }

  public void setIcon_url(String icon_url) {
    this.icon_url = icon_url;
  }

  public String getWeb_url() {
    return web_url;
  }

  public void setWeb_url(String web_url) {
    this.web_url = web_url;
  }

  public int getResourceId() {
    return resourceId;
  }

  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }

  public MenuBean(int type, String title, int resourceId) {
    this.type = type;
    this.title = title;
    this.resourceId = resourceId;
  }
}
