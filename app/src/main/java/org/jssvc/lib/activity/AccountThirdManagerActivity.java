package org.jssvc.lib.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * 第三方帐号管理
 */
public class AccountThirdManagerActivity extends BaseActivity {

  @BindView(R.id.tv_lib_title) TextView tvLibTitle;
  @BindView(R.id.tv_lib_tip) TextView tvLibTip;

  @Override protected int getContentViewId() {
    return R.layout.activity_account_third_manager;
  }

  @Override protected void initView() {
    // 获取当前用户绑定的信息
    getUserAcount();
  }

  @OnClick({
      R.id.tv_back, R.id.rl_lib, R.id.rl_jw
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_lib:
        // 图书馆账户绑定
        startActivity(new Intent(mContext, AccountLibManagerActivity.class));
        break;
      case R.id.rl_jw:
        // 教务系统绑定
        break;
    }
  }

  // 获取当前用户绑定的第三方账户信息
  private void getUserAcount() {
    showToast("获取第三方账户信息");
  }
}
