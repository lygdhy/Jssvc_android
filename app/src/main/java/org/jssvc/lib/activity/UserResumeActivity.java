package org.jssvc.lib.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MemberBean;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.GlideImageLoader;
import org.jssvc.lib.utils.ImageLoader;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static org.jssvc.lib.data.DataSup.getLocalMemberBean;

/**
 * 个人中心
 */
public class UserResumeActivity extends BaseActivity {

  @BindView(R.id.iv_avatar) ImageView ivAvatar;
  @BindView(R.id.tv_nick_name) TextView tvNickName;
  @BindView(R.id.tv_signature) TextView tvSignature;
  @BindView(R.id.tv_real_name) TextView tvRealName;
  @BindView(R.id.tv_sex) TextView tvSex;
  @BindView(R.id.tv_birth) TextView tvBirth;
  @BindView(R.id.tv_tel) TextView tvTel;
  @BindView(R.id.tv_qq) TextView tvQq;
  @BindView(R.id.tv_email) TextView tvEmail;

  ArrayList<ImageItem> images = null;
  private ImagePicker imagePicker;
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
      ImageLoader.withCircle(mContext, ivAvatar, bean.getAvatar());
      tvNickName.setText(bean.getNickname());
      tvSignature.setText(bean.getSignature());
      tvRealName.setText(bean.getRealname());
      tvSex.setText(bean.getSex());
      tvBirth.setText(bean.getBirthday());
      tvTel.setText(bean.getPhone());
      tvQq.setText(bean.getQq());
      tvEmail.setText(bean.getEmail());

      // 生日
      String birth = bean.getBirthday();
      if (!TextUtils.isEmpty(birth)) {
        String[] arr = birth.split("-");
        if (arr.length == 3) {
          year = TextUtils.isEmpty(arr[0]) ? 2000 : Integer.parseInt(arr[0]);
          month = TextUtils.isEmpty(arr[1]) ? 5 : Integer.parseInt(arr[1]) - 1;
          day = TextUtils.isEmpty(arr[2]) ? 14 : Integer.parseInt(arr[2]);
        }
      }
    } else {
      showToast("信息丢失，请重新登录");
      finish();
    }

    // 初始化ImagePicker
    imagePicker = ImagePicker.getInstance();
    imagePicker.setImageLoader(new GlideImageLoader());
    imagePicker.setShowCamera(true);  //显示拍照按钮
    imagePicker.setCrop(true);        //允许裁剪（单选才有效）
    imagePicker.setSaveRectangle(true); //是否按矩形区域保存
    imagePicker.setMultiMode(false);// 单选
    imagePicker.setSelectLimit(1);    //选中数量限制
    imagePicker.setStyle(CropImageView.Style.RECTANGLE);// 矩形
    imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
    imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
    imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
    imagePicker.setOutPutY(800);//保存文件的高度。单位像素
  }

  @OnClick({
      R.id.tv_back, R.id.rl_avatar, R.id.rl_nick_name, R.id.rl_signature, R.id.rl_real_name,
      R.id.rl_sex, R.id.rl_birth, R.id.rl_qq, R.id.rl_email
  }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.rl_avatar:
        // 添加照片
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 100);
        break;
      case R.id.rl_nick_name:
        showInputDialog(tag_nick_name, "编辑昵称", tvNickName.getHint().toString(),
            tvNickName.getText().toString().trim());
        break;
      case R.id.rl_signature:
        showInputDialog(tag_signature, "个性签名", tvSignature.getHint().toString(),
            tvSignature.getText().toString().trim());
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

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
      if (data != null && requestCode == 100) {
        images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
        // Luban图片压缩
        Luban.with(mContext)
            .load(images.get(0).path)
            .ignoreBy(100)
            .setTargetDir(getPath())
            .setCompressListener(new OnCompressListener() {
              @Override public void onStart() {
                showProgressDialog("压缩中...");
              }

              @Override public void onSuccess(File file) {
                uploadFiles(file);
              }

              @Override public void onError(Throwable e) {
                dissmissProgressDialog();
                showToast(e.getMessage());
              }
            })
            .launch();
      } else {
        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private String getPath() {
    String path = Environment.getExternalStorageDirectory() + "/jssvc/compress/";
    File file = new File(path);
    if (file.mkdirs()) {
      return path;
    }
    return path;
  }

  // 修改用户信息
  public void modifyAccountInfo(String k, String v) {
    OkGo.<String>post(HttpUrlParams.MODIFY_ACCOUNT).tag(this)
        .params("key", k)
        .params("value", v)
        .params("uid", bean.getId())
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            try {
              JSONObject jsonObject = new JSONObject(response.body());
              if (jsonObject.optInt("code") == 200) {
                // 重置用户信息
                JSONObject jo = jsonObject.optJSONObject("data");
                DataSup.setMemberStr2Local(jo.toString());
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
            showProgressDialog("保存中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  // 上传图片
  private void uploadFiles(File file) {
    OkGo.<String>post(HttpUrlParams.UPLOAD_FILES).tag(this)
        .params("file", file)
        .execute(new StringCallback() {
          @Override public void onSuccess(com.lzy.okgo.model.Response<String> response) {
            String img_path = response.body();
            ImageLoader.withCircle(mContext, ivAvatar, img_path);
            modifyAccountInfo("avatar", img_path);
          }

          @Override public void onError(com.lzy.okgo.model.Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog("上传中...");
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }

          @Override public void uploadProgress(Progress progress) {
            super.uploadProgress(progress);
            showProgressDialog("上传中..." + progress.fraction);
          }
        });
  }

  // 性别
  private void showSexSelector() {
    final String[] arr = new String[] { "男", "女" };
    new AlertDialog.Builder(this).setItems(arr, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        tvSex.setText(arr[which]);
        // 0'未知'  1 '男'  2'女'  3'保密'
        modifyAccountInfo("sex", String.valueOf(which + 1));
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
        String birth = year + "-" + (month + 1) + "-" + day;
        tvBirth.setText(birth);
        modifyAccountInfo("birthday", birth);
      }
    }, year, month, day);
    dpd.show();
  }

  AlertDialog alertDialog;
  final int tag_real_name = 1;
  final int tag_signature = 2;
  final int tag_nick_name = 3;
  final int tag_qq = 4;
  final int tag_email = 5;

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
            modifyAccountInfo("realname", value);
            break;
          case tag_signature:
            tvSignature.setText(value);
            modifyAccountInfo("signature", value);
            break;
          case tag_nick_name:
            tvNickName.setText(value);
            modifyAccountInfo("nickname", value);
            break;
          case tag_qq:
            tvQq.setText(value);
            modifyAccountInfo("qq", value);
            break;
          case tag_email:
            tvEmail.setText(value);
            modifyAccountInfo("email", value);
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
