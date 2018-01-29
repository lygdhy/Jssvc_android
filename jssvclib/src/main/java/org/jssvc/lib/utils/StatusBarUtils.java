package org.jssvc.lib.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by jjj on 2017/3/16.
 *
 * @description:
 */

public class StatusBarUtils {
    /**
     * 设置沉浸式状态栏
     *
     * @param activity
     * @param title_id(R.id.xxx)
     */
    public static void setStatusBar(final Activity activity, int title_id) {
        if (title_id != 0) {
            final ViewGroup linear_bar = activity.findViewById(title_id);
            setStatusBar(activity, linear_bar);
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    public static void setStatusBar(final Activity activity, final View viewGroup) {
        if (viewGroup == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            viewGroup.post(new Runnable() {
                @Override
                public void run() {
                    viewGroup.setPadding(0, getStatusBarHeight(activity), 0, 0);
                }
            });
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int statusBarHeight = activity.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
