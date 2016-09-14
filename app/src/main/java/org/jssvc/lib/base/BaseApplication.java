package org.jssvc.lib.base;

import android.app.Application;

import org.jssvc.lib.BuildConfig;
import org.xutils.x;


/**
 * Created by lygdh on 2016/9/13.
 */

public class BaseApplication extends Application {
    BaseApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        // 初始化xUtils3
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
