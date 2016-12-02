package org.jssvc.lib.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 帮助中心
 */
public class HelpDetailsActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_help_details;
    }

    @Override
    protected void initView() {
        String c = getIntent().getStringExtra("c");
        String q = getIntent().getStringExtra("q");
        String a = getIntent().getStringExtra("a");
        String t = getIntent().getStringExtra("t");

        tvTitle.setText(c);
        textView1.setText(q);
        textView2.setText(a);

        if (TextUtils.isEmpty(t)) {
            textView3.setVisibility(View.GONE);
        } else {
            textView3.setVisibility(View.VISIBLE);
            textView3.setText(t);
        }
    }

    @OnClick({R.id.tvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }
}
