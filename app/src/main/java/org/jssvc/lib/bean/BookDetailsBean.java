package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 图书详情，未知的键值对
 */

public class BookDetailsBean implements Serializable {
  private String key;
  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
