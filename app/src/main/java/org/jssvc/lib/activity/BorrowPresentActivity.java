package org.jssvc.lib.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 当前借阅
 */
public class BorrowPresentActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_present);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
