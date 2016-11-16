package org.jssvc.lib.activity;

import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的债务
 */
public class MyDebtActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_debt;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
