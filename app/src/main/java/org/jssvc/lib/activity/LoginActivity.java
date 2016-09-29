package org.jssvc.lib.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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

    String url = "http://opac.jssvc.edu.cn:8080/reader/redr_verify.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        textView.setText(url);
    }

    @OnClick({R.id.tvBack, R.id.btnGetInfo, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnLogin:
                // 登陆
                OkHttpUtils.post().url(url)
                        .addParams("number", "157301241")
                        .addParams("passwd", "157301241")
                        .addParams("select", "cert_no")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                textView.setText(response);
                            }
                        });
                break;
            case R.id.btnGetInfo:
                // 获取详情
                String url2 = "http://opac.jssvc.edu.cn:8080/reader/redr_info.php";
                textView.setText(url2);
                OkHttpUtils.post().url(url2)
//                .addParams("number", "157301241")
//                .addParams("passwd", "157301241")
//                .addParams("select", "cert_no")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                textView.setText(response);
                            }
                        });
                break;
        }
    }
}
