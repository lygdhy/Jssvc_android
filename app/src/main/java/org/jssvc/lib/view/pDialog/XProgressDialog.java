package org.jssvc.lib.view.pDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.jssvc.lib.R;

/**
 * Created by lygdh on 2016/11/30.
 */

public class XProgressDialog extends AlertDialog {

  // theme类型
  public static final int THEME_HORIZONTAL_SPOT = 1;
  public static final int THEME_CIRCLE_PROGRESS = 2;

  protected View progressBar;
  protected TextView message;
  protected String messageText = "请稍后...";
  protected int theme = THEME_HORIZONTAL_SPOT;

  public XProgressDialog(Context context) {
    super(context);
  }

  public XProgressDialog(Context context, String message) {
    super(context);
    this.messageText = message;
  }

  public XProgressDialog(Context context, int theme) {
    super(context);
    this.theme = theme;
  }

  public XProgressDialog(Context context, String message, int theme) {
    super(context);
    this.messageText = message;
    this.theme = theme;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    switch (theme) {
      case THEME_CIRCLE_PROGRESS:
        setContentView(R.layout.view_xprogressdialog_circle_progress);
        break;
      default:
        setContentView(R.layout.view_xprogressdialog_spot);
        break;
    }
    message = (TextView) findViewById(R.id.message);
    progressBar = findViewById(R.id.progress);
    if (message != null && !TextUtils.isEmpty(messageText)) {
      message.setText(messageText);
    }
    getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    setCanceledOnTouchOutside(false);
    setCancelable(false);
  }

  public void setMessage(String message) {
    this.messageText = message;
  }

  /**
   * 获取显示文本控件
   */
  protected TextView getMessageView() {
    return message;
  }

  /**
   * 获取显示进度的控件
   */
  protected View getProgressView() {
    return progressBar;
  }

  @Override public void show() {
    super.show();
  }
}
