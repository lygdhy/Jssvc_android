package org.jssvc.lib.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.activity.WebActivity;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.AdsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<AdsBean> adsList = new ArrayList<>();
        adsList.add(new AdsBean("1", "1", "欢度中秋", "http://www.hydong.me/appsrc/bg_addemo.png", "http://baike.baidu.com/view/2568.htm"));
        adsList.add(new AdsBean("2", "1", "图书馆", "http://www.hydong.me/appsrc/bg_addemo2.png", "http://baike.baidu.com/view/5476774.htm"));

        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, adsList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
        // convenientBanner.setManualPageable(false);//设置不能手动影响
    }

    @Override
    protected void onFirstUserVisible() {
        
    }

    public class LocalImageHolderView implements Holder<AdsBean> {
        private SimpleDraweeView simpleDraweeView;

        @Override
        public View createView(Context context) {
            simpleDraweeView = new SimpleDraweeView(context);
            simpleDraweeView.setScaleType(SimpleDraweeView.ScaleType.CENTER_CROP);
            return simpleDraweeView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final AdsBean adsBean) {
            simpleDraweeView.setImageURI(adsBean.getPic());
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", adsBean.getUrl());
                    intent.putExtra("title", adsBean.getTitle());
                    startActivity(intent);
                }
            });
        }
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }
}
