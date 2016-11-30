package org.jssvc.lib.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 账户激活2-重置密码
 */
public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.edtPwd)
    EditText edtPwd;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView() {
        btnRegister.setText("激活 " + AccountPref.getLogonAccoundNumber(context));
    }

    @OnClick({R.id.tvBack, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnRegister:
                // 激活
                String newpwd = edtPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(newpwd)) {
                    OkGo.post(HttpUrlParams.URL_LIB_CHANGE_PWD)
                            .tag(this)
                            .params("old_passwd", AccountPref.getLogonAccoundPwd(context))
                            .params("new_passwd", newpwd)
                            .params("chk_passwd", newpwd)
                            .params("submit1", "%E7%A1%AE%E5%AE%9A")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    // s 即为所需要的结果
                                    parseHtml(s);
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

    // 解析网页
    private void parseHtml(String s) {
        String errorMsg = HtmlParseUtils.getErrMsgOnChangePwd(s);
        if (TextUtils.isEmpty(errorMsg)) {
            // 密码修改成功
            showToast("密码修改成功");
            AccountPref.saveLoginAccoundPwd(context, edtPwd.getText().toString().trim());
            finish();
        } else {
            // 有错误提示
            showToast(errorMsg);
        }
    }
}
