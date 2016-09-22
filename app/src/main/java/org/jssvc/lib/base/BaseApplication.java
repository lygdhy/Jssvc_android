package org.jssvc.lib.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by lygdh on 2016/9/13.
 */

public class BaseApplication extends Application {
    BaseApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        // 初始化Fresco
        Fresco.initialize(this);

        // 初始化OkHttpClient
        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar1)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
