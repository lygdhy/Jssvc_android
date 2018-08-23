package org.jssvc.lib.ui.login.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.ui.login.bean.EventSms;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.MD5Utils;
import org.jssvc.lib.view.TimeCountDown;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 平台账号重置密码（用于注册和找回密码）
 *     version: 1.0
 * </pre>
 */
public class ResetPwdFragment extends BaseFragment
        implements TimeCountDown.OnTimerCountDownListener {

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.code_view)
    View codeView;
    @BindView(R.id.code_layout)
    RelativeLayout codeLayout;
    @BindView(R.id.btn_count_down)
    TimeCountDown btnCountDown;

    int opt_code = 0;
    String opt_phone = "";
    String str_pwd = "";
    boolean is_smart = false;

    public static ResetPwdFragment newInstance(int opt_code) {
        ResetPwdFragment fragment = new ResetPwdFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("opt_code", opt_code);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ResetPwdFragment() {
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_reset_pwd;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opt_code = getArguments().getInt("opt_code");
    }

    public void postKeyValue(String opt_phone, boolean is_smart) {
        this.opt_phone = opt_phone;
        this.is_smart = is_smart;

        edtPhone.setText(opt_phone);
        edtPhone.setEnabled(false);

        // 智能模式下无需输入验证码
        if (is_smart) {
            // 免验证码
            btnCountDown.setVisibility(View.GONE);
            codeView.setVisibility(View.GONE);
            codeLayout.setVisibility(View.GONE);
            edtPwd.setFocusable(true);
        } else {
            btnCountDown.setVisibility(View.VISIBLE);
            codeView.setVisibility(View.VISIBLE);
            codeLayout.setVisibility(View.VISIBLE);
            btnCountDown.setOnTimerCountDownListener(this);// 初始化倒计时
            btnCountDown.initTimer();
        }
    }

    // 通过验证
    public void passCheck() {
        btnCountDown.setVisibility(View.GONE);
        doNext();
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String code = edtCode.getText().toString().trim();
                String pwd = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    showToast("密码不能为空");
                    break;
                }
                str_pwd = pwd;

                // 智能模式下无需输入验证码
                if (is_smart) {
                    doNext();
                } else {
                    if (TextUtils.isEmpty(code)) {
                        showToast("验证码不能为空");
                    } else {
                        // 短信验证
                        EventBus.getDefault().post(new EventSms("check", code));
                    }
                }
                break;
        }
    }


    public void doNext() {
        // 修改或添加用户 15396986298
        // opt_code //0注册1找回密码
        OkGo.<String>post(HttpUrlParams.URL_USER_REGISTER).tag(this)
                .params("phone", opt_phone)
                .params("pwd", MD5Utils.MD5(str_pwd))
                .params("type", (opt_code == 0) ? "reg" : "bac")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                Intent intent = new Intent();
                                intent.putExtra("phone", opt_phone);
                                intent.putExtra("pwd", str_pwd);
                                getActivity().setResult(RESULT_OK, intent);
                                getActivity().finish();
                            }
                            showToast(jsonObject.optString("message"));
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
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btnCountDown != null) btnCountDown.cancel();
    }

    @Override
    public void onCountDownStart() {
        Log.d("DHY", "onCountDownStart");
    }

    @Override
    public void onCountDownLoading(int currentCount) {
        Log.d("DHY", "currentCount:" + currentCount);
    }

    @Override
    public void onCountDownError() {
        Log.d("DHY", "onCountDownError");
        if (btnCountDown != null) btnCountDown.setText("点击重试");
    }

    @Override
    public void onCountDownFinish() {
        Log.d("DHY", "onCountDownFinish");
        if (btnCountDown != null) btnCountDown.setText("重新发送");
    }
}
