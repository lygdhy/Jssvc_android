package org.jssvc.lib.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.Response;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import org.jssvc.lib.view.SwipeWindowHelper;
import org.jssvc.lib.view.pDialog.XProgressDialog;

/**
 * Activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity {
  private Unbinder unbinder;
  public Context context;

  private Toast toast = null;//全局Toast
  private XProgressDialog progressDialog = null;//全局ProgressDialog

  protected abstract int getContentViewId();

  protected abstract void initView();

  private SwipeWindowHelper mSwipeWindowHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentViewId());
    unbinder = ButterKnife.bind(this);
    context = this;
    initView();

    // 蒲公英错误日志收集
    PgyCrashManager.register(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    // unbinder.unbind();
    PgyCrashManager.unregister();
  }

  public void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }

  public void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    if (!supportSlideBack()) {
      return super.dispatchTouchEvent(ev);
    }

    if (mSwipeWindowHelper == null) {
      mSwipeWindowHelper = new SwipeWindowHelper(getWindow());
    }
    return mSwipeWindowHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
  }

  /**
   * 是否支持滑动返回
   */
  protected boolean supportSlideBack() {
    return true;
  }

  /**
   * show Toast
   */
  protected void showToast(String msg) {
    if (toast == null) {
      toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    } else {
      toast.setText(msg);
    }
    toast.show();
  }

  /**
   * show ProgressDialog
   */
  protected void showProgressDialog() {
    KeyboardUtils.hideSoftInput(this);
    if (progressDialog == null) {
      progressDialog = new XProgressDialog(context, XProgressDialog.THEME_HORIZONTAL_SPOT);
    }
    progressDialog.show();
  }

  /**
   * show ProgressDialog
   */
  protected void showProgressDialog(String msg) {
    KeyboardUtils.hideSoftInput(this);
    if (progressDialog == null) {
      progressDialog = new XProgressDialog(context, msg, XProgressDialog.THEME_HORIZONTAL_SPOT);
    } else {
      progressDialog.setMessage(msg);
    }
    progressDialog.show();
  }

  /**
   * dissmiss ProgressDialog
   */
  protected void dissmissProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  /**
   * 网络错误处理
   */
  protected void dealNetError(Response response) {
    Exception e = (Exception) response.getException();
    if (!NetworkUtils.isConnected()) {
      showToast("无法连接网络");
    } else if (e.getMessage().contains("No address associated with hostname")) {
      // Unable to resolve host "opac.jssvc.edu.cn": No address associated with hostname
      showToast("服务器故障，请稍后重试！");
    } else if (e == HttpException.NET_ERROR()) {
      // network error! http response code is 404 or 5xx!
      showToast("网络错误，错误代码 " + response.code());
    } else {
      showToast(e.getMessage());
    }
  }

  public void callQQCell(String qqNum) {
    // 手机QQ com.tencent.mqq
    // 手机QQ2012 com.tencent.mobileqq
    // QQ轻聊版 com.tencent.qqlite
    // QQ国际版 com.tencent.mobileqqi
    // QQHD com.tencent.minihd.qq
    // 企业QQ com.tencent.eim
    if (AppUtils.isInstallApp("com.tencent.mqq")
        || AppUtils.isInstallApp("com.tencent.mobileqq")
        || AppUtils.isInstallApp("com.tencent.qqlite")
        || AppUtils.isInstallApp("com.tencent.mobileqqi")
        || AppUtils.isInstallApp("com.tencent.minihd.qq")
        || AppUtils.isInstallApp("com.tencent.eim")) {
      String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    } else {
      showToast("暂未安装QQ相关软件无法发起聊天");
    }
  }
}
