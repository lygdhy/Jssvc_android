package org.jssvc.lib.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountActivateActivity;
import org.jssvc.lib.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ListSelecterBean;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.DividerItemDecoration;

/**
 * <pre>
 *     author : lygdh
 *     time   : 2017/06/12
 *     desc   : 图书馆账号绑定
 *     version: 1.0
 * </pre>
 */
public class AccountBoundFragment extends BaseFragment {

  @BindView(R.id.tv_school) TextView tvSchool;
  @BindView(R.id.tv_type) TextView tvType;
  @BindView(R.id.edt_account) EditText edtAccount;
  @BindView(R.id.edt_pwd) EditText edtPwd;

  List<ListSelecterBean> schoolList = new ArrayList<>();
  List<ListSelecterBean> loginTypeList = new ArrayList<>();
  ListSelecterBean currentLoginType;
  ListSelecterBean currentSchool;

  public AccountBoundFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_account_bound;
  }

  @Override protected void initView() {
    initSelecters();
  }

  @OnClick({ R.id.tv_school, R.id.tv_type, R.id.btn_submit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_school:// 选择院校
        showSelecterDialog(0);
        break;
      case R.id.tv_type: // 选择登录方式
        showSelecterDialog(1);
        break;
      case R.id.btn_submit:// 登录验证
        String loginname = edtAccount.getText().toString().trim();
        final String loginpwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
          showToast("登录信息不能为空");
        } else {
          showProgressDialog("登录中...");

          AccountPref.saveLoginAccoundNumber(context, loginname);
          AccountPref.saveLoginType(context, currentLoginType.getId());
          OkGo.post(HttpUrlParams.URL_LIB_LOGIN)
              .tag(this)
              .params("number", loginname)
              .params("passwd", loginpwd)
              .params("select", currentLoginType.getId())
              .execute(new StringCallback() {
                @Override public void onSuccess(String s, Call call, Response response) {
                  dissmissProgressDialog();
                  // s 即为所需要的结果
                  parseHtml(s, loginpwd);
                }

                @Override public void onError(Call call, Response response, Exception e) {
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
  private void parseHtml(String s, String loginpwd) {
    String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 保存密码
      AccountPref.saveLoginAccoundPwd(context, loginpwd);
      // 账号统计
      MobclickAgent.onProfileSignIn(AccountPref.getLogonType(context).toUpperCase(),
          AccountPref.getLogonAccoundNumber(context));
      // 登录成功
      showToast("===绑定成功===");
      getActivity().finish();
    } else {
      // 有错误提示
      if (errorMsg.contains("认证失败")) {
        // “如果认证失败，您将不能使用我的图书馆功能”
        startActivity(new Intent(context, AccountActivateActivity.class));
      } else {
        showToast(errorMsg);
        AccountPref.removeLogonAccoundPwd(context);
      }
    }
  }

  private void initSelecters() {
    loginTypeList.clear();
    loginTypeList.add(new ListSelecterBean(R.drawable.icon_card, "cert_no", "证件号", ""));
    loginTypeList.add(new ListSelecterBean(R.drawable.icon_tiaoma, "bar_no", "条码号", ""));
    loginTypeList.add(new ListSelecterBean(R.drawable.icon_email, "email", "Email", ""));

    schoolList.clear();
    schoolList.add(new ListSelecterBean(R.drawable.icon_school_jssvc, "jssvc", "苏州市职业大学", ""));
    schoolList.add(new ListSelecterBean(R.drawable.icon_school_siit, "siit", "苏州工业职业技术学院", ""));
    schoolList.add(
        new ListSelecterBean(R.drawable.icon_school_lyycj, "lyycj", "苏州旅游与财经高等职业技术学校", ""));
    schoolList.add(
        new ListSelecterBean(R.drawable.icon_school_szjsjt, "szjsjt", "苏州建设交通高等职业技术学校", ""));

    currentLoginType = loginTypeList.get(0);
    tvType.setText(currentLoginType.getTitle());
    edtAccount.setHint("请输入" + currentLoginType.getTitle());

    currentSchool = schoolList.get(0);
    tvSchool.setText(currentSchool.getTitle());
  }

  // 登录方式选择 code 0院校  1登录方式
  private void showSelecterDialog(final int code) {
    final AlertDialog dlg = new AlertDialog.Builder(context).create();
    dlg.show();
    dlg.getWindow()
        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = dlg.getWindow();
    window.setContentView(R.layout.dialog_list_select_layout);

    TextView tvDialogTitle = (TextView) window.findViewById(R.id.tvDialogTitle);
    TextView tvDialogSubTitle = (TextView) window.findViewById(R.id.tvDialogSubTitle);
    List<ListSelecterBean> tempList = new ArrayList<>();
    if (code == 0) {
      tempList.addAll(schoolList);
      tvDialogTitle.setText("请选择院校");
    }
    if (code == 1) {
      tempList.addAll(loginTypeList);
      tvDialogTitle.setText("请选择账号类型");
    }
    tvDialogSubTitle.setVisibility(View.GONE);

    DialogListSelecterAdapter selecterAdapter;
    RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recyclerView);

    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    selecterAdapter = new DialogListSelecterAdapter(context, tempList);
    recyclerView.setAdapter(selecterAdapter);

    selecterAdapter.setOnItemClickListener(new DialogListSelecterAdapter.IMyViewHolderClicks() {
      @Override public void onItemClick(View view, ListSelecterBean item) {
        if (code == 0) {
          currentSchool = item;
          tvSchool.setText(currentSchool.getTitle());
        }
        if (code == 1) {
          currentLoginType = item;
          tvType.setText(currentLoginType.getTitle());
          edtAccount.setHint("请输入" + currentLoginType.getTitle());
        }
        dlg.dismiss();
      }
    });
  }
}
