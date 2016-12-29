package org.jssvc.lib.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.ListSelecterBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtPwd)
    EditText edtPwd;
    @BindView(R.id.tvLoginType)
    TextView tvLoginType;

    List<ListSelecterBean> loginTypeList = new ArrayList<>();
    ListSelecterBean currentLoginType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initLoginType();
    }

    private void initLoginType() {
        loginTypeList.add(new ListSelecterBean(R.drawable.icon_card, "cert_no", "证件号", ""));
        loginTypeList.add(new ListSelecterBean(R.drawable.icon_tiaoma, "bar_no", "条码号", ""));
        loginTypeList.add(new ListSelecterBean(R.drawable.icon_email, "email", "Email", ""));

        currentLoginType = loginTypeList.get(0);
        tvLoginType.setText(currentLoginType.getTitle());
        edtName.setHint("请输入" + currentLoginType.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        edtName.setText(AccountPref.getLogonAccoundNumber(context));
        edtPwd.setText(AccountPref.getLogonAccoundPwd(context));
    }

    @OnClick({R.id.tvBack, R.id.tvLoginType, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tvLoginType:
                // 登陆方式
                showLoginTypeDialog();
                break;
            case R.id.btnLogin:
                // 登陆
                String loginname = edtName.getText().toString().trim();
                String loginpwd = edtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
                    showToast("登陆信息不能为空");
                } else {
                    showProgressDialog("登录中...");

                    AccountPref.saveLoginAccoundNumber(context, loginname);
                    AccountPref.saveLoginAccoundPwd(context, loginpwd);
                    AccountPref.saveLoginType(context, currentLoginType.getId());
                    OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
                            .tag(this)
                            .params("number", loginname)
                            .params("passwd", loginpwd)
                            .params("select", currentLoginType.getId())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    dissmissProgressDialog();
                                    // s 即为所需要的结果
                                    parseHtml(s);
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    dissmissProgressDialog();
                                    dealNetError(e);
                                }

                            });
                }
                break;
        }
    }

    // 解析网页
    private void parseHtml(String s) {
        String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
        if (TextUtils.isEmpty(errorMsg)) {
            // 账号统计
            MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(), AccountPref.getLogonAccoundNumber(context));
            // 登陆成功
            finish();
        } else {
            // 有错误提示
            if (errorMsg.contains("认证失败")) {
                // “如果认证失败，您将不能使用我的图书馆功能”
                startActivity(new Intent(context, RegisterActivity.class));
            } else {
                showToast(errorMsg);
                AccountPref.removeLogonAccoundPwd(context);
            }
        }
    }

    // 登陆方式选择
    private void showLoginTypeDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_list_select_layout);

        TextView tvDialogTitle = (TextView) window.findViewById(R.id.tvDialogTitle);
        TextView tvDialogSubTitle = (TextView) window.findViewById(R.id.tvDialogSubTitle);
        tvDialogTitle.setText("请选择登陆方式");
        tvDialogSubTitle.setVisibility(View.GONE);

        DialogListSelecterAdapter selecterAdapter;
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        selecterAdapter = new DialogListSelecterAdapter(context, loginTypeList);
        recyclerView.setAdapter(selecterAdapter);

        selecterAdapter.setOnItemClickListener(new DialogListSelecterAdapter.IMyViewHolderClicks() {
            @Override
            public void onItemClick(View view, ListSelecterBean item) {
                currentLoginType = item;
                tvLoginType.setText(currentLoginType.getTitle());
                edtName.setHint("请输入" + currentLoginType.getTitle());
                dlg.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }
}
