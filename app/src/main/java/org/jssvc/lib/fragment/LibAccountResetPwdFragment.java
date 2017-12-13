package org.jssvc.lib.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountLibManagerActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 图书馆账户修改密码
 *     version: 1.0
 * </pre>
 */
public class LibAccountResetPwdFragment extends BaseFragment {

  @BindView(R.id.edt_old_pwd) EditText edtOldPwd;
  @BindView(R.id.edt_new_pwd) EditText edtNewPwd;

  public LibAccountResetPwdFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_lib_account_reset_pwd;
  }

  @Override protected void initView() {
  }

  @OnClick({ R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_submit:
        String oldpwd = edtOldPwd.getText().toString().trim();
        String newpwd = edtNewPwd.getText().toString().trim();

        AccountLibManagerActivity activity = (AccountLibManagerActivity) getActivity();
        String localpwd = activity.oldPwd;

        if (TextUtils.isEmpty(oldpwd) || TextUtils.isEmpty(newpwd)) {
          showToast("新旧密码不能为空！");
        } else {
          if (oldpwd.equals(localpwd)) {
            go2Reset(localpwd, newpwd);
          } else {
            showToast("旧密码不符");
          }
        }
        break;
    }
  }

  // 前往重置密码
  private void go2Reset(String oldpwd, final String newpwd) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_CHANGE_PWD).tag(this)
        .params("old_passwd", oldpwd)
        .params("new_passwd", newpwd)
        .params("chk_passwd", newpwd)
        .params("submit1", "%E7%A1%AE%E5%AE%9A")
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            parseHtml(response.body(), newpwd);
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("正在修改...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // 解析网页
  private void parseHtml(String s, String newpwd) {
    String errorMsg = HtmlParseUtils.getErrMsgOnChangePwd(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 密码修改成功
      showToast("密码修改成功");
      // 提交第三方账户
      submintThirdAccount(newpwd);
    } else {
      // 有错误提示
      showToast(errorMsg);
    }
  }

  // 提交第三方账户
  private void submintThirdAccount(String newpwd) {
    AccountLibManagerActivity activity = (AccountLibManagerActivity) getActivity();

    OkGo.<String>post(HttpUrlParams.THIRD_ACCOUNT_BOUND).tag(this)
        .params("uid", getUid())
        .params("platform", "1")// 平台1图书馆
        .params("school_abbr", activity.school)//学校代码
        .params("third_account", activity.name)
        .params("third_pwd", newpwd)
        .params("third_type", activity.type)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                JSONObject jo = jsonObject.optJSONObject("data");

                showToast("绑定成功");
                DataSup.saveThirdAccountJson2Local(jo.toString());
                getActivity().finish();
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
            showProgressDialog("绑定中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }
}
