package org.jssvc.lib.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountLibManagerActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 图书馆账户激活
 *     version: 1.0
 * </pre>
 */
public class LibAccountActivateFragment extends BaseFragment {

  @BindView(R.id.edt_real_name) EditText edtRealName;

  public LibAccountActivateFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_lib_account_activate;
  }

  @Override protected void initView() {
  }

  @OnClick({ R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_submit:
        String realName = edtRealName.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
          showToast("请输入您图书证上的姓名");
        } else {
          showProgressDialog("正在提交...");

          OkGo.<String>post(HttpUrlParams.URL_LIB_USER_REGISTER).tag(this)
              .params("name", realName)
              .execute(new StringCallback() {
                @Override public void onSuccess(Response<String> response) {
                  dissmissProgressDialog();
                  parseHtml(response.body());
                }

                @Override public void onError(Response<String> response) {
                  super.onError(response);
                  dissmissProgressDialog();
                  dealNetError(response);
                }
              });

          //OkGo.post(HttpUrlParams.URL_LIB_USER_REGISTER)
          //    .tag(this)
          //    .params("name", realName)
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
        break;
    }
  }

  // 解析网页
  private void parseHtml(String s) {
    String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 身份验证成功，重置密码
      AccountLibManagerActivity activity = (AccountLibManagerActivity) getActivity();
      activity.accountResetPwdFragment();
    } else {
      // 有错误提示
      showToast(errorMsg);
    }
  }
}
