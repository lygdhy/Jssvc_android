package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountPlatformManagerActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.view.TimeCountDown;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 平台账号重置密码（用于注册和找回密码）
 *     version: 1.0
 * </pre>
 */
public class PlatformAccountResetPwdFragment extends BaseFragment
    implements TimeCountDown.OnTimerCountDownListener {

  @BindView(R.id.edt_phone) EditText edtPhone;
  @BindView(R.id.edt_code) EditText edtCode;
  @BindView(R.id.edt_pwd) EditText edtPwd;
  @BindView(R.id.btn_submit) Button btnSubmit;
  @BindView(R.id.btn_count_down) TimeCountDown btnCountDown;

  int opt_code = 0;//0注册1找回密码
  String opt_phone = "";

  public PlatformAccountResetPwdFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_platform_account_reset_pwd;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments().containsKey(AccountPlatformManagerActivity.ARG_OPT_CODE)) {
      opt_code = getArguments().getInt(AccountPlatformManagerActivity.ARG_OPT_CODE);
      opt_phone = getArguments().getString(AccountPlatformManagerActivity.ARG_OPT_PHONE);
    }
  }

  @Override protected void initView() {
    if (opt_code == 0) btnSubmit.setText("立即注册");
    if (opt_code == 1) btnSubmit.setText("重置密码");
    edtPhone.setText(opt_phone);

    btnCountDown.setOnTimerCountDownListener(this);
    btnCountDown.initTimer();
  }

  @OnClick({ R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_submit:
        String code = edtCode.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd)) {
          showToast("验证码和密码不能为空");
        } else {
          showToast("===提及服务===");
        }
        break;
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (btnCountDown != null) btnCountDown.cancel();
  }

  @Override public void onCountDownStart() {
    Log.d("DHY", "onCountDownStart");
    showToast("===获取短信验证码===");
  }

  @Override public void onCountDownLoading(int currentCount) {
    Log.d("DHY", "currentCount:" + currentCount);
  }

  @Override public void onCountDownError() {
    Log.d("DHY", "onCountDownError");
    if (btnCountDown != null) btnCountDown.setText("点击重试");
  }

  @Override public void onCountDownFinish() {
    Log.d("DHY", "onCountDownFinish");
    if (btnCountDown != null) btnCountDown.setText("重新发送");
  }
}
