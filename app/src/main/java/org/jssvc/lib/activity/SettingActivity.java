package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.umeng.analytics.MobclickAgent;
import okhttp3.Call;
import okhttp3.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.User;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.DataCleanManager;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomDialog;

import static com.pgyersdk.update.UpdateManagerListener.getAppBeanFromString;
import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

  @BindView(R.id.btn_exit) Button btnExit;
  @BindView(R.id.tv_version) TextView tvVersion;
  @BindView(R.id.tv_cache) TextView tvCache;

  @Override protected int getContentViewId() {
    return R.layout.activity_setting;
  }

  @Override protected void initView() {
    setCurrentRubbish();

    autoUpdateCheck();
  }

  @Override public void onResume() {
    super.onResume();
    if (AccountPref.isLogon(context)) {
      // 用户名和密码都在
      btnExit.setVisibility(View.VISIBLE);
      btnExit.setText("注销" + AccountPref.getLogonAccoundNumber(context));

      loadUserInfo();
    } else {
      btnExit.setVisibility(View.GONE);
    }
  }

  private void loadUserInfo() {
    User user = AccountPref.getLogonUser(context);
    if (user == null || TextUtils.isEmpty(user.getUserid())) {
      getUserInfoByNet();
    } else {
      loadUserInfo2UI(user);
    }
  }

  @OnClick({
      R.id.tv_back, R.id.rl_update, R.id.rl_clear, R.id.rl_feedback, R.id.rl_about, R.id.btn_exit,
      R.id.rl_appraise, R.id.rl_invitation
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_update:
        // 版本检查
        PgyUpdateManager.register(this, "org.jssvc.lib.provider", new UpdateManagerListener() {
          @Override public void onUpdateAvailable(String result) {
            findNewVer(result);
          }

          @Override public void onNoUpdateAvailable() {
            showToast("已经是最新版");
          }
        });
        break;
      case R.id.rl_clear:
        // 清除缓存
        clearAlertDialog();
        break;
      case R.id.rl_feedback:
        // 意见反馈
        startActivity(new Intent(context, FeedbackActivity.class));
        break;
      case R.id.rl_about:
        // 关于我们
        startActivity(new Intent(context, AboutActivity.class));
        break;
      //case R.id.rlMine:
      //  // 证件信息=============================
      //  if (AccountPref.isLogon(context)) {
      //    startActivity(new Intent(context, CardInfoActivity.class));
      //  } else {
      //    startActivity(new Intent(context, LoginActivity.class));
      //  }
      //  break;
      case R.id.rl_appraise:
        // 给我们点个赞
        break;
      case R.id.rl_invitation:
        // 邀请好友使用
        startActivity(new Intent(context, ShareActivity.class));
        break;
      case R.id.btn_exit:
        // 注销
        if (AccountPref.isLogon(context)) {
          AccountPref.removeLogonAccoundPwd(context);
          AccountPref.removeLogonUser(context);
          btnExit.setVisibility(View.GONE);

          // 账号统计
          MobclickAgent.onProfileSignOff();
        }
        break;
    }
  }

  // 获取个人信息
  private void getUserInfoByNet() {
    OkGo.post(HttpUrlParams.URL_LIB_ACCOUND).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(String s, Call call, Response response) {
        // s 即为所需要的结果
        parseHtml(s);
      }

      @Override public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        dealNetError(e);
      }
    });
  }

  // 解析网页
  private void parseHtml(String s) {
    User user = HtmlParseUtils.getUserInfo(s);
    if (user != null && !TextUtils.isEmpty(user.getUserid())) {
      AccountPref.saveLogonUser(context, user);
      loadUserInfo2UI(user);
    } else {
      showToast("解析失败");
    }
  }

  // 加载数据到页面
  private void loadUserInfo2UI(User user) {

    // 解析借阅次数
    if (!TextUtils.isEmpty(user.getReadTimes())) {
      String timestr = user.getReadTimes().replaceAll("册次", "");
      try {
        int times = Integer.parseInt(timestr);
        int level = CardInfoActivity.getLevelByTimes(times);
      } catch (Exception e) {
      }
    }
  }

  // 发现新版本
  private void findNewVer(String result) {
    final AppBean appBean = getAppBeanFromString(result);

    CustomDialog.Builder builder = new CustomDialog.Builder(context);
    builder.setTitle("更新");
    builder.setMessage(appBean.getReleaseNote());
    builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();
        startDownloadTask(SettingActivity.this, appBean.getDownloadURL());
      }
    });
    builder.setNegativeButton("稍后再说", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.create().show();
  }

  // 版本自动检查
  private void autoUpdateCheck() {
    PgyUpdateManager.register(this, "org.jssvc.lib.provider", new UpdateManagerListener() {
      @Override public void onUpdateAvailable(String result) {
        if (tvVersion != null) {
          tvVersion.setText("发现新版本");
          tvVersion.setTextColor(ContextCompat.getColor(context, R.color.color_text_warn));
        }
      }

      @Override public void onNoUpdateAvailable() {
        if (tvVersion != null) {
          tvVersion.setText("已经是最新版");
          tvVersion.setTextColor(ContextCompat.getColor(context, R.color.color_text_hint));
        }
      }
    });
  }

  // 一秒后刷新
  class splashhandler implements Runnable {
    public void run() {
      showToast("清理完成");
      setCurrentRubbish();
    }
  }

  // 获取当前垃圾大小
  private void setCurrentRubbish() {
    String sizeStr = "0.0M";
    try {
      sizeStr = DataCleanManager.getTotalCacheSize(getApplicationContext());
      tvCache.setText(sizeStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 清空缓存dialog
  public void clearAlertDialog() {
    CustomDialog.Builder builder = new CustomDialog.Builder(context);
    builder.setTitle("提示");
    builder.setMessage("确定要清除缓存吗？");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();

        DataCleanManager.clearAllCache(getApplicationContext());
        showToast("清理中...");
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 3000);
      }
    });
    builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.create().show();
  }
}