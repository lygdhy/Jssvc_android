package org.jssvc.lib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import org.jssvc.lib.utils.SwipeWindowHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder unbinder;
    public Context context;
    private Toast toast = null;//全局Toast

    private SwipeWindowHelper mSwipeWindowHelper;// 滑动关闭

    protected abstract int getContentViewId();

    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);
        context = this;
        initView();

        // 修改StatusBar颜色
//        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(context, R.color.colorAccent), 30);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
