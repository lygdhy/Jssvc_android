package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ArticleListBean implements Serializable {
  private String id;
  private String category;
  private String banner;
  private String title;
  private String author;
  private String platform;
  private String addtime;
  private String original;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getAddtime() {
    return addtime;
  }

  public void setAddtime(String addtime) {
    this.addtime = addtime;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }
}
