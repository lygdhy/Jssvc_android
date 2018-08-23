package org.jssvc.lib.ui.article.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/09/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ArticleDetailBean implements Serializable {
  private String id;
  private String platform;
  private String title;
  private String content;
  private String author;
  private String original;
  private String addtime;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getAddtime() {
    return addtime;
  }

  public void setAddtime(String addtime) {
    this.addtime = addtime;
  }
}
