package org.jssvc.lib.activity;

import android.os.Bundle;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * APP主程序
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
