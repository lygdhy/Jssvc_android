package org.jssvc.lib.module.login;

import com.blankj.utilcode.util.LogUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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

        Flowable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtils.e("aLong=" + aLong);
                        if (aLong > 9) {
//                            view.onLoginComplete();
                        }
                        view.onLoginSuccess("登录成功" + aLong);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e("e====" + t);
                    }

                    @Override
                    public void onComplete() {
                        view.onLoginSuccess("结束");
                    }
                });
//        final Observable observable = Observable
//                .interval(2000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
//        final Observer observer = new Observer<Long>() {
//            Disposable disposable;
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable=d;
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                if (aLong > 10) {
//                    disposable.dispose();
//                    view.onLoginSuccess("登录成功" + aLong);
//                    view.onLoginComplete();
//                }
//                view.onLoginSuccess("登录成功" + aLong);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                LogUtils.e("e====" + e);
//            }
//
//            @Override
//            public void onComplete() {
//                view.onLoginSuccess("结束");
//            }
//        };
//        observable.subscribe(observer);
    }
}
