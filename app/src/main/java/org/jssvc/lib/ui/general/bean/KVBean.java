package org.jssvc.lib.ui.general.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/12/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class KVBean implements Serializable {
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

  public KVBean(String key, String value) {
    this.key = key;
    this.value = value;
  }
}
