package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.AppUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.view.CustomDialog;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

  @BindView(R.id.edtFeed) EditText edtFeed;
  @BindView(R.id.edtEmail) EditText edtEmail;
  @BindView(R.id.btnSubFeed) Button btnSubFeed;

  @Override protected int getContentViewId() {
    return R.layout.activity_feedback;
  }

  @Override protected void initView() {

  }

  @OnClick({ R.id.opt_back, R.id.btnSubFeed, R.id.opt_chat }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.opt_back:
        finish();
        break;
      case R.id.btnSubFeed:
        String feedStr = edtFeed.getText().toString().trim();
        String contactStr = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(feedStr)) {
          showToast("先写一些意见吧~");
        } else if (TextUtils.isEmpty(contactStr)) {
          showToast("请留下您的联系方式，方面我们及时沟通！");
        } else {
          submintFeedback(feedStr, contactStr);
        }
        break;
      case R.id.opt_chat:
        callQQCell(Constants.QQ_WAITER);
        break;
    }
  }

  // 提交反馈
  private void submintFeedback(String feedStr, String contactStr) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    OkGo.<String>post(HttpUrlParams.URL_ORG_FEEDBACK).tag(this)
        .params("time", df.format(new Date()))
        .params("uid", getUid())
        .params("content", feedStr)
        .params("contact", contactStr + "")
        .params("device", getPhoneBrand() + " - " + getPhoneModel() + " - " + getBuildVersion())
        .params("version", AppUtils.getAppVersionName())
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {

            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                thankDialog();
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
            showProgressDialog("正在提交...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // 感谢
  private void thankDialog() {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
    builder.setTitle("提示");
    builder.setMessage("感谢您提出的宝贵意见！");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();
        finish();
      }
    });
    builder.create().show();
  }

  /**
   * 获取手机品牌
   */
  public static String getPhoneBrand() {
    return android.os.Build.BRAND;
  }

  /**
   * 获取手机型号
   */
  public static String getPhoneModel() {
    return android.os.Build.MODEL;
  }

  /**
   * 获取手机Android 版本（4.4、5.0、5.1 ...）
   */
  public static String getBuildVersion() {
    return android.os.Build.VERSION.RELEASE;
  }
}
