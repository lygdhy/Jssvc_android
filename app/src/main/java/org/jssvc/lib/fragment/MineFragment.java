package org.jssvc.lib.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AboutSchoolActivity;
import org.jssvc.lib.activity.AccountThirdManagerActivity;
import org.jssvc.lib.activity.SettingActivity;
import org.jssvc.lib.activity.UserResumeActivity;
import org.jssvc.lib.base.BaseFragment;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 个人中心
 *     version: 1.0
 * </pre>
 */
public class MineFragment extends BaseFragment {

  @BindView(R.id.iv_avatar) SimpleDraweeView ivAvatar;
  @BindView(R.id.tv_user_name) TextView tvUserName;
  @BindView(R.id.tv_user_sign) TextView tvUserSign;

  @Override protected int getContentViewId() {
    return R.layout.fragment_mine;
  }

  @Override protected void initView() {

  }

  @OnClick({
      R.id.ll_user, R.id.rl_borrow, R.id.rl_bookrack, R.id.rl_appraise, R.id.rl_account,
      R.id.rl_about_lib, R.id.rl_setting
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ll_user:
        // 登录/查看详情
        startActivity(new Intent(context, UserResumeActivity.class));
        break;
      case R.id.rl_borrow:
        // 我的借阅
        break;
      case R.id.rl_bookrack:
        // 我的书架
        break;
      case R.id.rl_appraise:
        // 我的书评
        break;
      case R.id.rl_account:
        // 第三方账户绑定
        startActivity(new Intent(context, AccountThirdManagerActivity.class));
        break;
      case R.id.rl_about_lib:
        // 关于图书馆
        startActivity(new Intent(context, AboutSchoolActivity.class));
        break;
      case R.id.rl_setting:
        // 设置
        startActivity(new Intent(context, SettingActivity.class));
        break;
    }
  }
}
