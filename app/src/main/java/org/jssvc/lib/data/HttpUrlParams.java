package org.jssvc.lib.data;

/**
 * Created by lygdh on 2016/9/13.
 */

public class HttpUrlParams {

    // 图书馆
    public static final String BASE_LIB_URL = "http://opac.jssvc.edu.cn:8080/";

    public static final String URL_LIB_USER_REGISTER = BASE_LIB_URL + "reader/redr_con_result.php";// 账号激活
    public static final String URL_LIB_CHANGE_PWD = BASE_LIB_URL + "reader/change_passwd_result.php";// 修改密码

    public static final String URL_LIB_LOGIN = BASE_LIB_URL + "reader/redr_verify.php";// 图书馆登陆
    public static final String URL_LIB_ACCOUND = BASE_LIB_URL + "reader/redr_info.php";// 个人详情

    public static final String URL_LIB_CURRENT_BORROW = BASE_LIB_URL + "reader/book_lst.php";// 当前借阅
    public static final String URL_LIB_HISTORY_BORROW = BASE_LIB_URL + "reader/book_hist.php";// 借阅历史
    public static final String URL_LIB_RENEW_BORROW = BASE_LIB_URL + "reader/ajax_renew.php";// 续借

    public static final String URL_LIB_BOOK_SEARCH = BASE_LIB_URL + "opac/openlink.php";// 图书搜索

    public static final String URL_LIB_BOOK_SHELF = BASE_LIB_URL + "reader/book_shelf_man.php";// 我的书架

    // 自有服务
    public static final String BASE_ORG_URL = "http://www.jssvc.org/";

    public static final String URL_ORG_FEEDBACK = BASE_ORG_URL + "svu/feedback.php";// 意见反馈

    // 新书推荐
    // http://lib.jssvc.edu.cn/reading/newbooks/new.html
}
