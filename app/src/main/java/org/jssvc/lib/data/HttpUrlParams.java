package org.jssvc.lib.data;

/**
 * Created by lygdh on 2016/9/13.
 */

public class HttpUrlParams {

    public static final String BASE_URL = "http://www.hydong.me/";

    public static final String URL_EXPAND = BASE_URL + "appSupport/expand/";//扩展页面
    public static final String URL_AD = BASE_URL + "appSupport/adPics/";//广告
    public static final String URL_ARTICLE = BASE_URL + "appSupport/article/";//文章

    // 图书馆
    public static final String BASE_LIB_URL = "http://opac.jssvc.edu.cn:8080/";

    public static final String URL_LIB_LOGIN = BASE_LIB_URL + "reader/redr_verify.php";// 图书馆登陆
    public static final String URL_LIB_ACCOUND = BASE_LIB_URL + "reader/redr_info.php";// 个人详情
}
