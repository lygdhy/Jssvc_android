package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountResetActivity;
import org.jssvc.lib.base.BaseFragment;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 账号验证（用于注册和找回密码）
 *     version: 1.0
 * </pre>
 */
public class AccountPhoneCheckFragment extends BaseFragment {

  @BindView(R.id.edt_username) EditText edtUsername;

  int opt_code = 0;//0注册1找回密码

  public AccountPhoneCheckFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_account_phone_check;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments().containsKey(AccountResetActivity.ARG_OPT_CODE)) {
      opt_code = getArguments().getInt(AccountResetActivity.ARG_OPT_CODE);
    }
  }

  @Override protected void initView() {

  }

  @OnClick({ R.id.btn_next, R.id.tv_protocol }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_next:// 下一步
        String phone = edtUsername.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
          showToast("请先输入手机号码");
        } else {
          // 验证
          showToast("===验证号码===");
          AccountResetActivity activity = (AccountResetActivity) getActivity();
          activity.resetPwdFragment(phone);
        }
        break;
      case R.id.tv_protocol:// 阅读协议
        showToast("===阅读协议===");
        break;
    }
  }
}
