package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * APP启动页面
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 全屏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 100);//静态启动页
    }

    class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(context, MainActivity.class));
            finish();
        }
    }
}
