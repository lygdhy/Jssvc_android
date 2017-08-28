package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Handler;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import qiu.niorgai.StatusBarCompat;

/**
 * APP启动页面
 */
public class SplashActivity extends BaseActivity {

  @Override protected int getContentViewId() {
    return R.layout.activity_splash;
  }

  @Override protected void initView() {
    StatusBarCompat.translucentStatusBar(this, false);

    Handler handler = new Handler();
    handler.postDelayed(new splashhandler(), 1000);//静态启动页
  }

  class splashhandler implements Runnable {
    public void run() {
      if (AccountPref.isLogon(context)) {
        // 自动登录
        autoLogin();
      } else {
        // 用户名密码不全，不登录直接进入
        AccountPref.removeLogonAccoundPwd(context);
        startActivity(new Intent(context, MainActivity.class));
        //startActivity(new Intent(context, HomeActivity.class));
        finish();
      }
    }
  }

  // 自动登录
  private void autoLogin() {
    // 用户名和密码都在，静默登录
    OkGo.<String>post(HttpUrlParams.URL_LIB_LOGIN).tag(this)
        .params("number", AccountPref.getLogonAccoundNumber(context))
        .params("passwd", AccountPref.getLogonAccoundPwd(context))
        .params("select", AccountPref.getLogonType(context))
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            dissmissProgressDialog();
            // 账号统计
            MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(),
                AccountPref.getLogonAccoundNumber(context));
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            AccountPref.removeLogonAccoundPwd(context);// 登录失败
            dealNetError(response);
          }

          @Override public void onFinish() {
            super.onFinish();
            // 完成跳转
            startActivity(new Intent(context, MainActivity.class));
            //startActivity(new Intent(context, HomeActivity.class));
            finish();
          }
        });

    //OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
    //    .tag(this)
    //    .params("number", AccountPref.getLogonAccoundNumber(context))
    //    .params("passwd", AccountPref.getLogonAccoundPwd(context))
    //    .params("select", AccountPref.getLogonType(context))
    //    .execute(new StringCallback() {
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dealNetError(e);
    //        // 登录失败
    //        AccountPref.removeLogonAccoundPwd(context);
    //      }
    //
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        // 账号统计
    //        MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(),
    //            AccountPref.getLogonAccoundNumber(context));
    //      }
    //
    //      @Override public void onAfter(@Nullable String s, @Nullable Exception e) {
    //        super.onAfter(s, e);
    //        // 完成跳转
    //        startActivity(new Intent(context, MainActivity.class));
    //        //startActivity(new Intent(context, HomeActivity.class));
    //        finish();
    //      }
    //    });
  }
}
