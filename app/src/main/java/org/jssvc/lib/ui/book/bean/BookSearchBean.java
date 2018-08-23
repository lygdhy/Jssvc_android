package org.jssvc.lib.ui.book.bean;

import java.io.Serializable;

/**
 * Created by lygdh on 2016/11/16.
 */

public class BookSearchBean implements Serializable {
  private String No;//no
  private String Title;//书名
  private String Type;// 图书类型
  private String Code;// 编码
  private String Author;// 作者
  private String Publisher;// 出版社&出版年份
  private String Copy_Total;// 馆藏总数
  private String Copy_Remain;// 可借副本
  private String DetialUrl;// 图书详情链接

  public String getDetialUrl() {
    return DetialUrl;
  }

  public void setDetialUrl(String detialUrl) {
    DetialUrl = detialUrl;
  }

  public String getNo() {
    return No;
  }

  public void setNo(String no) {
    No = no;
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String title) {
    Title = title;
  }

  public String getType() {
    return Type;
  }

  public void setType(String type) {
    Type = type;
  }

  public String getCode() {
    return Code;
  }

  public void setCode(String code) {
    Code = code;
  }

  public String getAuthor() {
    return Author;
  }

  public void setAuthor(String author) {
    Author = author;
  }

  public String getPublisher() {
    return Publisher;
  }

  public void setPublisher(String publisher) {
    Publisher = publisher;
  }

  public String getCopy_Total() {
    return Copy_Total;
  }

  public void setCopy_Total(String copy_Total) {
    Copy_Total = copy_Total;
  }

  public String getCopy_Remain() {
    return Copy_Remain;
  }

  public void setCopy_Remain(String copy_Remain) {
    Copy_Remain = copy_Remain;
  }
}
