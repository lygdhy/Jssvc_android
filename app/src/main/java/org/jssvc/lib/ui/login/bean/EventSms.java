package org.jssvc.lib.ui.login.bean;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/12/19
 *     desc   : EventBus短信验证
 *     version: 1.0
 * </pre>
 */
public class EventSms {
  private String opt;
  private String opt_value;

  public String getOpt() {
    return opt;
  }

  public void setOpt(String opt) {
    this.opt = opt;
  }

  public String getOpt_value() {
    return opt_value;
  }

  public void setOpt_value(String opt_value) {
    this.opt_value = opt_value;
  }

  public EventSms(String opt, String opt_value) {
    this.opt = opt;
    this.opt_value = opt_value;
  }
}
