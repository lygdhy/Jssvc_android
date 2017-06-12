package org.jssvc.lib.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
import okhttp3.Call;
import okhttp3.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
  @BindView(R.id.btn_login) Button btnLogin;
  @BindView(R.id.edt_username) EditText edtUsername;
  @BindView(R.id.edt_pwd) EditText edtPwd;

  @Override protected int getContentViewId() {
    return R.layout.activity_login;
  }

  @Override protected void initView() {
    initLoginType();
  }

  private void initLoginType() {
  }

  @Override public void onResume() {
    super.onResume();
    edtUsername.setText(AccountPref.getLogonAccoundNumber(context));
    edtPwd.setText(AccountPref.getLogonAccoundPwd(context));
  }

  @OnClick({ R.id.tv_back, R.id.tv_register, R.id.btnLogin }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_register:
        // 账户注册
        startActivity(new Intent(context, RegisterActivity.class));
        break;
      case R.id.btnLogin:
        // 登录
        String loginname = edtUsername.getText().toString().trim();
        final String loginpwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
          showToast("登录信息不能为空");
        } else {
          showProgressDialog("登录中...");

          doLogin(loginname, loginpwd);
        }
        break;
    }
  }

  // 账户登录
  private void doLogin(String loginname, final String loginpwd) {
    OkGo.post(HttpUrlParams.URL_USER_LOGIN)
        .tag(this)
        .params("number", loginname)
        .params("passwd", loginpwd)
        .execute(new StringCallback() {
          @Override public void onSuccess(String s, Call call, Response response) {
            dissmissProgressDialog();
            // s 即为所需要的结果
            parseHtml(s, loginpwd);
          }

          @Override public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            dissmissProgressDialog();
            dealNetError(e);
          }
        });
  }

  // 解析网页
  private void parseHtml(String s, String loginpwd) {
    String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 保存密码
      AccountPref.saveLoginAccoundPwd(context, loginpwd);
      // 账号统计
      MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(),
          AccountPref.getLogonAccoundNumber(context));
      // 登录成功
      finish();
    } else {
      // 有错误提示
      if (errorMsg.contains("认证失败")) {
        // “如果认证失败，您将不能使用我的图书馆功能”
        startActivity(new Intent(context, AccountActivateActivity.class));
      } else {
        showToast(errorMsg);
        AccountPref.removeLogonAccoundPwd(context);
      }
    }
  }
}
