package org.jssvc.lib.utils;

import android.content.Context;

/**
 * Created by jjj on 2018/1/29.
 *
 * @description:
 */

public class AppUtils {

    private Context mContext;   //ApplicationContext
    private volatile static AppUtils mInstance;
    /**
     * 初始化配置
     *
     * @param configuration
     */
    public synchronized void init(BaseConfiguration configuration) {

        this.mContext = configuration.context;
    }

    public static AppUtils getInstance() {
        if (mInstance == null) {
            synchronized (AppUtils.class) {
                if (mInstance == null) {
                    mInstance = new AppUtils();
                }
            }
        }
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }
}
