package org.jssvc.lib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Activity 基类
 */
public class BaseActivity extends Activity {
    public Context context;
    private Toast toast = null;//全局Toast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

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

//    /**
//     * 网络GET请求
//     *
//     * @param url 目标地址
//     * @param map 参数列表
//     * @return
//     */
//    public String httpGetRequest(String url, Map parms) {
//        String result = "";
//        GetBuilder builder = OkHttpUtils.get();
//        builder.url(url);
//        // 添加参数
//        Iterator iter = parms.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            builder.addParams(entry.getKey().toString(), entry.getValue().toString());
//        }
//        // 执行操作
//        builder.build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                e.printStackTrace();
//                Log.d("onError", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                result = response;
//            }
//        });
//        return result;
//    }
}
