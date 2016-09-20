package org.jssvc.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity {
    public Context context;
    private Toast toast = null;//全局Toast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // 修改StatusBar颜色
//        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(context, R.color.colorAccent), 30);
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

}
