package org.jssvc.lib.base;

/**
 * Created by lygdh on 2016/11/21.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.lzy.okgo.model.Response;

/**
 * 基类Fragment
 */

public abstract class BaseFragment extends Fragment {

  private Unbinder unbinder;

  protected Context mContext;
  protected BaseActivity mActivity;

  protected abstract int getContentViewId();

  protected abstract void initView();

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mActivity = (BaseActivity) getActivity();
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getContentViewId(), container, false);
    unbinder = ButterKnife.bind(this, view);//绑定fragment
    this.mContext = getActivity();
    initView();
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();//解绑
  }

  // 获取会员Id
  public String getUid() {
    return mActivity.getUid();
  }

  /**
   * show Toast
   */
  protected void showToast(String msg) {
    mActivity.showToast(msg);
  }

  /**
   * show ProgressDialog
   */
  protected void showProgressDialog() {
    mActivity.showProgressDialog();
  }

  /**
   * show ProgressDialog
   */
  protected void showProgressDialog(String msg) {
    mActivity.showProgressDialog(msg);
  }

  /**
   * dissmiss ProgressDialog
   */
  protected void dissmissProgressDialog() {
    mActivity.dissmissProgressDialog();
  }

  /**
   * 网络错误处理
   */
  protected void dealNetError(Response response) {
    mActivity.dealNetError(response);
  }
}

