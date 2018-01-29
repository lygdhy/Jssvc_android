package org.jssvc.lib.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.jssvc.lib.R;
import org.jssvc.lib.base.impl.IBaseActivity;
import org.jssvc.lib.utils.StatusBarUtils;
import org.jssvc.lib.view.TitleView;
import org.jssvc.lib.view.pDialog.XProgressDialog;

/**
 * Created by jjj on 2018/1/26.
 *
 * @description:
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {

    private XProgressDialog mProgressDialog = null;//全局ProgressDialog

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //防止界面被重复打开
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        if (setTransBarId() != 0) {
            setContentView(getContentViewId());
            StatusBarUtils.setStatusBar(this, setTransBarId());
        } else {
            setContentView(R.layout.activity_base);
            TitleView titleView = findViewById(R.id.title_view);
            StatusBarUtils.setStatusBar(this, titleView.getParentView());
            LinearLayout ll_root = findViewById(R.id.ll_base_root);
            View content = View.inflate(this, getContentViewId(), null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll_root.addView(content, params);
            initTitle(titleView);
        }

        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (setEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (setEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void beforeOnCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterOnCreate(Bundle savedInstanceState) {

    }

    /**
     * 设置沉浸在状态栏的颜色
     *
     * @return 0:表示使用默认标题栏
     */
    @Override
    public int setTransBarId() {
        return 0;
    }

    @Override
    public void startAnimActivity(Class<?> cla) {
        startActivity(new Intent(this, cla));
    }

    @Override
    public void startAnimActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(null);
    }

    @Override
    public void showProgressDialog(String msg) {
        KeyboardUtils.hideSoftInput(this);
        if (mProgressDialog == null) {
            mProgressDialog = new XProgressDialog(this, msg, XProgressDialog.THEME_HORIZONTAL_SPOT);
        }
        if (!TextUtils.isEmpty(msg)) {
            mProgressDialog.setMessage(msg);
        }
        mProgressDialog.show();
    }

    @Override
    public void dissmissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public boolean setEventBus() {
        return false;
    }
}
