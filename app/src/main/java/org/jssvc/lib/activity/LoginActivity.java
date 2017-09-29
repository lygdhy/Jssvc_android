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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.DataSup;
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
          doLogin(loginname, loginpwd);
        }
        break;
    }
  }

  // //0注册1找回密码
  private void goAccountReset(int code) {
    Intent intent = new Intent(mContext, AccountPlatformManagerActivity.class);
    intent.putExtra(AccountPlatformManagerActivity.ARG_OPT_CODE, code);//0注册1找回密码
    startActivity(intent);
  }

  // 账户登录
  private void doLogin(String loginname, String loginpwd) {
    OkGo.<String>post(HttpUrlParams.URL_USER_LOGIN).tag(this)
        .params("number", loginname)
        .params("passwd", MD5(loginpwd))
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                JSONObject jo = jsonObject.optJSONObject("data");
                DataSup.setMemberStr2Local(jo.toString());
                finish();
              } else {
                showToast(jsonObject.optString("message"));
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("登录中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // MD5加密
  private static String MD5(String sourceStr) {
    String result = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(sourceStr.getBytes());
      byte b[] = md.digest();
      int i;
      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0) i += 256;
        if (i < 16) buf.append("0");
        buf.append(Integer.toHexString(i));
      }
      result = buf.toString();
      System.out.println("MD5(" + sourceStr + ",32) = " + result);
      System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
    } catch (NoSuchAlgorithmException e) {
      System.out.println(e);
    }
    return result;
  }
}
