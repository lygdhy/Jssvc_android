package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import okhttp3.Call;
import okhttp3.Response;
import qiu.niorgai.StatusBarCompat;

/**
 * APP启动页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this, false);

        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 1000);//静态启动页
    }

    class splashhandler implements Runnable {
        public void run() {
            if (AccountPref.isLogon(context)) {
                // 用户名和密码都在，静默登陆
                OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
                        .tag(this)
                        .params("number", AccountPref.getLogonAccoundNumber(context))
                        .params("passwd", AccountPref.getLogonAccoundPwd(context))
                        .params("select", AccountPref.getLogonType(context))
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                // 登陆失败
                                AccountPref.removeLogonAccoundPwd(context);
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                // 登陆成功
                            }

                            @Override
                            public void onAfter(@Nullable String s, @Nullable Exception e) {
                                super.onAfter(s, e);
                                // 完成跳转
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }
                        });
            } else {
                // 用户名密码不全，不登陆直接进入
                AccountPref.removeLogonAccoundPwd(context);
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }
    }
}
