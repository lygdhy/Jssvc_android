package org.jssvc.lib.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.PlatformAccountPhoneCheckFragment;
import org.jssvc.lib.fragment.PlatformAccountResetPwdFragment;

/**
 * 平台账户管理AccountPlatformManagerActivity
 * 1、PlatformAccountPhoneCheckFragment	手机号码验证
 * 2、PlatformAccountResetPwdFragment	账户找回/注册
 */
public class AccountPlatformManagerActivity extends BaseActivity {
  public static final String ARG_OPT_CODE = "opt_code";
  public static final String ARG_OPT_PHONE = "opt_phone";

  @BindView(R.id.tv_title) TextView tvTitle;

  int opt_code = 0;//0注册1找回密码

  @Override protected int getContentViewId() {
    return R.layout.activity_account_platform_manager;
  }

  @Override protected void initView() {
    opt_code = getIntent().getIntExtra("opt_code", 0);
    if (opt_code == 0) tvTitle.setText("快速注册");
    if (opt_code == 1) tvTitle.setText("重置密码");

    initPhoneCheckFragment();
  }

  @OnClick({ R.id.tv_back }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
    }
  }

  /**
   * 输入手机号码页面
   */
  public void initPhoneCheckFragment() {
    Bundle arguments = new Bundle();
    arguments.putInt(ARG_OPT_CODE, opt_code);
    PlatformAccountPhoneCheckFragment myFragment = new PlatformAccountPhoneCheckFragment();
    myFragment.setArguments(arguments);
    getSupportFragmentManager().beginTransaction().add(R.id.main_container, myFragment).commit();
  }

  /**
   * 重置密码页面
   */
  public void resetPwdFragment(String phone) {
    Bundle arguments = new Bundle();
    arguments.putInt(ARG_OPT_CODE, opt_code);
    arguments.putString(ARG_OPT_PHONE, phone);
    PlatformAccountResetPwdFragment myFragment = new PlatformAccountResetPwdFragment();
    myFragment.setArguments(arguments);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.main_container, myFragment)
        .addToBackStack(null)
        .commit();
  }
}
