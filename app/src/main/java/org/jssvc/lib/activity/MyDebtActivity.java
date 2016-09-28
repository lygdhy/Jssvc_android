package org.jssvc.lib.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的债务
 */
public class MyDebtActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_debt);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
