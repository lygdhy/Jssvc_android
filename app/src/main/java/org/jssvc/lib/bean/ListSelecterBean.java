package org.jssvc.lib.bean;

/**
 * Created by lygdh on 2016/12/29.
 */

public class ListSelecterBean {
  private int icon;
  private String id;
  private String title;
  private String subtitle;

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ListSelecterBean(int icon, String id, String title, String subtitle) {
    this.icon = icon;
    this.id = id;
    this.title = title;
    this.subtitle = subtitle;
  }
}
