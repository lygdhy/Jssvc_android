package org.jssvc.lib.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.jssvc.lib.base.impl.IBaseActivity;
import org.jssvc.lib.view.pDialog.XProgressDialog;

/**
 * Created by jjj on 2018/1/29.
 *
 * @description:
 */

public abstract class AbstractActivity extends AppCompatActivity implements IBaseActivity {

    /**
     * 全局ProgressDialog
     */
    private XProgressDialog mProgressDialog = null;

    @Override
    public void beforeOnCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterOnCreate(Bundle savedInstanceState) {

    }

    /**
     * 设置占据状态栏的View
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

    /**
     * 是否开启EventBus
     *
     * @return
     */
    @Override
    public boolean setEventBus() {
        return false;
    }

}
