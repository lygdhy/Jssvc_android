package org.jssvc.lib.ui.article;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.ui.article.bean.ArticleDetailBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.ui.general.WebActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文章详情
 */
public class ArticleDetailsActivity extends BaseActivity {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_original)
    TextView tvOriginal;
    @BindView(R.id.webView)
    WebView webView;

    ArticleDetailBean article;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_article_details;
    }

    @Override
    protected void initView() {
        tvTitle.setVisibility(View.INVISIBLE);
        tvAuthor.setVisibility(View.INVISIBLE);
        tvOriginal.setVisibility(View.INVISIBLE);

        String articleid = getIntent().getStringExtra("articleid");
        //showToast("--->   article id is " + articleid);
        getArticleById(articleid);
    }

    @OnClick({R.id.tv_back, R.id.tv_original})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_original:
                // 阅读原文
                if (article != null && !TextUtils.isEmpty(article.getOriginal())) {
                    //Intent intent = new Intent();
                    //intent.setAction("android.intent.action.VIEW");
                    //intent.setData(Uri.parse(article.getOriginal()));
                    //startActivity(intent);

//          Intent intent = new Intent(mContext, WebActivity.class);
//          intent.putExtra("url", article.getOriginal());
//          startActivity(intent);

                    WebActivity.start(mContext, "", article.getOriginal());
                }
                break;
        }
    }

    // 获取文章详情
    private void getArticleById(String articleid) {
        OkGo.<String>get(HttpUrlParams.GET_ARTICLE_DET).tag(this)
                .params("article_id", articleid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                article = new ArticleDetailBean();
                                article = new Gson().fromJson(jsonObject.optJSONObject("data").toString(),
                                        ArticleDetailBean.class);
                                loadArticle();
                            } else {
                                showToast(jsonObject.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dealNetError(response);
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dissmissProgressDialog();
                    }
                });
    }

    // 加载页面
    private void loadArticle() {
        tvTitle.setVisibility(View.VISIBLE);
        tvAuthor.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(article.getOriginal())) tvOriginal.setVisibility(View.VISIBLE);

        tvTitle.setText(article.getTitle());
        tvCategory.setText(article.getPlatform());

        String dateStr = "";
        try {
            Date date = df.parse(article.getAddtime() + "");
            SimpleDateFormat newf = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = newf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvAuthor.setText(dateStr + "  " + article.getAuthor());

        // 加载页面
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL("http://www.jssvc.edu.cn/", article.getContent(), "text/html", "utf-8", null);
    }
}
