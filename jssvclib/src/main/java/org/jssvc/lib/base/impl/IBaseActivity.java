package org.jssvc.lib.base.impl;

import android.content.Intent;

/**
 * Created by jjj on 2018/1/26.
 *
 * @description:
 */

public interface IBaseActivity {
    int getViewByXml();

    void initView();

    void initData();

    void startAnimActivity(Class<?> cla);

    void startAnimActivity(Intent intent);

    void showProgressDialog();

    void showProgressDialog(String msg);

    void dissmissProgressDialog();

    void showToast(String msg);
}
