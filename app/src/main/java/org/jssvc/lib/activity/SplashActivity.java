package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import butterknife.BindView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.ImageLoader;
import qiu.niorgai.StatusBarCompat;

/**
 * APP启动页面
 */
public class SplashActivity extends BaseActivity {

  @BindView(R.id.iv_ad) ImageView mImageView;

  @Override protected int getContentViewId() {
    return R.layout.activity_splash;
  }

  @Override protected void initView() {
    StatusBarCompat.translucentStatusBar(this, false);

    String picPath =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505302208889&di=a6c81211133cc6fdbac4b451e699bc15&imgtype=0&src=http%3A%2F%2Fdown1.sucaitianxia.com%2Fpsd02%2Fpsd242%2Fpsds65632.jpg";
    ImageLoader.with(mContext, mImageView, picPath);

    Handler handler = new Handler();
    handler.postDelayed(new splashhandler(), 3000);//静态启动页
  }

  /**
   * 是否支持滑动返回
   */
  protected boolean supportSlideBack() {
    return false;
  }

  class splashhandler implements Runnable {
    public void run() {
      if (AccountPref.isLogon(mContext)) {
        // 自动登录
        autoLogin();
      } else {
        // 用户名密码不全，不登录直接进入
        AccountPref.removeLogonAccoundPwd(mContext);
        startActivity(new Intent(mContext, MainActivity.class));
        //startActivity(new Intent(mContext, HomeActivity.class));
        finish();
      }
    }
  }

  // 自动登录
  private void autoLogin() {
    // 用户名和密码都在，静默登录
    OkGo.<String>post(HttpUrlParams.URL_LIB_LOGIN).tag(this)
        .params("number", AccountPref.getLogonAccoundNumber(mContext))
        .params("passwd", AccountPref.getLogonAccoundPwd(mContext))
        .params("select", AccountPref.getLogonType(mContext))
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            // 账号统计
            MobclickAgent.onProfileSignIn(AccountPref.getLogonType(mContext).toUpperCase(),
                AccountPref.getLogonAccoundNumber(mContext));
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            AccountPref.removeLogonAccoundPwd(mContext);// 登录失败
            dealNetError(response);
          }

          @Override public void onFinish() {
            super.onFinish();
            // 完成跳转
            startActivity(new Intent(mContext, MainActivity.class));
            //startActivity(new Intent(mContext, HomeActivity.class));
            finish();
          }
        });

    //OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
    //    .tag(this)
    //    .params("number", AccountPref.getLogonAccoundNumber(mContext))
    //    .params("passwd", AccountPref.getLogonAccoundPwd(mContext))
    //    .params("select", AccountPref.getLogonType(mContext))
    //    .execute(new StringCallback() {
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dealNetError(e);
    //        // 登录失败
    //        AccountPref.removeLogonAccoundPwd(mContext);
    //      }
    //
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        // 账号统计
    //        MobclickAgent.onProfileSignIn(AccountPref.getLogonType(mContext).toUpperCase(),
    //            AccountPref.getLogonAccoundNumber(mContext));
    //      }
    //
    //      @Override public void onAfter(@Nullable String s, @Nullable Exception e) {
    //        super.onAfter(s, e);
    //        // 完成跳转
    //        startActivity(new Intent(mContext, MainActivity.class));
    //        //startActivity(new Intent(mContext, HomeActivity.class));
    //        finish();
    //      }
    //    });
  }
}
