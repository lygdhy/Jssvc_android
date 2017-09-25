package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 广告bean
 */

public class AdsBean implements Serializable {
  private String id;
  private String theme;
  private String category;
  private String banner;
  private String extra1;
  private String extra2;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getBanner() {
    return banner;
  }

  public void setBanner(String banner) {
    this.banner = banner;
  }

  public String getExtra1() {
    return extra1;
  }

  public void setExtra1(String extra1) {
    this.extra1 = extra1;
  }

  public String getExtra2() {
    return extra2;
  }

  public void setExtra2(String extra2) {
    this.extra2 = extra2;
  }

  public AdsBean(String theme, String category, String banner) {
    this.theme = theme;
    this.category = category;
    this.banner = banner;
  }
}
