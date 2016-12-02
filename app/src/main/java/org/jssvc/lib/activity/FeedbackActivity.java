package org.jssvc.lib.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.AppUtils;
import org.jssvc.lib.view.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.edtFeed)
    EditText edtFeed;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.btnSubFeed)
    Button btnSubFeed;
    @BindView(R.id.tvChat)
    TextView tvChat;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tvBack, R.id.btnSubFeed, R.id.tvChat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnSubFeed:
                String feedStr = edtFeed.getText().toString().trim();
                if (TextUtils.isEmpty(feedStr)) {
                    showToast("先写一些意见吧~");
                } else {
                    submintFeedback(feedStr, edtEmail.getText().toString().trim());
                }
                break;
            case R.id.tvChat:
                callQQCell("2906501168");
                break;
        }
    }

    // 提交反馈
    private void submintFeedback(String feedStr, String trim) {
        showProgressDialog("正在提交...");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        OkGo.post(HttpUrlParams.URL_ORG_FEEDBACK)
                .tag(this)
                .params("time", df.format(new Date()))
                .params("userid", AccountPref.getLogonAccoundNumber(context))
                .params("advice", feedStr)
                .params("email", trim + "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        thankDialog();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        dealNetError(e);
                    }

                });
    }

    // 感谢
    private void thankDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
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

    private void callQQCell(String qqNum) {
        // 手机QQ com.tencent.mqq
        // 手机QQ2012 com.tencent.mobileqq
        // QQ轻聊版 com.tencent.qqlite
        // QQ国际版 com.tencent.mobileqqi
        // QQHD com.tencent.minihd.qq
        // 企业QQ com.tencent.eim
        if (AppUtils.isInstallApp(context, "com.tencent.mqq") ||
                AppUtils.isInstallApp(context, "com.tencent.mobileqq") ||
                AppUtils.isInstallApp(context, "com.tencent.qqlite") ||
                AppUtils.isInstallApp(context, "com.tencent.mobileqqi") ||
                AppUtils.isInstallApp(context, "com.tencent.minihd.qq") ||
                AppUtils.isInstallApp(context, "com.tencent.eim")) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            showToast("暂未安装QQ相关软件无法发起聊天");
        }
    }
}
