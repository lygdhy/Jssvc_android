package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import java.util.HashMap;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountPlatformManagerActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.view.TimeCountDown;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 平台账号重置密码（用于注册和找回密码）
 *     version: 1.0
 * </pre>
 */
public class PlatformAccountResetPwdFragment extends BaseFragment
    implements TimeCountDown.OnTimerCountDownListener {

  @BindView(R.id.edt_phone) EditText edtPhone;
  @BindView(R.id.edt_code) EditText edtCode;
  @BindView(R.id.edt_pwd) EditText edtPwd;
  @BindView(R.id.btn_submit) Button btnSubmit;
  @BindView(R.id.btn_count_down) TimeCountDown btnCountDown;

  EventHandler smsHandler;

  int opt_code = 0;//0注册1找回密码
  String opt_phone = "";// 被操作手机号码
  String str_pwd = "";// 新密码

  public PlatformAccountResetPwdFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_platform_account_reset_pwd;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments().containsKey(AccountPlatformManagerActivity.ARG_OPT_CODE)) {
      opt_code = getArguments().getInt(AccountPlatformManagerActivity.ARG_OPT_CODE);
      opt_phone = getArguments().getString(AccountPlatformManagerActivity.ARG_OPT_PHONE);
    }
  }

  @Override protected void initView() {
    if (opt_code == 0) btnSubmit.setText("立即注册");
    if (opt_code == 1) btnSubmit.setText("重置密码");
    edtPhone.setText(opt_phone);
    edtCode.setFocusable(true);

    btnCountDown.setOnTimerCountDownListener(this);// 初始化倒计时

    initSMSSDK();// 初始化SDK

    sendSMS();// 发送短信
  }

  @OnClick({ R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_submit:
        String code = edtCode.getText().toString().trim();
        String pwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd)) {
          showToast("验证码和密码不能为空");
        } else {
          // 短信验证
          str_pwd = pwd;
          SMSSDK.submitVerificationCode("+86", opt_phone, code);
        }
        break;
    }
  }

  // 发送短信
  private void sendSMS() {
    SMSSDK.getVerificationCode("+86", opt_phone, new OnSendMessageHandler() {
      @Override public boolean onSendMessage(String country, String phone) {
        return false;
      }
    });
  }

  // 初始化短信引擎
  private void initSMSSDK() {
    smsHandler = new EventHandler() {
      @Override public void afterEvent(int event, int result, Object data) {
        if (result == SMSSDK.RESULT_COMPLETE) {
          if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            //提交验证码成功
            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
            Log.d("DHY", "验证成功！！！！！！！！！！！！！");
            getActivity().runOnUiThread(new Runnable() {
              @Override public void run() {
                saveUserData();
              }
            });
          } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
            //boolean smart = (Boolean) data;
            //获取验证码成功,true为智能验证，false为普通下发短信
            getActivity().runOnUiThread(new Runnable() {
              @Override public void run() {
                showToast("验证短信已发送，请注意查收！");
                btnCountDown.initTimer();
              }
            });
          } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
            //返回支持发送验证码的国家列表
          }
          Log.d("DHY", data.toString());
        } else {
          try {
            Throwable throwable = (Throwable) data;
            throwable.printStackTrace();
            JSONObject object = new JSONObject(throwable.getMessage());
            final String des = object.optString("detail");//错误描述
            int status = object.optInt("status");//错误代码
            if (status > 0 && !TextUtils.isEmpty(des)) {

              Log.d("DHY", "des = " + des);
              getActivity().runOnUiThread(new Runnable() {
                @Override public void run() {
                  // 提示错误信息
                  showToast(des);
                  if (btnCountDown != null) btnCountDown.cancel();
                }
              });

              return;
            }
          } catch (Exception e) {
            //do something
          }
        }
      }
    };
    SMSSDK.registerEventHandler(smsHandler); //注册短信回调
  }

  private void saveUserData() {
    showToast("验证成功!!!!保存数据");
    getActivity().finish();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (btnCountDown != null) btnCountDown.cancel();
    if (smsHandler != null) SMSSDK.unregisterEventHandler(smsHandler);
  }

  @Override public void onCountDownStart() {
    Log.d("DHY", "onCountDownStart");
  }

  @Override public void onCountDownLoading(int currentCount) {
    Log.d("DHY", "currentCount:" + currentCount);
  }

  @Override public void onCountDownError() {
    Log.d("DHY", "onCountDownError");
    if (btnCountDown != null) btnCountDown.setText("点击重试");
  }

  @Override public void onCountDownFinish() {
    Log.d("DHY", "onCountDownFinish");
    if (btnCountDown != null) btnCountDown.setText("重新发送");
  }
}
