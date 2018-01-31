package org.jssvc.lib.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.jssvc.lib.R;
import org.jssvc.lib.base.impl.IBaseActivity;
import org.jssvc.lib.utils.StatusBarUtils;
import org.jssvc.lib.view.TitleView;

import butterknife.ButterKnife;

/**
 * Created by jjj on 2018/1/26.
 *
 * @description:
 */

public abstract class BaseActivity extends AbstractActivity implements IBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        //防止界面被重复打开
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        afterOnCreate(savedInstanceState);
        if (setTransBarId() != 0) {
            setContentView(getContentViewId());
            ButterKnife.bind(this);
            StatusBarUtils.setStatusBar(this, setTransBarId());
        } else {
            setContentView(R.layout.activity_base);
            TitleView titleView = findViewById(R.id.title_view);
            StatusBarUtils.setStatusBar(this, titleView.getParentView());
            LinearLayout ll_root = findViewById(R.id.ll_base_root);
            View content = View.inflate(this, getContentViewId(), null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll_root.addView(content, params);
            ButterKnife.bind(this);
            initTitle(titleView);
        }

        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (setEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (setEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

}
