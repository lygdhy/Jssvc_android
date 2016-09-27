package org.jssvc.lib.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.SettingActivity;
import org.jssvc.lib.activity.WebActivity;
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
    @BindView(R.id.tvReadBack)
    TextView tvReadBack;
    @BindView(R.id.tvSetting)
    TextView tvSetting;

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

    @Override
    protected void onFirstUserVisible() {

    }

    @OnClick({R.id.totalLayout, R.id.readingLayout, R.id.lawsLayout, R.id.debtLayout, R.id.tvReadBack, R.id.tvSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.totalLayout:
                // 借阅历史
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
            case R.id.tvReadBack:
                // 我的阅读心得
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", "http://www.hydong.me/article/new_2.html");
                intent.putExtra("title", "校园新闻");
                startActivity(intent);
                break;
            case R.id.tvSetting:
                // 设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }
}
