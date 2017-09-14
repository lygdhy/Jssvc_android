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
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.AccountPref;
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
        String localpwd = AccountPref.getLogonAccoundPwd(mContext);

        if (TextUtils.isEmpty(oldpwd) || TextUtils.isEmpty(newpwd)) {
          showToast("就旧密码不能为空！");
        } else {
          if (oldpwd.equals(localpwd)) {
            go2Reset(newpwd);
          } else {
            showToast("旧密码不符");
          }
        }
        break;
    }
  }

  // 前往重置密码
  private void go2Reset(String newpwd) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_CHANGE_PWD).tag(this)
        .params("old_passwd", AccountPref.getLogonAccoundPwd(mContext))
        .params("new_passwd", newpwd)
        .params("chk_passwd", newpwd)
        .params("submit1", "%E7%A1%AE%E5%AE%9A")
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            parseHtml(response.body());
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("正在提交...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });

    //OkGo.post(HttpUrlParams.URL_LIB_CHANGE_PWD)
    //    .tag(this)
    //    .params("old_passwd", AccountPref.getLogonAccoundPwd(mContext))
    //    .params("new_passwd", newpwd)
    //    .params("chk_passwd", newpwd)
    //    .params("submit1", "%E7%A1%AE%E5%AE%9A")
    //    .execute(new StringCallback() {
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        dissmissProgressDialog();
    //        // s 即为所需要的结果
    //        parseHtml(s);
    //      }
    //
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dissmissProgressDialog();
    //        dealNetError(e);
    //      }
    //    });
  }

  // 解析网页
  private void parseHtml(String s) {
    String errorMsg = HtmlParseUtils.getErrMsgOnChangePwd(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 密码修改成功
      showToast("密码修改成功");
      AccountPref.saveLoginAccoundPwd(mContext, edtNewPwd.getText().toString().trim());
      getActivity().finish();
    } else {
      // 有错误提示
      showToast(errorMsg);
    }
  }
}
