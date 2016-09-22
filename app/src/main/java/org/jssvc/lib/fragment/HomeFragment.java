package org.jssvc.lib.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.textView)
    TextView textView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        String url = "http://opac.jssvc.edu.cn:8080/reader/redr_verify.php";
        textView.setText(url);
        OkHttpUtils.post().url(url)
                .addParams("number", "157301241")
                .addParams("passwd", "157301241")
                .addParams("select", "cert_no")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        textView.setText(response);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.textView)
    public void onClick() {
        String url = "http://opac.jssvc.edu.cn:8080/reader/redr_info.php";
        textView.setText(url);
        OkHttpUtils.post().url(url)
//                .addParams("number", "157301241")
//                .addParams("passwd", "157301241")
//                .addParams("select", "cert_no")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        textView.setText(response);
                    }
                });
    }
}
