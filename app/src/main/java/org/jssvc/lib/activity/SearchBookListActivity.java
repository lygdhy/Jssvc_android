package org.jssvc.lib.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜书
 */
public class SearchBookListActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvBack)
    public void onClick() {
        finish();
    }
}
