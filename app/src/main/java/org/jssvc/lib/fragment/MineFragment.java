package org.jssvc.lib.fragment;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.BorrowHistoryActivity;
import org.jssvc.lib.activity.BorrowPresentActivity;
import org.jssvc.lib.activity.LoginActivity;
import org.jssvc.lib.activity.MyDebtActivity;
import org.jssvc.lib.activity.MyViolationActivity;
import org.jssvc.lib.activity.SettingActivity;
import org.jssvc.lib.activity.UserBriefActivity;
import org.jssvc.lib.activity.WebActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.userInfoLayout)
    LinearLayout userInfoLayout;
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
    @BindView(R.id.ivTotal)
    ImageView ivTotal;
    @BindView(R.id.ivReading)
    ImageView ivReading;
    @BindView(R.id.ivLaws)
    ImageView ivLaws;
    @BindView(R.id.ivDebt)
    ImageView ivDebt;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断有没有成功登陆
        if (!AccountPref.isLogon(context)) {
            // 未成功登陆
            tvUserName.setText("点击登陆");
            tvUserLevel.setText("登陆后更精彩...");

            tvTotal.setVisibility(View.GONE);
            tvReading.setVisibility(View.GONE);
            tvLaws.setVisibility(View.GONE);
            tvDebt.setVisibility(View.GONE);
            ivTotal.setVisibility(View.VISIBLE);
            ivReading.setVisibility(View.VISIBLE);
            ivLaws.setVisibility(View.VISIBLE);
            ivDebt.setVisibility(View.VISIBLE);

            simpleDraweeView.setImageURI(Uri.parse("res://" + context.getPackageName() + "/" + R.drawable.def_user_avatar));
        } else {
            // 成功登陆
            tvUserName.setText("董洪逾");
            tvUserLevel.setText("初出茅庐");

            tvTotal.setText("12");
            tvReading.setText("2");
            tvLaws.setText("0");
            tvDebt.setText("0");
            tvTotal.setVisibility(View.VISIBLE);
            tvReading.setVisibility(View.VISIBLE);
            tvLaws.setVisibility(View.VISIBLE);
            tvDebt.setVisibility(View.VISIBLE);
            ivTotal.setVisibility(View.GONE);
            ivReading.setVisibility(View.GONE);
            ivLaws.setVisibility(View.GONE);
            ivDebt.setVisibility(View.GONE);

            simpleDraweeView.setImageURI("https://avatars0.githubusercontent.com/u/7424705?v=3&s=466");
        }
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @OnClick({R.id.userInfoLayout, R.id.simpleDraweeView, R.id.totalLayout, R.id.readingLayout, R.id.lawsLayout, R.id.debtLayout, R.id.tvReadBack, R.id.tvSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.simpleDraweeView:
            case R.id.userInfoLayout:
                if (AccountPref.isLogon(context)) {
                    // 用户详情
                    startActivity(new Intent(context, UserBriefActivity.class));
                } else {
                    startActivity(new Intent(context, LoginActivity.class));
                }
                break;
            case R.id.totalLayout:
                // 借阅历史
                startActivity(new Intent(context, BorrowHistoryActivity.class));
                break;
            case R.id.readingLayout:
                // 当前借阅
                startActivity(new Intent(context, BorrowPresentActivity.class));
                break;
            case R.id.lawsLayout:
                // 违章
                startActivity(new Intent(context, MyViolationActivity.class));
                break;
            case R.id.debtLayout:
                // 欠费
                startActivity(new Intent(context, MyDebtActivity.class));
                break;
            case R.id.tvReadBack:
                // 我的阅读心得
                Intent intentReadBack = new Intent(context, WebActivity.class);
                intentReadBack.putExtra("url", HttpUrlParams.URL_ARTICLE + "new_2.html");
                intentReadBack.putExtra("title", "校园新闻");
                startActivity(intentReadBack);
                break;
            case R.id.tvSetting:
                // 设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }
}
