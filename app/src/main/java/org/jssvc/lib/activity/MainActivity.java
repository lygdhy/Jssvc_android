package org.jssvc.lib.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import java.util.HashMap;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity {

  @BindView(R.id.edt_phone) EditText edtPhone;
  @BindView(R.id.edt_code) EditText edtCode;

  EventHandler eh;
  String str_phone = "";

  @Override protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override protected void initView() {
    eh = new EventHandler() {
      @Override public void afterEvent(int event, int result, Object data) {
        if (result == SMSSDK.RESULT_COMPLETE) {
          //回调完成
          if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            //提交验证码成功
            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
            showToast("验证成功！！！！！！！！！！！！！");
          } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
            //获取验证码成功,true为智能验证，false为普通下发短信
            boolean smart = (Boolean) data;
            if (smart) {
              //通过智能验证
              Log.d("DHY", "发送成功！Tip：智能验证");
            } else {
              //依然走短信验证
              Log.d("DHY", "发送成功！Tip：依然走短信验证");
            }
          } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
            //返回支持发送验证码的国家列表
          }
          Log.d("DHY", data.toString());
        } else {
          try {
            Throwable throwable = (Throwable) data;
            throwable.printStackTrace();
            JSONObject object = new JSONObject(throwable.getMessage());
            String des = object.optString("detail");//错误描述
            int status = object.optInt("status");//错误代码
            if (status > 0 && !TextUtils.isEmpty(des)) {
              Log.d("DHY", "des = " + des);
              return;
            }
          } catch (Exception e) {
            //do something
          }
        }
      }
    };
    SMSSDK.registerEventHandler(eh); //注册短信回调
  }

  @OnClick({ R.id.btn_get, R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_get:
        str_phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(str_phone)) {
          showToast("号码不能为空");
          break;
        }
        SMSSDK.getVerificationCode("+86", str_phone, new OnSendMessageHandler() {
          @Override public boolean onSendMessage(String country, String phone) {
            /**
             * 此方法在发送验证短信前被调用，传入参数为接收者号码
             * 开发者自己执行一个操作，来根据电话号码判断是否需要发送短信
             * 返回true表示此号码无须实际接收短信
             */
            return false;
          }
        });
        break;
      case R.id.btn_submit:
        // 验证
        String code = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
          showToast("验证码不能为空");
          break;
        }
        SMSSDK.submitVerificationCode("+86", str_phone, code);

        break;
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    SMSSDK.unregisterEventHandler(eh);
  }
}
