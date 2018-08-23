package org.jssvc.lib.ui.account.bean;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/09/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ThirdAccountBean {
  private String platform;// 平台 1图书馆 2教务
  private String type;
  private String account;
  private String pwd;

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }
}
