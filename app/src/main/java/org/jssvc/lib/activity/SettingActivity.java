package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.utils.DataCleanManager;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.rlCheck)
    RelativeLayout rlCheck;
    @BindView(R.id.rlClear)
    RelativeLayout rlClear;
    @BindView(R.id.rlFeedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.rlAbout)
    RelativeLayout rlAbout;
    @BindView(R.id.btnExit)
    Button btnExit;
    @BindView(R.id.tvCheck)
    TextView tvCheck;
    @BindView(R.id.barClear)
    ProgressBar barClear;
    @BindView(R.id.tvClear)
    TextView tvClear;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        setCurrentRubbish();
        autoUpdateCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccountPref.isLogon(context)) {
            // 用户名和密码都在
            btnExit.setVisibility(View.VISIBLE);
            btnExit.setText("注销" + AccountPref.getLogonAccoundNumber(context));
        } else {
            btnExit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tvBack, R.id.rlCheck, R.id.rlClear, R.id.rlFeedback, R.id.rlAbout, R.id.btnExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.rlCheck:
                // 版本检查
                PgyUpdateManager.register(SettingActivity.this, new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        tvCheck.setText("发现新版本");
                        tvCheck.setTextColor(ContextCompat.getColor(context, R.color.red));

                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(SettingActivity.this)
                                .setTitle("更新")
                                .setMessage(appBean.getReleaseNote())
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadTask(SettingActivity.this, appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        showToast("已经是最新版");
                        tvCheck.setText("已经是最新版");
                        tvCheck.setTextColor(ContextCompat.getColor(context, R.color.ui_text_tip));
                    }
                });
                break;
            case R.id.rlClear:
                // 清除缓存
                clearAlertDialog();
                break;
            case R.id.rlFeedback:
                // 意见反馈
                startActivity(new Intent(context, FeedbackActivity.class));
                break;
            case R.id.rlAbout:
                // 关于我们
                startActivity(new Intent(context, AboutActivity.class));
                break;
            case R.id.btnExit:
                // 注销
                if (AccountPref.isLogon(context)) {
                    AccountPref.removeLogonAccoundPwd(context);
                    btnExit.setVisibility(View.GONE);
                }
                break;
        }
    }

    // 版本自动检查
    private void autoUpdateCheck() {
        PgyUpdateManager.register(SettingActivity.this, new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
                tvCheck.setText("发现新版本");
                tvCheck.setTextColor(ContextCompat.getColor(context, R.color.red));
            }

            @Override
            public void onNoUpdateAvailable() {
                tvCheck.setText("已经是最新版");
                tvCheck.setTextColor(ContextCompat.getColor(context, R.color.ui_text_tip));
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
            barClear.setVisibility(View.GONE);
            tvClear.setVisibility(View.VISIBLE);
            sizeStr = DataCleanManager.getTotalCacheSize(getApplicationContext());
            tvClear.setText(sizeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 清空缓存dialog
    public void clearAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//, AlertDialog.THEME_HOLO_LIGHT
        builder.setTitle("提示");
        builder.setMessage("确定要清除缓存吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

                barClear.setVisibility(View.VISIBLE);
                tvClear.setVisibility(View.GONE);

                DataCleanManager.clearAllCache(getApplicationContext());
                showToast("清理中...");
                Handler x = new Handler();
                x.postDelayed(new splashhandler(), 3000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}