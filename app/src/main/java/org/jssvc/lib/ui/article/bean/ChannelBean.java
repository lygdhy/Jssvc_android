package org.jssvc.lib.ui.article.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/22
 *     desc   : 频道编号
 *     version: 1.0
 * </pre>
 */
public class ChannelBean implements Serializable {
  private String id;
  private String platform;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }
}
