package org.jssvc.lib.activity;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MsgBean;

/**
 * 新闻详情
 */
public class MsgBrowseActivity extends BaseActivity {
  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.webView) BridgeWebView webView;

  @Override protected int getContentViewId() {
    return R.layout.activity_msg_browse;
  }

  @Override protected void initView() {
    MsgBean item = (MsgBean) getIntent().getSerializableExtra("item");
  }

  @OnClick({ R.id.tvBack }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
    }
  }
}
