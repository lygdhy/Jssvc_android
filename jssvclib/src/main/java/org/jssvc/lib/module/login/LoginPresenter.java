package org.jssvc.lib.module.login;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jjj on 2018/1/31.
 *
 * @description:
 */

public class LoginPresenter {

    LoginContract.View view;

    @Inject
    LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    public void login() {
        new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {

            }
        };
        final Observable observable = Observable
                .interval(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        final Observer observer = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Long aLong) {
                if (aLong > 10) {
                    view.onLoginSuccess("登录成功" + aLong);
                    view.onLoginComplete();
                }
                view.onLoginSuccess("登录成功" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("e===="+e);
            }

            @Override
            public void onComplete() {
                view.onLoginSuccess("结束");
            }
        };
        observable.subscribe(observer);
    }
}
