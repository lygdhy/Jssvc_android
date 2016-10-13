package org.jssvc.lib.data;

/**
 * Created by lygdh on 2016/9/13.
 */

public class HttpUrlParams {

    // 平台服务
    public static final String BASE_HOST = "http://www.jssvc.org/";
    public static final String BASE_API = BASE_HOST + "app/api/";//接口地址

    public static final String URL_EXPAND = BASE_HOST + "app/expand/";//扩展页面
    public static final String URL_AD = BASE_HOST + "app/adPics/";//广告
    public static final String URL_ARTICLE = BASE_HOST + "app/article/";//文章

    // 图书馆
    public static final String BASE_LIB_URL = "http://opac.jssvc.edu.cn:8080/";

    public static final String URL_LIB_LOGIN = BASE_LIB_URL + "reader/redr_verify.php";// 图书馆登陆
    public static final String URL_LIB_ACCOUND = BASE_LIB_URL + "reader/redr_info.php";// 个人详情
}
