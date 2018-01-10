package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.RegexUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.EventSms;
import org.jssvc.lib.data.HttpUrlParams;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 平台账号验证（用于注册和找回密码）
 *     version: 1.0
 * </pre>
 */
public class PlatformAccountPhoneCheckFragment extends BaseFragment {
  @BindView(R.id.edt_username) EditText edtUsername;

  int opt_code = 0;

  public PlatformAccountPhoneCheckFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_platform_account_phone_check;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    opt_code = getArguments().getInt("opt_code");
  }

  @Override protected void initView() {

  }

  @OnClick({ R.id.btn_next, R.id.tv_protocol }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_next:// 下一步
        String phone = edtUsername.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
          showToast("请先输入手机号码");
        } else if (!RegexUtils.isMobileExact(phone)) {
          showToast("请输入正确的手机号码");
        } else {
          checkifreg(phone);
        }
        break;
      case R.id.tv_protocol:// 阅读协议
        showToast("好好学习，天天向上！");
        break;
    }
  }

  // 检查是否注册
  private void checkifreg(final String phone) {
    OkGo.<String>post(HttpUrlParams.CHECK_IF_REG).tag(this)
        .params("phone", phone)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                int hasReg = jsonObject.optInt("data");
                // opt_code //0注册 1找回密码
                // hasReg //0未注册 1已注册
                if (hasReg == 1 && opt_code == 0) {
                  showToast("该手机号已注册，请直接登录");
                  return;
                }
                if (hasReg == 0 && opt_code == 1) {
                  showToast("该手机号未注册");
                  return;
                }
                // 提交短信SDK验证
                EventBus.getDefault().post(new EventSms("send", phone));
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
