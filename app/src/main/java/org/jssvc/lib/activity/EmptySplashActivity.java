package org.jssvc.lib.activity;

import android.content.Intent;
import android.os.Bundle;

import org.jssvc.lib.base.BaseActivity;

import qiu.niorgai.StatusBarCompat;

/**
 * 空启动，无ContentView，防APP启动时黑白屏
 */
public class EmptySplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarCompat.translucentStatusBar(this, false);

        // 注意, 这里并没有setContentView, 单纯只是用来跳转到相应的Activity.
        // 目的是减少首屏渲染

        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }
}
