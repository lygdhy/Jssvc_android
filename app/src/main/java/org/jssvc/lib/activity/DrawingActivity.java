package org.jssvc.lib.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.OnClick;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.HttpUrlParams;

/**
 * 绘画
 */
public class DrawingActivity extends BaseActivity {

  @BindView(R.id.signature_pad) SignaturePad mSignaturePad;
  @BindView(R.id.clear_button) Button mClearButton;
  @BindView(R.id.save_button) Button mSaveButton;

  @Override protected int getContentViewId() {
    return R.layout.activity_drawing;
  }

  @Override protected void initView() {
    mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
      @Override public void onStartSigning() {
      }

      @Override public void onSigned() {
        mSaveButton.setEnabled(true);
        mClearButton.setEnabled(true);
      }

      @Override public void onClear() {
        mSaveButton.setEnabled(false);
        mClearButton.setEnabled(false);
      }
    });
  }

  @OnClick({ R.id.opt_back, R.id.clear_button, R.id.save_button })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.opt_back:
        finish();
        break;
      case R.id.clear_button:
        mSignaturePad.clear();
        break;
      case R.id.save_button:
        File file = saveBitmap2File(mSignaturePad.getSignatureBitmap());
        uploadFiles(file);
        break;
    }
  }

  // 上传图片
  private void uploadFiles(File file) {
    OkGo.<String>post(HttpUrlParams.UPLOAD_FILES).tag(this)
        .params("file", file)
        .execute(new StringCallback() {
          @Override public void onSuccess(com.lzy.okgo.model.Response<String> response) {
            String img_path = response.body();
            showToast(img_path);
          }

          @Override public void onError(com.lzy.okgo.model.Response<String> response) {
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

          @Override public void uploadProgress(Progress progress) {
            super.uploadProgress(progress);
            showToast("进度" + progress.fraction + "   配速" + progress.speed + "byte/s");
          }
        });
  }

  // 保存图片到文本
  private File saveBitmap2File(Bitmap bitmap) {
    File file = null;
    try {
      file = new File(Environment.getExternalStorageDirectory(),
          "/jssvc/" + "draw_" + System.currentTimeMillis() + ".jpg");
      if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
      saveBitmapToJPG(bitmap, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }

  // 写入图片
  public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
    Bitmap newBitmap =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);
    canvas.drawColor(Color.WHITE);
    canvas.drawBitmap(bitmap, 0, 0, null);
    OutputStream stream = new FileOutputStream(photo);
    newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
    stream.close();
  }
}
