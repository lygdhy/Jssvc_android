package org.jssvc.lib.base.impl;

import android.content.Intent;
import android.os.Bundle;

import org.jssvc.lib.view.TitleView;

/**
 * Created by jjj on 2018/1/26.
 *
 * @description:
 */

public interface IBaseActivity {

    void beforeOnCreate(Bundle savedInstanceState);

    void afterOnCreate(Bundle savedInstanceState);

    int getContentViewId();

    int setTransBarId();

    void initTitle(TitleView titleView);

    void initView();

    void initData();

    void startAnimActivity(Class<?> cla);

    void startAnimActivity(Intent intent);

    void showProgressDialog();

    void showProgressDialog(String msg);

    void dissmissProgressDialog();

    void showToast(String msg);

    boolean setEventBus();
}
