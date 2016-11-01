package org.jssvc.lib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.HttpUrlParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static org.jssvc.lib.data.AccountPref.getLogonAccoundNumber;
import static org.jssvc.lib.data.AccountPref.getLogonAccoundPwd;
import static org.jssvc.lib.data.AccountPref.getLogonType;
import static org.jssvc.lib.data.AccountPref.saveLoginAccoundNumber;
import static org.jssvc.lib.data.AccountPref.saveLoginAccoundPwd;
import static org.jssvc.lib.data.AccountPref.saveLoginType;

/**
 * 用户登陆
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
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        // 设置登陆信息
//        String userid = "157301241";
//        String pwd = "157301241";
        String type = "cert_no";
//
//        saveLoginAccoundNumber(context, userid);
//        saveLoginAccoundPwd(context, pwd);
        saveLoginType(context, type);

        edtName.setText(getLogonAccoundNumber(context));
        edtPwd.setText(getLogonAccoundPwd(context));
    }

    @OnClick({R.id.tvBack, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnLogin:
                // 登陆
                String loginname = edtName.getText().toString().trim();
                String loginpwd = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
                    showToast("登陆信息不能为空");
                } else {
                    saveLoginAccoundNumber(context, loginname);
                    saveLoginAccoundPwd(context, loginpwd);

                    OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
                            .tag(this)
                            .params("number", loginname)
                            .params("passwd", loginpwd)
                            .params("select", getLogonType(context))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    // s 即为所需要的结果
                                    finish();
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                }

                            });
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
