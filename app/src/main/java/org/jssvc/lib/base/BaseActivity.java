package org.jssvc.lib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Activity 基类
 */
public class BaseActivity extends Activity {
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }
}
