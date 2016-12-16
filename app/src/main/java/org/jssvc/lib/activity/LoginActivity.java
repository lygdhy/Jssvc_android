package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtPwd)
    EditText edtPwd;
    @BindView(R.id.tvLoginType)
    TextView tvLoginType;

    String[] loginTypes = new String[]{"证件号", "条码号", "Email"};
    String[] loginTypesCode = new String[]{"cert_no", "bar_no", "email"};
    int loginTypePos = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onResume() {
        super.onResume();
        edtName.setText(AccountPref.getLogonAccoundNumber(context));
        edtPwd.setText(AccountPref.getLogonAccoundPwd(context));
    }

    @OnClick({R.id.tvBack, R.id.tvLoginType, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvLoginType:
                // 登陆方式
                showLoginTypeDialog();
                break;
            case R.id.btnLogin:
                // 登陆
                String loginname = edtName.getText().toString().trim();
                String loginpwd = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
                    showToast("登陆信息不能为空");
                } else {
                    showProgressDialog("登录中...");

                    AccountPref.saveLoginAccoundNumber(context, loginname);
                    AccountPref.saveLoginAccoundPwd(context, loginpwd);
                    AccountPref.saveLoginType(context, loginTypesCode[loginTypePos]);
                    OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
                            .tag(this)
                            .params("number", loginname)
                            .params("passwd", loginpwd)
                            .params("select", loginTypesCode[loginTypePos])
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    dissmissProgressDialog();
                                    // s 即为所需要的结果
                                    parseHtml(s);
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    dissmissProgressDialog();
                                    dealNetError(e);
                                }

                            });
                }
                break;
        }
    }

    // 解析网页
    private void parseHtml(String s) {
        String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
        if (TextUtils.isEmpty(errorMsg)) {
            // 账号统计
            MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(), AccountPref.getLogonAccoundNumber(context));
            // 登陆成功
            finish();
        } else {
            // 有错误提示
            if (errorMsg.contains("认证失败")) {
                // “如果认证失败，您将不能使用我的图书馆功能”
                startActivity(new Intent(context, RegisterActivity.class));
            } else {
                showToast(errorMsg);
                AccountPref.removeLogonAccoundPwd(context);
            }
        }
    }

    // 登陆方式选择
    private void showLoginTypeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("请选择登陆方式")
                .setSingleChoiceItems(loginTypes, loginTypePos,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                loginTypePos = which;
                                tvLoginType.setText(loginTypes[loginTypePos]);
                                edtName.setHint("请输入" + loginTypes[loginTypePos]);
                                dialog.dismiss();
                            }
                        }
                ).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
