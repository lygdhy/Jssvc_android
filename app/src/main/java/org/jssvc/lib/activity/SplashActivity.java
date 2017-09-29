package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import butterknife.BindView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.ThirdAccountBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.utils.ImageLoader;
import qiu.niorgai.StatusBarCompat;

import static org.jssvc.lib.base.BaseApplication.libOnline;
import static org.jssvc.lib.base.BaseApplication.localMemberBean;

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
    handler.postDelayed(new splashhandler(), 1000);//静态启动页
  }

  /**
   * 是否支持滑动返回
   */
  protected boolean supportSlideBack() {
    return false;
  }

  class splashhandler implements Runnable {
    public void run() {

      // 如果SP有数据，则初始化账户
      if (DataSup.hasLogin()) localMemberBean = DataSup.getLocalMemberBean();

      // 如果有绑定图书馆，则静默登录图书馆，并libOnline=true
      ThirdAccountBean libBean = DataSup.getThirdAccountBean(Constants.THIRD_ACCOUNT_CODE_LIB);
      if (libBean != null) {
        autoLogin(libBean.getAccount(), libBean.getPwd(), libBean.getPwd());
      } else {
        // 完成跳转
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
      }
    }
  }

  // 自动登录
  private void autoLogin(String number, String passwd, String select) {
    // 用户名和密码都在，静默登录
    OkGo.<String>post(HttpUrlParams.URL_LIB_LOGIN).tag(this)
        .params("number", number)
        .params("passwd", passwd)
        .params("select", select)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            String errorMsg = HtmlParseUtils.getErrMsgOnLogin(response.body());
            if (TextUtils.isEmpty(errorMsg)) {
              libOnline = true;// 登录成功，标识为已登录
            }
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onFinish() {
            super.onFinish();
            // 完成跳转
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
          }
        });
  }
}
