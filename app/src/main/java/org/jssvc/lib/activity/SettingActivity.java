package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.utils.DataCleanManager;
import org.jssvc.lib.view.CustomDialog;

import static com.pgyersdk.update.UpdateManagerListener.getAppBeanFromString;
import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;
import static org.jssvc.lib.base.BaseApplication.libOnline;
import static org.jssvc.lib.base.BaseApplication.localMemberBean;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

  @BindView(R.id.rl_exit) RelativeLayout rlExit;
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
    if (DataSup.hasLogin()) {// 已登录
      rlExit.setVisibility(View.VISIBLE);
    } else {// 未登录
      rlExit.setVisibility(View.GONE);
    }
  }

  @OnClick({
      R.id.tv_back, R.id.rl_update, R.id.rl_clear, R.id.rl_feedback, R.id.rl_about, R.id.rl_exit,
      R.id.rl_appraise, R.id.rl_invitation, R.id.rl_app
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
        startActivity(new Intent(mContext, FeedbackActivity.class));
        break;
      case R.id.rl_about:
        // 关于我们
        startActivity(new Intent(mContext, AboutActivity.class));
        break;
      case R.id.rl_appraise:
        // 给我们点个赞
        break;
      case R.id.rl_invitation:
        // 邀请好友使用
        startActivity(new Intent(mContext, ShareActivity.class));
        break;
      case R.id.rl_app:
        // 精品应用推荐
        break;
      case R.id.rl_exit:
        // 注销
        DataSup.setMemberStr2Local("");
        localMemberBean = null;
        libOnline = false;
        rlExit.setVisibility(View.GONE);
        finish();
        break;
    }
  }

  // 发现新版本
  private void findNewVer(String result) {
    final AppBean appBean = getAppBeanFromString(result);

    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
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
          tvVersion.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_warn));
        }
      }

      @Override public void onNoUpdateAvailable() {
        if (tvVersion != null) {
          tvVersion.setText("已经是最新版");
          tvVersion.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_hint));
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
      sizeStr = DataCleanManager.getTotalCacheSize(mContext);
      tvCache.setHint(sizeStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 清空缓存dialog
  public void clearAlertDialog() {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
    builder.setTitle("提示");
    builder.setMessage("确定要清除缓存吗？");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();

        DataCleanManager.clearAllCache(mContext);
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