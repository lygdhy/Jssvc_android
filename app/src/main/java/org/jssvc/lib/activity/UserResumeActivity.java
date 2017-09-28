package org.jssvc.lib.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MemberBean;
import org.jssvc.lib.utils.ImageLoader;

import static org.jssvc.lib.data.DataSup.getLocalMemberBean;

/**
 * 个人中心
 */
public class UserResumeActivity extends BaseActivity {

  @BindView(R.id.iv_avatar) ImageView ivAvatar;
  @BindView(R.id.tv_nick_name) TextView tvNickName;
  @BindView(R.id.tv_real_name) TextView tvRealName;
  @BindView(R.id.tv_sex) TextView tvSex;
  @BindView(R.id.tv_birth) TextView tvBirth;
  @BindView(R.id.tv_tel) TextView tvTel;
  @BindView(R.id.tv_qq) TextView tvQq;
  @BindView(R.id.tv_email) TextView tvEmail;

  MemberBean bean;
  int year = 2000;
  int month = 5;
  int day = 14;

  @Override protected int getContentViewId() {
    return R.layout.activity_user_resume;
  }

  @Override protected void initView() {
    bean = getLocalMemberBean();
    if (bean != null) {
      ImageLoader.with(mContext, ivAvatar, bean.getAvatar());
      tvNickName.setText(bean.getNickname());
      tvRealName.setText(bean.getRealname());
      tvSex.setText(bean.getSex());
      tvBirth.setText(bean.getBirthday());
      tvTel.setText(bean.getPhone());
      tvQq.setText(bean.getQq());
      tvEmail.setText(bean.getEmail());
    } else {
      showToast("信息丢失，请重新登录");
      finish();
    }
  }

  @OnClick({
      R.id.tv_back, R.id.rl_avatar, R.id.rl_nick_name, R.id.rl_real_name, R.id.rl_sex,
      R.id.rl_birth, R.id.rl_qq, R.id.rl_email
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_avatar:
        break;
      case R.id.rl_nick_name:
        showInputDialog(tag_nick_name, "编辑昵称", tvNickName.getHint().toString(),
            tvNickName.getText().toString().trim());
        break;
      case R.id.rl_real_name:
        showInputDialog(tag_real_name, "编辑姓名", tvRealName.getHint().toString(),
            tvRealName.getText().toString().trim());
        break;
      case R.id.rl_sex:
        // 性别
        showSexSelector();
        break;
      case R.id.rl_birth:
        // 生日
        showDatePickerDialog();
        break;
      case R.id.rl_qq:
        showInputDialog(tag_qq, "编辑QQ", tvQq.getHint().toString(),
            tvQq.getText().toString().trim());
        break;
      case R.id.rl_email:
        showInputDialog(tag_email, "编辑邮箱", tvEmail.getHint().toString(),
            tvEmail.getText().toString().trim());
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
    DatePickerDialog dpd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
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

  AlertDialog alertDialog;
  final int tag_real_name = 1;
  final int tag_nick_name = 2;
  final int tag_qq = 3;
  final int tag_email = 4;

  private void showInputDialog(final int tag, String title, String hint, String value) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    builder.setTitle("");
    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
    View layout = inflater.inflate(R.layout.dialog_normal_input_layout, null);

    TextView tvTitle = (TextView) layout.findViewById(R.id.title);
    final EditText edtMessage = (EditText) layout.findViewById(R.id.message);

    Button tvCancel = (Button) layout.findViewById(R.id.btn_cancel);
    Button btnSubmit = (Button) layout.findViewById(R.id.btn_submit);

    tvTitle.setText(title);
    edtMessage.setHint(hint);
    edtMessage.setText(value);
    edtMessage.requestFocus();
    edtMessage.setSelection(value.length());

    // 取消
    tvCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        alertDialog.dismiss();
      }
    });

    // 提交
    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String value = edtMessage.getText().toString().trim();
        switch (tag) {
          case tag_real_name:
            tvRealName.setText(value);
            break;
          case tag_nick_name:
            tvNickName.setText(value);
            break;
          case tag_qq:
            tvQq.setText(value);
            break;
          case tag_email:
            tvEmail.setText(value);
            break;
        }
        alertDialog.dismiss();
      }
    });
    builder.setView(layout);
    builder.setCancelable(false);
    alertDialog = builder.create();
    alertDialog.show();
  }
}
