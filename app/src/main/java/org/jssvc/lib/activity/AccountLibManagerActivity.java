package org.jssvc.lib.activity;

import android.view.View;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.LibAccountActivateFragment;
import org.jssvc.lib.fragment.LibAccountBoundFragment;
import org.jssvc.lib.fragment.LibAccountResetPwdFragment;

/**
 * Lib账户管理AccountLibManagerActivity
 * 1、LibAccountBoundFragment  帐号绑定
 * 2、LibAccountActivateFragment 	账户激活
 * 3、LibAccountResetPwdFragment 账户修改密码
 */
public class AccountLibManagerActivity extends BaseActivity {

  public String school = "";
  public String name = "";
  public String oldPwd = "";
  public String type = "";

  @Override protected int getContentViewId() {
    return R.layout.activity_account_lib_manager;
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

  /**
   * 图书馆帐号绑定页面
   */
  private void initAccountBoundFragment() {
    LibAccountBoundFragment myFragment = new LibAccountBoundFragment();
    getSupportFragmentManager().beginTransaction().add(R.id.main_container, myFragment).commit();
  }

  /**
   * 图书馆账户激活页面
   */
  public void accountActivateFragment() {
    LibAccountActivateFragment myFragment = new LibAccountActivateFragment();
    getSupportFragmentManager().beginTransaction()
        .add(R.id.main_container, myFragment)
        .addToBackStack(null)
        .commit();
  }

  /**
   * 图书馆账户修改密码
   */
  public void accountResetPwdFragment() {
    LibAccountResetPwdFragment myFragment = new LibAccountResetPwdFragment();
    getSupportFragmentManager().beginTransaction()
        .add(R.id.main_container, myFragment)
        .addToBackStack(null)
        .commit();
  }
}
