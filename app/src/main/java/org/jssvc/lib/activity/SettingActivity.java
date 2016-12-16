package org.jssvc.lib.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.utils.DataCleanManager;
import org.jssvc.lib.view.CustomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.pgyersdk.update.UpdateManagerListener.getAppBeanFromString;
import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

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

    @BindView(R.id.rlPush)
    RelativeLayout rlPush;
    @BindView(R.id.rlPwd)
    RelativeLayout rlPwd;
    @BindView(R.id.rlShare)
    RelativeLayout rlShare;

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

//        rlPush.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountPref.isLogon(context)) {
            // 用户名和密码都在
            btnExit.setVisibility(View.VISIBLE);
            btnExit.setText("注销" + AccountPref.getLogonAccoundNumber(context));

            rlPwd.setVisibility(View.VISIBLE);
        } else {
            btnExit.setVisibility(View.GONE);
            rlPwd.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tvBack, R.id.rlCheck, R.id.rlClear, R.id.rlFeedback, R.id.rlAbout, R.id.btnExit, R.id.rlPwd, R.id.rlShare})
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
                        findNewVer(result);
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        showToast("已经是最新版");
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
            case R.id.rlPwd:
                // 修改密码
                startActivity(new Intent(context, ResetPwdActivity.class));
                break;
            case R.id.rlShare:
                // 分享APP
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("text/plain");//设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "掌上图书馆+");//添加分享内容标题
                share_intent.putExtra(Intent.EXTRA_TEXT, "掌上图书馆+下载地址 https://www.pgyer.com/jssvclib2");//添加分享内容
                share_intent = Intent.createChooser(share_intent, "分享");
                startActivity(share_intent);
                break;
            case R.id.btnExit:
                // 注销
                if (AccountPref.isLogon(context)) {
                    AccountPref.removeLogonAccoundPwd(context);
                    btnExit.setVisibility(View.GONE);
                    // 账号统计
                    MobclickAgent.onProfileSignOff();
                }
                break;
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
                Acp.getInstance(context).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                dialog.dismiss();
                                startDownloadTask(SettingActivity.this, appBean.getDownloadURL());
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                showToast(permissions.toString() + "权限拒绝");
                            }
                        });
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
        PgyUpdateManager.register(SettingActivity.this, new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
                if (tvCheck != null) {
                    tvCheck.setText("发现新版本");
                    tvCheck.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }

            @Override
            public void onNoUpdateAvailable() {
                if (tvCheck != null) {
                    tvCheck.setText("已经是最新版");
                    tvCheck.setTextColor(ContextCompat.getColor(context, R.color.ui_text_tip));
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
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定要清除缓存吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();

                barClear.setVisibility(View.VISIBLE);
                tvClear.setVisibility(View.GONE);

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