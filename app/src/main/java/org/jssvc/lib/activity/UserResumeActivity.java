package org.jssvc.lib.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;

import static org.jssvc.lib.R.id.rl_birth;

/**
 * 个人中心
 */
public class UserResumeActivity extends BaseActivity {

  @BindView(R.id.iv_avatar) SimpleDraweeView ivAvatar;
  @BindView(R.id.tv_nick_name) TextView tvNickName;
  @BindView(R.id.tv_real_name) TextView tvRealName;
  @BindView(R.id.tv_sex) TextView tvSex;
  @BindView(R.id.tv_birth) TextView tvBirth;
  @BindView(R.id.tv_tel) TextView tvTel;
  @BindView(R.id.tv_qq) TextView tvQq;
  @BindView(R.id.tv_email) TextView tvEmail;

  int year = 2000;
  int month = 5;
  int day = 14;

  @Override protected int getContentViewId() {
    return R.layout.activity_user_resume;
  }

  @Override protected void initView() {
  }

  @OnClick({
      R.id.tv_back, R.id.rl_avatar, R.id.rl_nick_name, R.id.rl_real_name, R.id.rl_sex, rl_birth,
      R.id.rl_tel, R.id.rl_qq, R.id.rl_email
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_avatar:
        break;
      case R.id.rl_nick_name:
        break;
      case R.id.rl_real_name:
        break;
      case R.id.rl_sex:
        // 性别
        showSexSelector();
        break;
      case rl_birth:
        // 生日
        showDatePickerDialog();
        break;
      case R.id.rl_tel:
        break;
      case R.id.rl_qq:
        break;
      case R.id.rl_email:
        break;
    }
  }

  // 性别
  private void showSexSelector() {
    final String[] arr = new String[] { "男", "女" };
    new AlertDialog.Builder(this).setItems(arr, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        tvSex.setText(arr[which]);
      }
    }).show();
  }

  // 生日
  private void showDatePickerDialog() {
    DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
        year = myyear;
        month = monthOfYear;
        day = dayOfMonth;
        tvBirth.setText(year + "-" + (month + 1) + "-" + day);
      }
    }, year, month, day);
    dpd.show();
  }
}
