package org.jssvc.lib.module.login;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import org.jssvc.lib.R;
import org.jssvc.lib.base.activity.BaseActivity;
import org.jssvc.lib.view.TitleView;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

/**
 * Created by jjj on 2018/1/31.
 *
 * @description:
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter presenter;
    @BindView(R.id.tv_forget)
    TextView tvForget;

    @Override
    public void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);
        AndroidInjection.inject(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initTitle(TitleView titleView) {
        titleView.setTitleView("登录");
        titleView.setRightTextView("注册");
    }

    @Override
    public void initView() {
        DaggerLoginComponent.builder()
                .loginModule(new LoginModule( this))
                .build()
                .inject(this);
        presenter.login();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onLoginSuccess(String msg) {
        LogUtils.e(msg);
        tvForget.setText(msg);
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginComplete() {
        finish();
    }
}
