package org.jssvc.lib.activity;

import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜书
 */
public class SearchBookListActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search_book_list;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
