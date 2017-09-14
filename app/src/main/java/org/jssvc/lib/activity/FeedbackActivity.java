package org.jssvc.lib.activity;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.view.CustomDialog;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.edtFeed) EditText edtFeed;
  @BindView(R.id.edtEmail) EditText edtEmail;
  @BindView(R.id.btnSubFeed) Button btnSubFeed;
  @BindView(R.id.tvChat) TextView tvChat;

  @Override protected int getContentViewId() {
    return R.layout.activity_feedback;
  }

  @Override protected void initView() {

  }

  @OnClick({ R.id.tvBack, R.id.btnSubFeed, R.id.tvChat }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
      case R.id.btnSubFeed:
        String feedStr = edtFeed.getText().toString().trim();
        String emailStr = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(feedStr)) {
          showToast("先写一些意见吧~");
        } else if (TextUtils.isEmpty(emailStr)) {
          showToast("请留下您的联系方式，方面我们及时反馈！");
        } else {
          submintFeedback(feedStr, emailStr);
        }
        break;
      case R.id.tvChat:
        callQQCell("2906501168");
        break;
    }
  }

  // 提交反馈
  private void submintFeedback(String feedStr, String trim) {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    OkGo.<String>post(HttpUrlParams.URL_ORG_FEEDBACK).tag(this)
        .params("time", df.format(new Date()))
        .params("userid", AccountPref.getLogonAccoundNumber(mContext))
        .params("advice", feedStr)
        .params("email", trim + "")
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            thankDialog();
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

    //OkGo.post(HttpUrlParams.URL_ORG_FEEDBACK)
    //    .tag(this)
    //    .params("time", df.format(new Date()))
    //    .params("userid", AccountPref.getLogonAccoundNumber(mContext))
    //    .params("advice", feedStr)
    //    .params("email", trim + "")
    //    .execute(new StringCallback() {
    //      @Override public void onSuccess(String s, Call call, Response response) {
    //        dissmissProgressDialog();
    //        thankDialog();
    //      }
    //
    //      @Override public void onError(Call call, Response response, Exception e) {
    //        super.onError(call, response, e);
    //        dissmissProgressDialog();
    //        dealNetError(e);
    //      }
    //    });
  }

  // 感谢
  private void thankDialog() {
    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
    builder.setTitle("提示");
    builder.setMessage("感谢您提出的宝贵意见！");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(final DialogInterface dialog, int which) {
        dialog.dismiss();
        edtFeed.setText("");
        edtEmail.setText("");
      }
    });
    builder.create().show();
  }
}
