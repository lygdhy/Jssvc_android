package org.jssvc.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;

import org.jssvc.lib.utils.KeyboardUtils;
import org.jssvc.lib.view.pDialog.XProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    public Context context;

    private Toast toast = null;//全局Toast
    private XProgressDialog progressDialog = null;//全局ProgressDialog

    protected abstract int getContentViewId();

    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);
        context = this;
        initView();

        PgyCrashManager.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        PgyCrashManager.unregister();
    }

    /**
     * 全局Toast
     */
    protected void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * show ProgressDialog
     */
    protected void showProgressDialog() {
        KeyboardUtils.hideSoftInput(this);
        if (progressDialog == null) {
            progressDialog = new XProgressDialog(context, XProgressDialog.THEME_HORIZONTAL_SPOT);
        }
        progressDialog.show();
    }

    /**
     * show ProgressDialog
     */
    protected void showProgressDialog(String msg) {
        KeyboardUtils.hideSoftInput(this);
        if (progressDialog == null) {
            progressDialog = new XProgressDialog(context, msg, XProgressDialog.THEME_HORIZONTAL_SPOT);
        } else {
            progressDialog.setMessage(msg);
        }
        progressDialog.show();
    }

    /**
     * dissmiss ProgressDialog
     */
    protected void dissmissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
