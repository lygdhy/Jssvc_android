package org.jssvc.lib.activity;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * 消息中心
 */
public class MsgActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvBody)
    TextView tvBody;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initView() {

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