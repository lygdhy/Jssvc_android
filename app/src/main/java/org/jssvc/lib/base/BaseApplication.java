package org.jssvc.lib.base;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.mob.MobSDK;
import com.pgyersdk.crash.PgyCrashManager;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.ui.account.bean.UserBean;
import org.jssvc.lib.view.ActivityLifecycleHelper;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by lygdh on 2016/11/15.
 */

public class BaseApplication extends MultiDexApplication {

    public static SPUtils spUtils;
    private static final String FILE_NAME = "sp_jssvc_lib";

    BaseApplication appContext;
    public static UserBean localUserBean = null;// 全局账户（来自SharedPreferences）
    public static boolean libOnline = false;//图书馆在线（来自内存）

    private ActivityLifecycleHelper mActivityLifecycleHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        Utils.init(appContext);
        if (spUtils == null) {
            spUtils = SPUtils.getInstance(FILE_NAME);
        }

        // 蒲公英
        PgyCrashManager.register(this);

        // 友盟错误统计
        MobclickAgent.setCatchUncaughtExceptions(true);

        // 初始化OkGo
        initOkGo();

        // Mob SDK
        MobSDK.init(this);

        // X5浏览服务
        QbSdk.initX5Environment(appContext, null);

        // 注册滑动返回
        registerActivityLifecycleCallbacks(mActivityLifecycleHelper = new ActivityLifecycleHelper());
    }

    // 初始化OkGo
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 配置log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //设置OkHttpClient
                .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)//先使用缓存，不管是否存在，仍然请求网络
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                       //全局公共参数
    }

    public ActivityLifecycleHelper getActivityLifecycleHelper() {
        return mActivityLifecycleHelper;
    }

    public void onSlideBack(boolean isReset, float distance) {
        if (mActivityLifecycleHelper != null) {
            Activity lastActivity = mActivityLifecycleHelper.getPreActivity();
            if (lastActivity != null) {
                View contentView = lastActivity.findViewById(android.R.id.content);
                if (isReset) {
                    contentView.setX(contentView.getLeft());
                } else {
                    final int width = getResources().getDisplayMetrics().widthPixels;
                    contentView.setX(-width / 3 + distance / 3);
                }
            }
        }
    }
}
