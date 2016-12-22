package org.jssvc.lib.activity;

import android.view.View;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的书架
 */
public class BookShelfActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvEdite)
    TextView tvEdite;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_shelf;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tvBack, R.id.tvEdite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvEdite:
                break;
        }
    }
}
