package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.HttpUrlParams;

import okhttp3.Call;
import qiu.niorgai.StatusBarCompat;

import static org.jssvc.lib.data.AccountPref.getLogonAccoundNumber;
import static org.jssvc.lib.data.AccountPref.getLogonAccoundPwd;
import static org.jssvc.lib.data.AccountPref.getLogonType;
import static org.jssvc.lib.data.AccountPref.isLogon;

/**
 * APP启动页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        StatusBarCompat.translucentStatusBar(this, false);

        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 500);//静态启动页
    }

    class splashhandler implements Runnable {
        public void run() {

//            if (AppPref.isFirstRunning(context)) {
//                // 第一次启动APP，展示新版特性
//                AppPref.setAlreadyRun(context);// 这句话带入下一步骤执行
//            } else {
//                // 进入主题
//            }

            if (isLogon(context)) {
                // 用户名和密码都在，静默登陆
                // 登陆
                OkHttpUtils.post().url(HttpUrlParams.URL_LIB_LOGIN)
                        .addParams("number", getLogonAccoundNumber(context))
                        .addParams("passwd", getLogonAccoundPwd(context))
                        .addParams("select", getLogonType(context))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                // 登陆失败也直接进入
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                // 登陆成功，直接进入
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }
                        });
            } else {
                // 用户名密码不全，不登录直接进入
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    protected boolean supportSlideBack() {
        return false;
    }

}
