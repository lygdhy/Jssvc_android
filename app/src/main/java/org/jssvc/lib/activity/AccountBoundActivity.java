package org.jssvc.lib.activity;

import android.view.View;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.AccountBoundFragment;

/**
 * 账户绑定
 */
public class AccountBoundActivity extends BaseActivity {

  @Override protected int getContentViewId() {
    return R.layout.activity_account_bound;
  }

  @Override protected void initView() {
    initAccountBoundFragment();
  }

  @OnClick({ R.id.tv_back }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
    }
  }

  private void initAccountBoundFragment() {
    AccountBoundFragment myFragment = new AccountBoundFragment();
    getSupportFragmentManager().beginTransaction().add(R.id.main_container, myFragment).commit();
  }
}
