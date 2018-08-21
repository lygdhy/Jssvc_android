package org.jssvc.lib.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.UserBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.tv_back, R.id.tv_register, R.id.tv_forget, R.id.btn_login})
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

    // 0注册1找回密码
    private void goAccountReset(int code) {
        Intent intent = new Intent(mContext, RegisterActivity.class);
        intent.putExtra(RegisterActivity.ARG_OPT_CODE, code);//0注册1找回密码
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == 100) {
                // 注册/改密后自动登录
                doLogin(data.getStringExtra("phone"), data.getStringExtra("pwd"));
            }
        }
    }

    // 账户登录
    private void doLogin(String loginname, String loginpwd) {
        OkGo.<String>post(HttpUrlParams.URL_USER_LOGIN).tag(this)
                .params("number", loginname)
                .params("passwd", MD5Utils.MD5(loginpwd))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                JSONObject jo = jsonObject.optJSONObject("data");
                                DataSup.saveUserJson2Local(jo.toString());

                                // 加载第三方账户
                                UserBean bean = new Gson().fromJson(jo.toString(), UserBean.class);
                                getUserAcount(bean.getId());
                            } else {
                                showToast(jsonObject.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dealNetError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("登录中...");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                    }
                });
    }

    // 获取当前用户绑定的第三方账户信息
    private void getUserAcount(String uid) {
        OkGo.<String>post(HttpUrlParams.GET_THIRD_ACCOUNTS).tag(this)
                .params("uid", uid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                DataSup.saveThirdAccountJson2Local(jsonObject.optString("data"));
                            } else {
                                // showToast(jsonObject.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dealNetError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("账户初始化...");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                        // 登录后获取第三方账户，不论成功失败均finish
                        finish();
                    }
                });
    }
}
