package org.jssvc.lib.ui.account.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.ui.account.AccountLibManagerActivity;
import org.jssvc.lib.utils.HtmlParseUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 图书馆账户激活
 *     version: 1.0
 * </pre>
 */
public class LibAccountActivateFragment extends BaseFragment {

    @BindView(R.id.edt_real_name)
    EditText edtRealName;

    String school = "";
    String name = "";
    String pwd = "";
    String type = "";

    public LibAccountActivateFragment() {
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lib_account_activate;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            school = bundle.getString("school");
            name = bundle.getString("name");
            pwd = bundle.getString("pwd");
            type = bundle.getString("type");
        }
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String realName = edtRealName.getText().toString().trim();
                if (TextUtils.isEmpty(realName)) {
                    showToast("请输入您图书证上的姓名");
                } else {
                    checkRealName(realName);
                }
                break;
        }
    }

    // 验证真实姓名
    private void checkRealName(String realName) {
        OkGo.<String>post(HttpUrlParams.URL_LIB_USER_REGISTER).tag(this)
                .params("name", realName)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        parseHtml(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dealNetError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("正在验证...");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                    }
                });
    }

    // 解析网页
    private void parseHtml(String s) {
        String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
        if (TextUtils.isEmpty(errorMsg)) {
            // 身份验证成功，重置密码
            AccountLibManagerActivity activity = (AccountLibManagerActivity) getActivity();
            activity.accountResetPwdFragment();
        } else {
            // 有错误提示
            showToast(errorMsg);
        }
    }
}
