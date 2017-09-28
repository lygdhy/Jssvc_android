package org.jssvc.lib.bean;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LabMenuBean {
  private int id;
  private int icon;
  private String title;
  private String desc;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

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

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
