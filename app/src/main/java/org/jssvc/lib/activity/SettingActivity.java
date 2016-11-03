package org.jssvc.lib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.btnExit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        // 隐藏/显示登出按钮
        if (AccountPref.isLogon(context)) {
            btnExit.setVisibility(View.VISIBLE);
        } else {
            btnExit.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.tvBack, R.id.tvMsg, R.id.tvVersion, R.id.tvFeedback, R.id.tvAbout, R.id.btnExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvMsg:
                // 活动通知
                break;
            case R.id.tvVersion:
                // 版本升级
                break;
            case R.id.tvFeedback:
                // 意见反馈
                // 手机QQ com.tencent.mqq
                // 手机QQ2012 com.tencent.mobileqq
                // QQ轻聊版 com.tencent.qqlite
                // QQ国际版 com.tencent.mobileqqi
                // QQHD com.tencent.minihd.qq
                // 企业QQ com.tencent.eim
                if (AppUtils.isInstallApp(context, "com.tencent.mqq") ||
                        AppUtils.isInstallApp(context, "com.tencent.mobileqq") ||
                        AppUtils.isInstallApp(context, "com.tencent.qqlite") ||
                        AppUtils.isInstallApp(context, "com.tencent.mobileqqi") ||
                        AppUtils.isInstallApp(context, "com.tencent.minihd.qq") ||
                        AppUtils.isInstallApp(context, "com.tencent.eim")) {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=149553453";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    showToast("暂未安装QQ相关软件无法发起聊天");
                }
                break;
            case R.id.tvAbout:
                // 关于我们
                startActivity(new Intent(context, AboutActivity.class));
                break;
            case R.id.btnExit:
                // 登出
                AccountPref.removeLogonAccoundPwd(context);
                finish();
                break;
        }
    }
}
