package org.jssvc.lib.network.result;

/**
 * Created by jjj on 2018/1/30.
 *
 * @description:
 */

public class HttpResponse<T> {

    private int code;
    private String message;
    private T data;    //泛型T来表示object，可能是数组，也可能是对象

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        if (code == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String error) {
        this.message = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "httpResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + data +
                '}';
    }
}
