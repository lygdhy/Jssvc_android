package org.jssvc.lib.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.User;
import org.jssvc.lib.data.AccountPref;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;

/**
 * 证件信息
 */
public class CardInfoActivity extends BaseActivity {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.ivLevel) ImageView ivLevel;
  @BindView(R.id.tvName) TextView tvName;
  @BindView(R.id.tvLevel1) TextView tvLevel1;
  @BindView(R.id.tvLevel2) TextView tvLevel2;
  @BindView(R.id.tvUserDepart) TextView tvUserDepart;
  @BindView(R.id.tvUserNum) TextView tvUserNum;
  @BindView(R.id.tvUserType) TextView tvUserType;
  @BindView(R.id.tvUserLevel) TextView tvUserLevel;
  @BindView(R.id.tvCardStart) TextView tvCardStart;
  @BindView(R.id.tvCardEnd) TextView tvCardEnd;
  @BindView(R.id.tvRules) TextView tvRules;
  @BindView(R.id.tvArrearage) TextView tvArrearage;
  @BindView(R.id.scrollView) ScrollView scrollView;

  @Override protected int getContentViewId() {
    return R.layout.activity_card_info;
  }

  @Override protected void initView() {
    scrollView.setVisibility(View.GONE);

    if (AccountPref.isLogon(context)) {
      getUserInfoByNet();
    } else {
      startActivity(new Intent(context, LoginActivity.class));
      finish();
    }
  }

  @OnClick({ R.id.tvBack, R.id.ivLevel }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
      case R.id.ivLevel:
        break;
    }
  }

  // 获取个人信息
  private void getUserInfoByNet() {
    OkGo.<String>post(HttpUrlParams.URL_LIB_ACCOUND).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(Response<String> response) {
        parseHtml(response.body());
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

    //OkGo.post(HttpUrlParams.URL_LIB_ACCOUND).tag(this).execute(new StringCallback() {
    //  @Override public void onSuccess(String s, Call call, Response response) {
    //    dissmissProgressDialog();
    //    // s 即为所需要的结果
    //    parseHtml(s);
    //  }
    //
    //  @Override public void onError(Call call, Response response, Exception e) {
    //    super.onError(call, response, e);
    //    dissmissProgressDialog();
    //    dealNetError(e);
    //  }
    //});
  }

  // 解析网页
  private void parseHtml(String s) {
    User user = HtmlParseUtils.getUserInfo(s);
    if (!TextUtils.isEmpty(user.getUserid())) {
      AccountPref.saveLogonUser(context, user);

      scrollView.setVisibility(View.VISIBLE);
      tvName.setText(user.getUsername());

      tvUserDepart.setText(user.getDepartment());

      tvUserNum.setText(user.getUserid());
      tvUserType.setText(user.getType());
      tvUserLevel.setText(user.getReadType());
      tvCardStart.setText(user.getCardStartDate());
      tvCardEnd.setText(user.getCardEndDate());

      tvRules.setText(user.getViolation());
      tvArrearage.setText(user.getDebt());

      // 解析借阅次数
      if (!TextUtils.isEmpty(user.getReadTimes())) {
        String timestr = user.getReadTimes().replaceAll("册次", "");
        try {
          int times = Integer.parseInt(timestr);
          setLevelHead(user.getSex(), times);
        } catch (Exception e) {
          tvLevel1.setText("" + user.getReadTimes());
          tvLevel2.setText("null");
        }
      }
    } else {
      showToast("解析失败");
    }
  }

  //加载头像和等级称号
  private void setLevelHead(String sex, int number) {
    int level = getLevelByTimes(number);
    String levelname = "";
    switch (level) {
      case 1:
        levelname = "一穷二白";
        break;
      case 2:
        levelname = "初露头角";
        break;
      case 3:
        levelname = "读书学徒";
        break;
      case 4:
        levelname = "读书新秀";
        break;
      case 5:
        levelname = "略有小成";
        break;
      case 6:
        levelname = "读书达人";
        break;
      case 7:
        levelname = "学富五车";
        break;
      case 8:
        levelname = "我是学霸";
        break;
      case 9:
        levelname = "超级学霸";
        break;
    }
    if (sex.equals("男")) {
      ivLevel.getDrawable().setLevel(level);
    } else {
      ivLevel.getDrawable().setLevel(level + 10);
    }
    tvLevel1.setText("" + number); // 等级信息
    tvLevel2.setText("\"" + levelname + "\""); // 等级信息
  }

  // 获取读者等级
  public static int getLevelByTimes(int number) {
    int level = 1; // 对应的读者等级
    if (number == 0) level = 1;
    if (number >= 1) level = 2;
    if (number >= 5) level = 3;
    if (number >= 10) level = 4;
    if (number >= 20) level = 5;
    if (number >= 30) level = 6;
    if (number >= 50) level = 7;
    if (number >= 80) level = 8;
    if (number >= 150) level = 9;
    return level;
  }
}
