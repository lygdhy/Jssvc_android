package org.jssvc.lib.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.AboutActivity;
import org.jssvc.lib.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvUserLevel)
    TextView tvUserLevel;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.totalLayout)
    LinearLayout totalLayout;
    @BindView(R.id.tvReading)
    TextView tvReading;
    @BindView(R.id.readingLayout)
    LinearLayout readingLayout;
    @BindView(R.id.tvLaws)
    TextView tvLaws;
    @BindView(R.id.lawsLayout)
    LinearLayout lawsLayout;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.debtLayout)
    LinearLayout debtLayout;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        tvUserName.setText("朱小雨");
        tvUserLevel.setText("读书达人");

        tvTotal.setText("15");
        tvReading.setText("2");
        tvLaws.setText("0");
        tvDebt.setText("0");

        simpleDraweeView.setImageURI("http://v1.qzone.cc/avatar/201408/22/21/52/53f74b13786e4125.jpg%21200x200.jpg");
    }

    @OnClick({R.id.totalLayout, R.id.readingLayout, R.id.lawsLayout, R.id.debtLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.totalLayout:
                // 借阅历史
                startActivity(new Intent(context, AboutActivity.class));
                break;
            case R.id.readingLayout:
                // 当前借阅
                break;
            case R.id.lawsLayout:
                // 违章
                break;
            case R.id.debtLayout:
                // 欠费
                break;
        }
    }
}
