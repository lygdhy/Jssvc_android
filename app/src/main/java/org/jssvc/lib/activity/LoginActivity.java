package org.jssvc.lib.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.HttpUrlParams;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
  @BindView(R.id.edt_username) EditText edtUsername;
  @BindView(R.id.edt_pwd) EditText edtPwd;

  @Override protected int getContentViewId() {
    return R.layout.activity_login;
  }

  @Override protected void initView() {
  }

  @OnClick({ R.id.tv_back, R.id.tv_register, R.id.tv_forget, R.id.btn_login })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_register:// 账户注册
        goAccountReset(0);
        break;
      case R.id.tv_forget:// 忘记密码
        goAccountReset(1);
        break;
      case R.id.btn_login:// 登录
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

  // //0注册1找回密码
  private void goAccountReset(int code) {
    Intent intent = new Intent(context, AccountPlatformManagerActivity.class);
    intent.putExtra(AccountPlatformManagerActivity.ARG_OPT_CODE, code);//0注册1找回密码
    startActivity(intent);
  }

  // 账户登录
  private void doLogin(String loginname, String loginpwd) {
    OkGo.<String>post(HttpUrlParams.URL_USER_LOGIN).tag(this)
        .params("number", loginname)
        .params("passwd", loginpwd)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            showToast(response.body());
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog();
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });

    //OkGo.post(HttpUrlParams.URL_USER_LOGIN)
    //    .tag(this)
    //    .params("number", loginname)
    //    .params("passwd", loginpwd)
    //    .execute(new StringCallback() {
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        dissmissProgressDialog();
    //        // s 即为所需要的结果
    //        showToast(s);
    //      }
    //
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dissmissProgressDialog();
    //        dealNetError(e);
    //      }
    //    });
  }
}
