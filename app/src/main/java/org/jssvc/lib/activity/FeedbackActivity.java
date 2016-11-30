package org.jssvc.lib.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.utils.AppUtils;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvLibSer1)
    Button tvLibSer1;
    @BindView(R.id.tvLibSer2)
    Button tvLibSer2;
    @BindView(R.id.tvLibSer3)
    Button tvLibSer3;
    @BindView(R.id.tvLibSer4)
    Button tvLibSer4;
    @BindView(R.id.tvOrgSer1)
    Button tvOrgSer1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tvBack, R.id.tvLibSer1, R.id.tvLibSer2, R.id.tvLibSer3, R.id.tvLibSer4, R.id.tvOrgSer1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvLibSer1:
                callQQCell("1872237872");
                break;
            case R.id.tvLibSer2:
                callQQCell("893196521");
                break;
            case R.id.tvLibSer3:
                callQQCell("897457690");
                break;
            case R.id.tvLibSer4:
                callQQCell("149553453");
                break;
            case R.id.tvOrgSer1:
                callQQCell("2906501168");
                break;
        }
    }

    private void callQQCell(String qqNum) {
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
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            showToast("暂未安装QQ相关软件无法发起聊天");
        }
    }
}
