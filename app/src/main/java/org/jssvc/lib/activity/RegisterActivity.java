package org.jssvc.lib.activity;

import android.content.Intent;
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
import org.jssvc.lib.utils.HtmlParseUtils;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 账户激活-身份验证
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.edtRealName)
    EditText edtRealName;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
//        AccountPref.getLogonAccoundNumber(context)
    }

    @OnClick({R.id.tvBack, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnRegister:
                // 激活
                String realname = edtRealName.getText().toString().trim();
                if (!TextUtils.isEmpty(realname)) {
                    showProgressDialog("正在提交...");

                    OkGo.post(HttpUrlParams.URL_LIB_USER_REGISTER)
                            .tag(this)
                            .params("name", realname)
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
            // 身份验证成功
            Intent intent = new Intent(context, ResetPwdActivity.class);
            intent.putExtra("onlyReset", true);
            startActivity(intent);
        } else {
            // 有错误提示
            showToast(errorMsg);
        }
    }

}
