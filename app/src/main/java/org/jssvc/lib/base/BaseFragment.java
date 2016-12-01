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
import android.widget.Toast;

import org.jssvc.lib.view.pDialog.XProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Fragment
 */

public abstract class BaseFragment extends Fragment {

    private Toast toast = null;//全局Toast
    private XProgressDialog progressDialog = null;//全局ProgressDialog

    private Unbinder unbinder;

    protected Context context;
    protected BaseActivity mActivity;

    protected abstract int getContentViewId();

    protected abstract void initView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, view);//绑定fragment
        this.context = getActivity();
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();//解绑
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
    protected void dealNetError(Exception e) {
        mActivity.dealNetError(e);
    }
}

