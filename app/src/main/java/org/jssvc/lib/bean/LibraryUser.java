package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 图书馆用户信息
 * Created by lygdh on 2016/9/30.
 */

public class LibraryUser implements Serializable {
  private String userid;//学号
  private String username;//姓名
  private String sex;//性别
  private String type;//读者类型
  private String readType;//借阅类型
  private String department;//工作单位
  private String cardStartDate;//卡生效时间
  private String cardEndDate;//卡失效时间
  private String deposit;//押金
  private String poundage;//手续费
  private String readTimes;//累计借书
  private String violation;//违章状态
  private String debt;//欠款状态

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getReadType() {
    return readType;
  }

  public void setReadType(String readType) {
    this.readType = readType;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getCardStartDate() {
    return cardStartDate;
  }

  public void setCardStartDate(String cardStartDate) {
    this.cardStartDate = cardStartDate;
  }

  public String getCardEndDate() {
    return cardEndDate;
  }

  public void setCardEndDate(String cardEndDate) {
    this.cardEndDate = cardEndDate;
  }

  public String getDeposit() {
    return deposit;
  }

  public void setDeposit(String deposit) {
    this.deposit = deposit;
  }

  public String getPoundage() {
    return poundage;
  }

  public void setPoundage(String poundage) {
    this.poundage = poundage;
  }

  public String getReadTimes() {
    return readTimes;
  }

  public void setReadTimes(String readTimes) {
    this.readTimes = readTimes;
  }

  public String getViolation() {
    return violation;
  }

  public void setViolation(String violation) {
    this.violation = violation;
  }

  public String getDebt() {
    return debt;
  }

  public void setDebt(String debt) {
    this.debt = debt;
  }
}
