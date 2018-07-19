package org.jssvc.lib.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountThirdManagerActivity;
import org.jssvc.lib.activity.LoginActivity;
import org.jssvc.lib.activity.SettingActivity;
import org.jssvc.lib.activity.UserResumeActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ThirdAccountBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.utils.ImageLoader;
import org.jssvc.lib.view.WaveView;

import static org.jssvc.lib.base.BaseApplication.libOnline;
import static org.jssvc.lib.base.BaseApplication.localUserBean;

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

  public static MineFragment newInstance() {
    return new MineFragment();
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_mine;
  }

  @Override protected void initView() {
    waveView.setColor(getResources().getColor(R.color.color_ui_theme));
    waveView.start();
  }

  @OnClick({
      R.id.ll_user, R.id.rl_account, R.id.rl_setting
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ll_user:
        // 登录/查看详情
        if (DataSup.hasUserLogin()) {// 已登录
          startActivity(new Intent(mContext, UserResumeActivity.class));
        } else {// 未登录
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.rl_account:
        // 第三方账户绑定
        if (DataSup.hasUserLogin()) {// 已登录
          startActivity(new Intent(mContext, AccountThirdManagerActivity.class));
        } else {// 未登录
          startActivity(new Intent(mContext, LoginActivity.class));
        }
        break;
      case R.id.rl_setting:
        // 设置
        startActivity(new Intent(mContext, SettingActivity.class));
        break;
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (DataSup.hasUserLogin()) {// 已登录
      localUserBean = DataSup.getLocalUserBean();
      if (TextUtils.isEmpty(localUserBean.getAvatar())) {
        ImageLoader.with(mContext, ivAvatar, R.drawable.icon_default_avatar_1);
      } else {
        ImageLoader.withCircle(mContext, ivAvatar, localUserBean.getAvatar());
      }
      tvUserName.setText(
          TextUtils.isEmpty(localUserBean.getNickname()) ? "路人甲" : localUserBean.getNickname());

      // 如果绑定且未登录，自动登录到图书馆
      ThirdAccountBean libBean = DataSup.getLibThirdAccount();
      if (libBean != null && !libOnline) {
        // 有账号但未登录
        doLibLogin(libBean.getAccount(), libBean.getPwd(), libBean.getType());
      }
    } else {// 未登录
      ImageLoader.with(mContext, ivAvatar, R.drawable.icon_default_avatar_1);
      tvUserName.setText("点击登录");
    }
  }

  // 静默登录
  private void doLibLogin(final String loginname, final String loginpwd, final String loginType) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_LOGIN).tag(this)
        .params("number", loginname)
        .params("passwd", loginpwd)
        .params("select", loginType)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            parseHtml(response.body());
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          //@Override public void onStart(Request<String, ? extends Request> request) {
          //  super.onStart(request);
          //  showProgressDialog("帐号读取中...");
          //}
          //
          //@Override public void onFinish() {
          //  super.onFinish();
          //  dissmissProgressDialog();
          //}
        });
  }

  // 解析网页
  private void parseHtml(String s) {
    String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 登录成功
      libOnline = true;
    } else {
      libOnline = false;
    }
  }
}
