package org.jssvc.lib.fragment;

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
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.activity.AccountLibManagerActivity;
import org.jssvc.lib.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ListSelecterBean;
import org.jssvc.lib.data.DataSup;
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
public class LibAccountBoundFragment extends BaseFragment {

  @BindView(R.id.tv_school) TextView tvSchool;
  @BindView(R.id.tv_type) TextView tvType;
  @BindView(R.id.edt_account) EditText edtAccount;
  @BindView(R.id.edt_pwd) EditText edtPwd;

  List<ListSelecterBean> schoolList = new ArrayList<>();
  List<ListSelecterBean> loginTypeList = new ArrayList<>();
  ListSelecterBean currentLoginType;
  ListSelecterBean currentSchool;

  public LibAccountBoundFragment() {
  }

  @Override protected int getContentViewId() {
    return R.layout.fragment_lib_account_bound;
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
        String loginpwd = edtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(loginname) || TextUtils.isEmpty(loginpwd)) {
          showToast("帐号信息不能为空");
        } else {
          doLogin(loginname, loginpwd, currentLoginType.getId());
        }
        break;
    }
  }

  // 登录
  private void doLogin(final String loginname, final String loginpwd, final String loginType) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_LOGIN).tag(this)
        .params("number", loginname)
        .params("passwd", loginpwd)
        .params("select", loginType)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            parseHtml(response.body(), loginname, loginpwd, loginType);
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("身份验证中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // 解析网页
  private void parseHtml(String s, final String loginname, final String loginpwd,
      final String loginType) {
    String errorMsg = HtmlParseUtils.getErrMsgOnLogin(s);
    if (TextUtils.isEmpty(errorMsg)) {
      // 登录成功，提交账户到服务器
      submintThirdAccount(loginname, loginpwd, loginType);
    } else {
      // 有错误提示
      if (errorMsg.contains("认证失败")) {
        AccountLibManagerActivity activity = (AccountLibManagerActivity) getActivity();
        // 传参出去
        activity.school = currentSchool.getTitle();
        activity.name = loginname;
        activity.oldPwd = loginpwd;
        activity.type = loginType;

        // “如果认证失败，您将不能使用我的图书馆功能”
        activity.accountActivateFragment();
      } else {
        showToast(errorMsg);
      }
    }
  }

  // 提交第三方账户
  private void submintThirdAccount(String loginname, String loginpwd, String loginType) {
    OkGo.<String>post(HttpUrlParams.THIRD_ACCOUNT_BOUND).tag(this)
        .params("uid", getUid())
        .params("platform", "1")// 平台1图书馆
        .params("school_abbr", currentSchool.getTitle())//学校代码
        .params("third_account", loginname)
        .params("third_pwd", loginpwd)
        .params("third_type", loginType)
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                JSONObject jo = jsonObject.optJSONObject("data");

                showToast("绑定成功");
                DataSup.setThirdAccountStr2Local(jo.toString());
                getActivity().finish();
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
            showProgressDialog("绑定中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // 初始化字典选项
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
    final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
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

    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    selecterAdapter = new DialogListSelecterAdapter(mContext, tempList);
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
