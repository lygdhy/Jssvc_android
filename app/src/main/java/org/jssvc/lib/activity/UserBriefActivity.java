package org.jssvc.lib.activity;

import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.HttpUrlParams;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 读者档案
 */
public class UserBriefActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_brief;
    }

    @Override
    protected void initView() {
        // 加载用户数据
        OkGo.post(HttpUrlParams.URL_LIB_ACCOUND)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        textView.setText(s);
                    }
                });
    }

    @OnClick({R.id.tvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
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
