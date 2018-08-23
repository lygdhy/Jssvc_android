package org.jssvc.lib.ui.general.bean;

/**
 * 软件版本bean
 */

public class VersionBean {
  private String title;
  private String version_code;
  private String version_name;
  private String version_log;
  private String force_version_code;//低于该版本将强制升级
  private String download_link;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVersion_code() {
    return version_code;
  }

  public void setVersion_code(String version_code) {
    this.version_code = version_code;
  }

  public String getVersion_name() {
    return version_name;
  }

  public void setVersion_name(String version_name) {
    this.version_name = version_name;
  }

  public String getVersion_log() {
    return version_log;
  }

  public void setVersion_log(String version_log) {
    this.version_log = version_log;
  }

  public String getForce_version_code() {
    return force_version_code;
  }

  public void setForce_version_code(String force_version_code) {
    this.force_version_code = force_version_code;
  }

  public String getDownload_link() {
    return download_link;
  }

  public void setDownload_link(String download_link) {
    this.download_link = download_link;
  }
}
