package org.jssvc.lib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
 * 用户登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnGetInfo)
    Button btnGetInfo;
    @BindView(R.id.textView)
    TextView textView;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void launchForResult(Activity activity) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // 设置登录信息
        String userid = "157301241";
        String pwd = "157301241";
        String type = "cert_no";

        saveLoginAccoundNumber(context, userid);
        saveLoginAccoundPwd(context, pwd);
        saveLoginType(context, type);

        textView.setText("账号：" + getLogonAccoundNumber(context) + "   " + "密码：" + getLogonAccoundPwd(context));
    }

    @OnClick({R.id.tvBack, R.id.btnGetInfo, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnLogin:
                // 登陆
                OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
                        .tag(this)
                        .params("number", getLogonAccoundNumber(context))
                        .params("passwd", getLogonAccoundPwd(context))
                        .params("select", getLogonType(context))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                // s 即为所需要的结果
                                textView.setText(s);
                            }
                        });
                break;
            case R.id.btnGetInfo:
                // 获取详情
                OkGo.post(HttpUrlParams.URL_LIB_ACCOUND)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                // s 即为所需要的结果
                                textView.setText(s);
                            }
                        });
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
