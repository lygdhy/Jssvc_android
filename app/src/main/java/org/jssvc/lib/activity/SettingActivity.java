package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tvBack, R.id.tvMsg, R.id.tvVersion, R.id.tvFeedback, R.id.tvAbout})
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
                break;
            case R.id.tvAbout:
                // 关于我们
                startActivity(new Intent(context, AboutActivity.class));
                break;
        }
    }
}
