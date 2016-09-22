package org.jssvc.lib.base;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;

import org.jssvc.lib.utils.ActivityLifecycleHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by lygdh on 2016/9/13.
 */

public class BaseApplication extends Application {
    BaseApplication appContext;

    private ActivityLifecycleHelper mActivityLifecycleHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        //
        registerActivityLifecycleCallbacks(mActivityLifecycleHelper = new ActivityLifecycleHelper());

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
