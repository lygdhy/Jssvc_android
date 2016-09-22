package org.jssvc.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import org.jssvc.lib.utils.SwipeWindowHelper;

/**
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity {
    public Context context;
    private Toast toast = null;//全局Toast

    private SwipeWindowHelper mSwipeWindowHelper;// 滑动关闭

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!supportSlideBack()) {
            return super.dispatchTouchEvent(ev);
        }

        if (mSwipeWindowHelper == null) {
            mSwipeWindowHelper = new SwipeWindowHelper(getWindow());
        }
        return mSwipeWindowHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    protected boolean supportSlideBack() {
        return true;
    }

}
