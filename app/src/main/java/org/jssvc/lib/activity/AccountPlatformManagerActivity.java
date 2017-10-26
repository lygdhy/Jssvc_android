package org.jssvc.lib.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import java.util.HashMap;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.PlatformAccountPhoneCheckFragment;
import org.jssvc.lib.fragment.PlatformAccountResetPwdFragment;

/**
 * 平台账户管理AccountPlatformManagerActivity
 * 1、PlatformAccountPhoneCheckFragment	手机号码验证
 * 2、PlatformAccountResetPwdFragment	账户找回/注册
 */
public class AccountPlatformManagerActivity extends BaseActivity {
  public static final String ARG_OPT_CODE = "opt_code";

  PlatformAccountResetPwdFragment checkFragment;
  PlatformAccountPhoneCheckFragment phoneFragment;
  @BindView(R.id.tv_title) TextView tvTitle;

  private OnSendMessageHandler osmHandler;
  private EventHandler smsHandler;

  public boolean smart = false;// 是否智能，智能时无需验证
  public int opt_code = 0;//0注册1找回密码
  public String opt_phone = "";// 被操作手机号码
  public String str_pwd = "";// 新密码

  @Override protected int getContentViewId() {
    return R.layout.activity_account_platform_manager;
  }

  @Override protected void initView() {
    opt_code = getIntent().getIntExtra(ARG_OPT_CODE, 0);
    if (opt_code == 0) tvTitle.setText("注册");
    if (opt_code == 1) tvTitle.setText("重置密码");

    initSMSSDK();// 初始化SDK

    initPhoneCheckFragment();
  }

  @OnClick({ R.id.tv_back }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
    }
  }

  /**
   * 输入手机号码页面
   */
  public void initPhoneCheckFragment() {
    phoneFragment = new PlatformAccountPhoneCheckFragment();
    getSupportFragmentManager().beginTransaction().add(R.id.main_container, phoneFragment).commit();
  }

  /**
   * 重置密码页面
   */
  public void resetPwdFragment() {
    checkFragment = new PlatformAccountResetPwdFragment();
    getSupportFragmentManager().beginTransaction()
        .add(R.id.main_container, checkFragment)
        .addToBackStack(null)
        .commit();
  }

  // 发送短信
  public void sendSMS() {
    SMSSDK.getVerificationCode("+86", opt_phone, osmHandler);
  }

  // 验证短信
  public void checkSMS(String code) {
    SMSSDK.submitVerificationCode("+86", opt_phone, code);
  }

  public void setOnSendMessageHandler(OnSendMessageHandler h) {
    osmHandler = h;
  }

  // 初始化短信引擎
  private void initSMSSDK() {
    smsHandler = new EventHandler() {
      @Override public void afterEvent(final int event, final int result, final Object data) {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            if (result == SMSSDK.RESULT_COMPLETE) {
              if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                // 提交验证码成功
                HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                runOnUiThread(new Runnable() {
                  @Override public void run() {
                    checkFragment.passCheck();
                  }
                });
              } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                //获取验证码成功,true为智能验证，false为普通下发短信
                smart = (Boolean) data;
                if (smart) {
                  // SDK不发送短信，无须填写验证码
                  runOnUiThread(new Runnable() {
                    @Override public void run() {
                      showToast("验证成功！！");
                    }
                  });
                } else {
                  // SDK下发短信，需填写验证码
                  runOnUiThread(new Runnable() {
                    @Override public void run() {
                      showToast("验证短信已发送，请注意查收！");
                    }
                  });
                }
                // 页面跳转
                resetPwdFragment();
              } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                // 返回支持发送验证码的国家列表
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
                  runOnUiThread(new Runnable() {
                    @Override public void run() {
                      // 提示错误信息
                      showToast(des);
                      if (checkFragment != null) checkFragment.unPassCheck();
                    }
                  });

                  return;
                }
              } catch (Exception e) {
                //do something
              }
            }
          }
        });
      }
    };
    SMSSDK.registerEventHandler(smsHandler); //注册短信回调
  }
}
