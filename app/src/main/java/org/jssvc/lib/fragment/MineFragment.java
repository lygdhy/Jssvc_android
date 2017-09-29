package org.jssvc.lib.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AboutSchoolActivity;
import org.jssvc.lib.activity.AccountThirdManagerActivity;
import org.jssvc.lib.activity.BookShelfActivity;
import org.jssvc.lib.activity.CurentBorrowActivity;
import org.jssvc.lib.activity.LoginActivity;
import org.jssvc.lib.activity.SettingActivity;
import org.jssvc.lib.activity.UserResumeActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.utils.ImageLoader;
import org.jssvc.lib.view.WaveView;

import static org.jssvc.lib.base.BaseApplication.libOnline;
import static org.jssvc.lib.base.BaseApplication.localMemberBean;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/06/13
 *     desc   : 个人中心
 *     version: 1.0
 * </pre>
 */
public class MineFragment extends BaseFragment {

  @BindView(R.id.iv_avatar) ImageView ivAvatar;
  @BindView(R.id.tv_user_name) TextView tvUserName;
  @BindView(R.id.waveView) WaveView waveView;

  @Override protected int getContentViewId() {
    return R.layout.fragment_mine;
  }

  @Override protected void initView() {
    waveView.setColor(getResources().getColor(R.color.color_ui_theme));
    waveView.start();
  }

  @OnClick({
      R.id.ll_user, R.id.rl_borrow, R.id.rl_bookrack, R.id.rl_appraise, R.id.rl_account,
      R.id.rl_about_lib, R.id.rl_setting
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ll_user:
        // 登录/查看详情
        if (DataSup.hasLogin()) {// 已登录
          startActivity(new Intent(mContext, UserResumeActivity.class));
        } else {// 未登录
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.rl_borrow:
        // 当前借阅 / 催还续借
        if (libOnline) {
          startActivity(new Intent(mContext, CurentBorrowActivity.class));
        } else {
          showToast("图书服务已离线，需重新连接");
        }
        //// 借阅历史
        //if (libOnline) {
        //  startActivity(new Intent(mContext, HistoryBorrowActivity.class));
        //} else {
        //  showToast("图书服务已离线，需重新连接");
        //}
        break;
      case R.id.rl_bookrack:
        // 我的书架
        if (libOnline) {
          startActivity(new Intent(mContext, BookShelfActivity.class));
        } else {
          showToast("图书服务已离线，需重新连接");
        }
        break;
      case R.id.rl_appraise:
        // 我的书评
        showToast("暂未开通");
        break;
      case R.id.rl_account:
        // 第三方账户绑定
        startActivity(new Intent(mContext, AccountThirdManagerActivity.class));
        break;
      case R.id.rl_about_lib:
        // 关于图书馆
        startActivity(new Intent(mContext, AboutSchoolActivity.class));
        break;
      case R.id.rl_setting:
        // 设置
        startActivity(new Intent(mContext, SettingActivity.class));
        break;
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (DataSup.hasLogin()) {// 已登录
      localMemberBean = DataSup.getLocalMemberBean();
      ImageLoader.with(mContext, ivAvatar, localMemberBean.getAvatar());
      tvUserName.setText(localMemberBean.getNickname());
    } else {// 未登录
      ImageLoader.with(mContext, ivAvatar, R.drawable.icon_default_avatar_1);
      tvUserName.setText("点击登录");
    }
  }
}
