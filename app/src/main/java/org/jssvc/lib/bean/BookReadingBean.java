package org.jssvc.lib.bean;

import java.io.Serializable;

/**
 * 当前借阅
 */

public class BookReadingBean implements Serializable {
    private String bookName;//书名
    private String bookAuthor;//作者
    private String barCode;//条码号
    private String marcNo;//机读号
    private String borrowDate;//借阅时间
    private String returnDate;//应还时间
    private String times;//续借次数
    private String place;//馆藏地点
    private String detialUrl;//详情url

    public String getDetialUrl() {
        return detialUrl;
    }

    public void setDetialUrl(String detialUrl) {
        this.detialUrl = detialUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getMarcNo() {
        return marcNo;
    }

    public void setMarcNo(String marcNo) {
        this.marcNo = marcNo;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
