package org.jssvc.lib.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MemberBean;
import org.jssvc.lib.bean.ThirdAccountBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;

/**
 * 第三方帐号管理
 */
public class AccountThirdManagerActivity extends BaseActivity {
  @BindView(R.id.tv_lib_title) TextView tvLibTitle;

  ThirdAccountBean libBean;
  ThirdAccountBean jwBean;

  @Override protected int getContentViewId() {
    return R.layout.activity_account_third_manager;
  }

  @Override protected void initView() {
    MemberBean bean = DataSup.getLocalMemberBean();
    if (bean != null) {
      getUserAcount(bean.getId());// 获取当前用户绑定的信息
    }
  }

  private void loadUi() {
    libBean = DataSup.getThirdAccountBean(Constants.THIRD_ACCOUNT_CODE_LIB);
    if (libBean != null) {
      tvLibTitle.setText("我的图书馆（已绑定" + libBean.getAccount() + "）");
    }
    jwBean = DataSup.getThirdAccountBean(Constants.THIRD_ACCOUNT_CODE_JW);
    if (jwBean != null) {

    }
  }

  @OnClick({
      R.id.tv_back, R.id.rl_lib, R.id.rl_jw
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_lib:
        // 图书馆账户绑定
        if (libBean == null) {
          startActivity(new Intent(mContext, AccountLibManagerActivity.class));
        }
        break;
      case R.id.rl_jw:
        // 教务系统绑定
        break;
    }
  }

  // 获取当前用户绑定的第三方账户信息
  private void getUserAcount(String uid) {
    OkGo.<String>post(HttpUrlParams.GET_THIRD_ACCOUNTS).tag(this)
        .params("uid", uid)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                JSONObject jo = jsonObject.optJSONObject("data");
                DataSup.setThirdAccountStr2Local(jo.toString());
                loadUi();
              } else {
                showToast(jsonObject.optString("message"));
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog();
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }
}
