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

  @BindView(R.id.tv_version) TextView tvVersion;

  @Override protected int getContentViewId() {
    return R.layout.activity_about;
  }

  @Override protected void initView() {
    tvVersion.setText("Ver " + AppUtils.getAppVersionName());
  }

  @OnClick({ R.id.tv_back }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
    }
  }
}