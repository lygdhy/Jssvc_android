package org.jssvc.lib.utils;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jssvc.lib.ui.book.bean.BookAccessBean;
import org.jssvc.lib.ui.book.bean.BookDetailsBean;
import org.jssvc.lib.ui.book.bean.BookReadingBean;
import org.jssvc.lib.ui.book.bean.BookSearchBean;
import org.jssvc.lib.ui.book.bean.BookShelfBean;
import org.jssvc.lib.ui.book.bean.BookShelfListBean;
import org.jssvc.lib.ui.book.bean.LibraryUser;
import org.jssvc.lib.data.HttpUrlParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lygdh on 2016/11/15.
 */

public class HtmlParseUtils {

    // 登录/激活错误信息获取===========================================================================
    // 查找验证失败提示（[color=red]）信息，若返回结果为空则说明登录成功
    public static String getErrMsgOnLogin(String result) {
        String errorMsg = "";
        Document doc = Jsoup.parse(result);
        Elements links = doc.select("[color=red]");
        for (Element link : links) {
            errorMsg = link.text().toString().trim();
        }
        return errorMsg;
    }

    // 修改密码===========================================================================
    // 修改密码返回解析
    public static String getErrMsgOnChangePwd(String html) {
        String errorMsg = "";
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("[class=iconerr]");
        for (Element link : links) {
            errorMsg = link.text().toString().trim();
        }
        return errorMsg;
    }

    // 证件信息===========================================================================
    // 解析证件信息
    public static List<String> readUserInfo(String result) {
        List<String> stringList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        Elements links = doc.select("TD");
        if (links != null) {
            for (Element link : links) {
                String linkText = link.ownText() + "";
                stringList.add(linkText.toString());
            }
        }
        return stringList;
    }

    // 获取证件信息
    public static LibraryUser getUserInfo(String result) {
        LibraryUser user = new LibraryUser();
        List<String> stringList = readUserInfo(result);
        if (stringList.size() > 25) {
            user.setUserid("" + stringList.get(0));//学号
            user.setUsername("" + stringList.get(1));//姓名
            user.setSex("" + stringList.get(2));//性别
            user.setType("" + stringList.get(6));//读者类型
            user.setReadType("" + stringList.get(7));//借阅等级
            user.setDepartment("" + stringList.get(9));//工作单位
            user.setCardStartDate("" + stringList.get(19));//卡生效时间
            user.setCardEndDate("" + stringList.get(20));//卡失效时间
            user.setDeposit("" + stringList.get(21));//押金
            user.setPoundage("" + stringList.get(22));//手续费
            user.setReadTimes("" + stringList.get(23));//累计借书
            user.setViolation("" + stringList.get(24));//违章状态
            user.setDebt(stringList.get(25).startsWith(".") ? "0" + stringList.get(25) : "" + stringList.get(25));//欠款状态
        }
        return user;
    }

    // 当前借阅===========================================================================
    // 解析图书列表
    public static List<BookReadingBean> getCurrentBorrowList(String result) {
        List<BookReadingBean> bookList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        doc.setBaseUri(HttpUrlParams.BASE_LIB_URL);
        Elements links = doc.select("table").select("tr");
        if (links != null && links.size() > 1) {
            for (int i = 1; i < links.size(); i++) {
                BookReadingBean book = new BookReadingBean();
                Elements els = links.get(i).select("td");

                book.setBarCode(els.get(0).text());// barCode
                book.setBookName(els.get(1).text());// 名称+作者
                book.setBorrowDate(els.get(2).text());
                book.setReturnDate(els.get(3).select("font").text());
                book.setTimes(els.get(4).text());
                book.setPlace(els.get(5).text());

                Elements urls = els.get(1).select("a[href]");
                book.setDetialUrl(urls.get(0).attr("abs:href"));

                bookList.add(book);
            }
        }

        return bookList;
    }

    // 借阅历史===========================================================================
    // 解析图书列表
    public static List<BookReadingBean> getReturnBorrowList(String result) {
        List<BookReadingBean> bookList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        doc.setBaseUri(HttpUrlParams.BASE_LIB_URL);
        Elements links = doc.select("table").select("tr");
        if (links != null && links.size() > 1) {
            for (int i = 1; i < links.size(); i++) {
                BookReadingBean book = new BookReadingBean();
                Elements els = links.get(i).select("td");

                book.setBarCode(els.get(1).text());// barCode
                book.setBookName(els.get(2).text());// 名称
                book.setBookAuthor(els.get(3).text());// 作者
                book.setBorrowDate(els.get(4).text());
                book.setReturnDate(els.get(5).text());
                book.setPlace(els.get(6).text());

                Elements urls = els.get(2).select("a[href]");
                book.setDetialUrl(urls.get(0).attr("abs:href"));

                bookList.add(book);
            }
        }
        return bookList;
    }

    // 图书搜索===========================================================================
    // 获取搜索总数
    public static int getBookSearchListCount(String result) {
        int count = 0;
//        Integer count = 0;
        Document doc = Jsoup.parse(result);
        String s = doc.getElementsByClass("red").text();
        try {
            count = Integer.parseInt(s);// 数字or提示信息，使用Integer做返回值类型自处理异常
        } catch (Exception e) {
            count = 0;
        }
        return count;
    }

