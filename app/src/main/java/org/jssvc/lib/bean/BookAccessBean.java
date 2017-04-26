package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * Created by lygdh on 2016/11/17.
 */

public class BookAccessBean implements Serializable {
  private String tpCode;//索书号
  private String barCode;//条码号
  private String cell;//年卷期
  private String area;//校区
  private String place;//馆藏地
  private String state;//书刊状态

  public String getTpCode() {
    return tpCode;
  }

  public void setTpCode(String tpCode) {
    this.tpCode = tpCode;
  }

  public String getBarCode() {
    return barCode;
  }

  public void setBarCode(String barCode) {
    this.barCode = barCode;
  }

  public String getCell() {
    return cell;
  }

  public void setCell(String cell) {
    this.cell = cell;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
