package org.jssvc.lib.network.rx;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import org.jssvc.lib.network.result.HttpResponse;
import org.jssvc.lib.view.pDialog.XProgressDialog;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by jjj on 2018/1/30.
 *
 * @description:
 */

public abstract class BaseObserver<T> implements Observer<HttpResponse<T>> {

    private XProgressDialog mProgressDialog = null;
    private final int RESPONSE_CODE_OK = 0;       //自定义的业务逻辑，成功返回积极数据
    private final int RESPONSE_CODE_FAILED = -1;  //返回数据失败,严重的错误
    private Context mContext;
    private static Gson gson = new Gson();
    private int errorCode;
    private String errorMsg = "未知的错误！";

    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * @param mContext
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public BaseObserver(Context mContext, boolean showProgress) {
        this.mContext = mContext;
        if (showProgress) {
            if (mProgressDialog == null) {
                mProgressDialog = new XProgressDialog(mContext, XProgressDialog.THEME_HORIZONTAL_SPOT);
            }
            mProgressDialog.show();
        }
    }

    @Override
    public final void onSubscribe(Disposable d) {
        //dddddddddddddd
    }

    @Override
    public final void onNext(HttpResponse<T> response) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (response.getCode() == RESPONSE_CODE_OK) {
            onSuccess(response.getData());
        } else {
            onFailure(response.getCode(), response.getMessage());
        }
    }

    @Override
    public final void onError(Throwable t) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            errorCode = httpException.code();
            errorMsg = httpException.getMessage();
            getErrorMsg(httpException);
        } else if (t instanceof SocketTimeoutException) {  //VPN open
            errorCode = RESPONSE_CODE_FAILED;
            errorMsg = "服务器响应超时";
        }

        // .....其他的异常处理
        onFailure(errorCode, errorMsg);
    }

    /**
     * 简单的把Dialog 处理掉
     */
    @Override
    public final void onComplete() {
    }

    /**
     * Default error dispose!
     * 一般的就是 AlertDialog 或 SnackBar
     *
     * @param code
     * @param message
     */
    @CallSuper  //if overwrite,you should let it run.
    public void onFailure(int code, String message) {
        if (code == RESPONSE_CODE_FAILED && mContext != null) {
            ToastUtils.showShort(message + code);
        } else {
            disposeEorCode(message, code);
        }
    }

    /**
     * 对通用问题的统一拦截处理
     *
     * @param code
     */
    private final void disposeEorCode(String message, int code) {
        switch (code) {
            case 101:
            case 401:
        }
        Toast.makeText(mContext, message + "   code=" + code, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取详细的错误的信息 errorCode,errorMsg ,   这里和Api 结构有关，这里和Api 结构有关 ！
     * 以登录的时候的Grant_type 故意写错为例子,http 应该是直接的返回401=httpException.code()
     * 但是是怎么导致401的？我们的服务器会在respose.errorBody 中的content 中说明
     */
    private final void getErrorMsg(HttpException httpException) {
        String errorBodyStr = "";
        try {   //我们的项目需要的UniCode转码，不是必须要的！
            errorBodyStr = httpException.response().errorBody().string();
        } catch (IOException ioe) {
            Log.e("errorBodyStr ioe:", ioe.toString());
        }
        try {
            HttpResponse errorResponse = gson.fromJson(errorBodyStr, HttpResponse.class);
            if (null != errorResponse) {
                errorCode = errorResponse.getCode();
                errorMsg = errorResponse.getMessage();
            } else {
                errorCode = RESPONSE_CODE_FAILED;
                errorMsg = "ErrorResponse is null";
            }
        } catch (Exception jsonException) {
            errorCode = RESPONSE_CODE_FAILED;
            errorMsg = "http请求错误Json 信息异常";
            jsonException.printStackTrace();
        }
    }

}
