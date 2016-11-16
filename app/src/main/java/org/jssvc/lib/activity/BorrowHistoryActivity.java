package org.jssvc.lib.activity;

import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 借阅历史
 */
public class BorrowHistoryActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_borrow_history;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
