package org.jssvc.lib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 空启动页
 */
public class EmptySplashActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // 注意, 这里并没有setContentView, 单纯只是用来跳转到相应的Activity.
    // 目的是减少首屏渲染
    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
    finish();
  }
}
