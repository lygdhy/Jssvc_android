package org.jssvc.lib.activity;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.AppUtils;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.tvVer) TextView tvVer;

  @Override protected int getContentViewId() {
    return R.layout.activity_about;
  }

  @Override protected void initView() {
    tvVer.setText("Ver " + AppUtils.getAppVersionName());
  }

  @OnClick({ R.id.tvBack }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
    }
  }
}