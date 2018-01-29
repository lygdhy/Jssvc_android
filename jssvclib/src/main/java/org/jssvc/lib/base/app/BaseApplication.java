package org.jssvc.lib.base.app;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

import org.jssvc.lib.utils.AppUtils;
import org.jssvc.lib.utils.BaseConfiguration;


/**
 * Created by jjj on 2018/1/29.
 *
 * @description:
 */

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initAppUtils();
    }

    private void initAppUtils() {
        BaseConfiguration configuration = new BaseConfiguration.Builder(this)
                .build();
        AppUtils.getInstance().init(configuration);
        Utils.init(this);
    }
}