    // 解析图书搜索
    public static List<BookSearchBean> getBookSearchList(String result) {
        List<BookSearchBean> bookList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        doc.setBaseUri(HttpUrlParams.BASE_LIB_URL + "opac/");
        Elements links = doc.getElementsByClass("list_books");
        if (links != null) {
            for (Element link : links) {
                BookSearchBean book = new BookSearchBean();
                Elements codes = link.select("h3");// CODE
                book.setCode(codes.get(0).ownText());

                Elements types = link.select("h3>span");// 图书类型
                book.setType(types.get(0).ownText());

                Elements titles = link.select("h3>a");// 书名
                String namenum = titles.get(0).text();
                String str[] = namenum.split("\\.");
                String itemNo = str[0];
                String name = namenum.substring(itemNo.length() + 1, namenum.length());
                book.setNo(itemNo);// 提取标号
                book.setTitle(name);// 提取书名全称

                Elements copys = link.select("p>span");// 副本
                String fuben = copys.get(0).text();
                String f[] = fuben.split(" ");// 以空格为分割
                book.setCopy_Total("" + (TextUtils.isEmpty(f[0]) ? "" : f[0]).replaceAll("馆藏复本：", ""));
                book.setCopy_Remain("" + (TextUtils.isEmpty(f[1]) ? "" : f[1]).replaceAll("可借复本：", ""));

                Elements urls = link.select("a[href]");
                book.setDetialUrl(urls.get(0).attr("abs:href"));

                Elements authors = link.select("p");// 作者和出版社
                authors.select("span").remove();// 移除馆藏信息
                String linkInnerH = authors.html();
                String a[] = linkInnerH.split("<br>");
                book.setAuthor(TextUtils.isEmpty(a[0]) ? "" : a[0]);// 作者
                book.setPublisher(TextUtils.isEmpty(a[1]) ? "" : a[1]);// 出版社&日期

                bookList.add(book);
            }
        }
        return bookList;
    }

    // 图书封面===========================================================================
    public static String getBookCoverUrl(String result) {
        String coverUrl = "";
        Document doc = Jsoup.parse(result);
        Elements links = doc.select("[width=95]");
        if (links != null && links.size() > 0) {
            Elements media = links.get(0).select("[src]");
            coverUrl = media.attr("abs:src");
        }
        return coverUrl;
    }

    // 图书详情===========================================================================
    // 解析图书详情
    public static List<BookDetailsBean> getBookDetailsList(String result) {
        List<BookDetailsBean> detailList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        Elements links = doc.getElementsByClass("booklist");
        if (links != null) {
            for (int i = 0; i < links.size(); i++) {
                BookDetailsBean item = new BookDetailsBean();
                item.setKey(links.get(i).select("dt").text());
                item.setValue(links.get(i).select("dd").text());

                if (!TextUtils.isEmpty(item.getValue())) {
                    detailList.add(item);
                }
            }
        }
        return detailList;
    }

    // 图书详情-馆藏列表===========================================================================
    // 解析馆藏列表
    public static List<BookAccessBean> getBookAccessList(String result) {
        List<BookAccessBean> bookList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        Elements links = doc.select("table").select("[width=\"670\"]").select("tr");
        if (links != null && links.size() > 1) {
            for (int i = 1; i < links.size(); i++) {
                BookAccessBean book = new BookAccessBean();
                Elements els = links.get(i).select("td");

                book.setTpCode(els.get(0).text());
                book.setBarCode(els.get(1).text());// barCode
                book.setCell(els.get(2).text());
                book.setArea(els.get(3).text());
                book.setPlace(els.get(4).text());
                book.setState(els.get(5).text());

                bookList.add(book);
            }
        }
        return bookList;
    }

    // 我的书架===========================================================================
    // 书架列表
    public static List<BookShelfBean> getBookShelfList(String result) {
        List<BookShelfBean> shelfList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        doc.setBaseUri(HttpUrlParams.BASE_LIB_URL + "reader/");
        Elements links = doc.select("table").select("tr");
        if (links != null && links.size() > 1) {
            for (int i = 1; i < links.size(); i++) {
                BookShelfBean shelf = new BookShelfBean();
                Elements els = links.get(i).select("td");
                if (els.size() == 6) {
                    Elements urls = els.get(0).select("a[href]");
                    String durl = urls.get(0).attr("abs:href") + "";
                    shelf.setUrl(durl);

                    String[] array = durl.split("classid=");
                    if (array.length == 2) {
                        shelf.setId("" + array[1]);
                    } else {
                        shelf.setId("");
                    }

                    shelf.setName(els.get(0).text() + "");
                    shelf.setCount(els.get(1).text() + "");
                    shelf.setDate(els.get(2).text() + "");
                    shelf.setRemark(els.get(3).text() + "");

                    Elements delurls = els.get(4).select("a[href]");
                    shelf.setDeleteurl(delurls.get(0).attr("abs:href") + "");

                    shelfList.add(shelf);
                }
            }
        }
        return shelfList;
    }

    // 书架里的图书
    public static List<BookShelfListBean> getBookOnShelfList(String result) {
        List<BookShelfListBean> bookList = new ArrayList<>();
        Document doc = Jsoup.parse(result);
        doc.setBaseUri(HttpUrlParams.BASE_LIB_URL);
        Elements links = doc.select("table").select("tr");
        Elements temp = doc.getElementsByAttribute("name");
        if (links != null && links.size() > 1) {
            for (int i = 1; i < links.size(); i++) {
                BookShelfListBean book = new BookShelfListBean();
                Elements els = links.get(i).select("td");
                Elements urls = els.get(1).select("a[href]");
                book.setUrl(urls.get(0).attr("abs:href") + "");
                book.setName(els.get(1).text() + "");
                book.setAuthor(els.get(2).text() + "");
                book.setPublisher(els.get(3).text() + "");
                book.setCode(els.get(5).text() + "");

                String value = temp.get(i - 1).attr("name") + "";
                book.setBookcode(value.replaceAll("del_", ""));

                bookList.add(book);
            }
        }
        return bookList;
    }
}
