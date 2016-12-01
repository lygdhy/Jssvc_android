package org.jssvc.lib.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.NetworkUtils;

import java.util.List;

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
                if (NetworkUtils.isConnected(context)) {
                    Acp.getInstance(context).request(new AcpOptions.Builder()
                                    .setPermissions(Manifest.permission.INTERNET)
                                    .build(),
                            new AcpListener() {
                                @Override
                                public void onGranted() {
                                    // 登录
                                    autoLogin();
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    showToast(permissions.toString() + "权限拒绝");
                                }
                            });
                } else {
                    showToast("无法连接网络");
                    AccountPref.removeLogonAccoundPwd(context);
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
            } else {
                // 用户名密码不全，不登陆直接进入
                AccountPref.removeLogonAccoundPwd(context);
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }
    }

    // 自动登录
    private void autoLogin() {
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
                        dealNetError(e);
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
    }
}
