package org.jssvc.lib.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import org.jssvc.lib.base.activity.BaseActivity;
import org.jssvc.lib.base.impl.IBaseFragment;

/**
 * Created by jjj on 2018/1/26.
 *
 * @description:
 */

public abstract class BaseFragment extends Fragment implements IBaseFragment {

    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void startAnimActivity(Class<?> cla) {
        mActivity.startAnimActivity(cla);
    }

    @Override
    public void startAnimActivity(Intent intent) {
        mActivity.startAnimActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        mActivity.showProgressDialog();
    }

    @Override
    public void showProgressDialog(String msg) {
        mActivity.showProgressDialog(msg);
    }

    @Override
    public void dissmissProgressDialog() {
        mActivity.dissmissProgressDialog();
    }

    @Override
    public void showToast(String msg) {
        mActivity.showToast(msg);
    }

    /**
     * 是否开启EventBus
     *
     * @return
     */
    @Override
    public boolean setEventBus() {
        return false;
    }
}
