package org.jssvc.lib.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * 分享APP
 */
public class ShareActivity extends BaseActivity {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.tvShare) TextView tvShare;

  @Override protected int getContentViewId() {
    return R.layout.activity_share;
  }

  @Override protected void initView() {

  }

  @OnClick({ R.id.tvBack, R.id.tvShare }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
      case R.id.tvShare:
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name)
            + "下载地址 http://www.jssvc.org/download/libapp.html");//添加分享内容
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);
        break;
    }
  }
}
