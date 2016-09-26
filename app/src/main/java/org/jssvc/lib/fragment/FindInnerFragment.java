package org.jssvc.lib.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ArticleListAdapter;
import org.jssvc.lib.base.BaseFragment;
import org.jssvc.lib.bean.ArticleBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;

/**
 * Created by lygdh on 2016/9/26.
 */

public class FindInnerFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.mListView)
    ListView mDataLv;

    private ArticleListAdapter mAdapter;

    public static final String ARGS_CHANNEL = "args_channel_id";
    private int channelId;

    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    List<ArticleBean> articleBeanList = new ArrayList<>();

    String url = "http://v.juhe.cn/toutiao/index?type=top&key=7eee44e7b89b84e5cc6f79ae24358dd1";

    public static FindInnerFragment newInstance(int id) {
        FindInnerFragment fragment = new FindInnerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CHANNEL, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelId = getArguments().getInt(ARGS_CHANNEL);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_find_inner;
    }

    @Override
    protected void initView() {
        mRefreshLayout.setDelegate(this);

        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);

        mAdapter = new ArticleListAdapter(getActivity());
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);


        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getActivity(), true);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.colorAccent);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.ic_launcher);
//        moocStyleRefreshViewHolder.setLoadMoreBackgroundColorRes(R.color.custom_imoocstyle);
        moocStyleRefreshViewHolder.setSpringDistanceScale(0.2f);
//        moocStyleRefreshViewHolder.setRefreshViewBackgroundColorRes(R.color.custom_imoocstyle);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
//        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(getActivity()), true);

        mDataLv.setAdapter(mAdapter);
    }

    @Override
    protected void onFirstUserVisible() {
        mNewPageNumber = 0;
        mMorePageNumber = 0;

        for (int i = 0; i < 8; i++) {
            articleBeanList.add(new ArticleBean("" + i, "标题" + 1, "这里是内容" + 1));
        }

        OkHttpUtils.post().url(url)
                .addParams("type", "157301241")
                .addParams("passwd", "157301241")
                .addParams("select", "cert_no")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

        mRefreshLayout.endRefreshing();
        mAdapter.setData(articleBeanList);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber++;
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了");
            return;
        }

        for (int i = articleBeanList.size(); i < 8; i++) {
            articleBeanList.add(new ArticleBean("" + i, "标题" + 1, "这里是内容" + 1));
        }
        mRefreshLayout.endRefreshing();
        mAdapter.addNewData(articleBeanList);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mMorePageNumber++;
        if (mMorePageNumber > 4) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }

        for (int i = articleBeanList.size(); i < 8; i++) {
            articleBeanList.add(new ArticleBean("" + i, "标题" + 1, "这里是内容" + 1));
        }
        mRefreshLayout.endLoadingMore();
        mAdapter.addMoreData(articleBeanList);

//        showLoadingDialog();
//        mEngine.loadMoreData(mMorePageNumber).enqueue(new Callback<List<ArticleBean>>() {
//            @Override
//            public void onResponse(Call<List<ArticleBean>> call, final Response<List<ArticleBean>> response) {
//                // 测试数据放在七牛云上的比较快，这里加载完数据后模拟延时查看动画效果
//                ThreadUtil.runInUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshLayout.endLoadingMore();
//                        dismissLoadingDialog();
//                        mAdapter.addMoreData(response.body());
//                    }
//                }, MainActivity.LOADING_DURATION);
//            }
//
//            @Override
//            public void onFailure(Call<List<ArticleBean>> call, Throwable t) {
//                mRefreshLayout.endLoadingMore();
//            }
//        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showToast("点击了条目 " + mAdapter.getItem(position).getId());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showToast("长按了" + mAdapter.getItem(position).getId());
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_normal_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).getId());
            return true;
        }
        return false;
    }
}
