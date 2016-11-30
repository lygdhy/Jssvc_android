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

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Fragment
 */

public abstract class BaseFragment extends Fragment {

    private Toast toast = null;//全局Toast

    private Unbinder unbinder;

    protected Context context;

    protected abstract int getContentViewId();

    protected abstract void initView();

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
     * 全局Toast
     */
    protected void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}

