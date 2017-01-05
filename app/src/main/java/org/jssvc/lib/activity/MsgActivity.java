package org.jssvc.lib.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.MsgAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MsgBean;
import org.jssvc.lib.view.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新闻通知
 */
public class MsgActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;

    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;

    List<MsgBean> msgList = new ArrayList<>();
    MsgAdapter msgAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initView() {

        rlEmpty.setVisibility(View.GONE);
        loadMsgList();
    }

    @OnClick({R.id.tvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }

    private void loadMsgList() {
        // 初始化假数据
        msgList.add(new MsgBean("1", "我校提前招生工作正式启动", "", "学生工作处", "2016/12/30"));
        msgList.add(new MsgBean("2", "大学生艺术展演之慰问后勤安保人员专场演出成功举行", "", "校团委、公共艺术教育中心", "2016/12/30"));
        msgList.add(new MsgBean("3", "学校举行苏州市大学生应急救护培训基地二期启动仪式", "http://www.jssvc.edu.cn/UploadFiles/20161230144130829.jpg", "校团委", "2016/12/30"));
        msgList.add(new MsgBean("4", "我校喜获两项教育部产学合作协同育人项目立项", "http://www.jssvc.edu.cn/UploadFiles/20161230142918478.jpg", "计算机工程学院", "2016/12/30"));
        msgList.add(new MsgBean("5", "苏州市光电缆行业大学访学活动首访亨通集团", "http://www.jssvc.edu.cn/UploadFiles/201612301022613.jpg", "电子信息工程学院", "2016/12/30"));
        msgList.add(new MsgBean("6", "我校与三星公司、园区产业工程师协会合作成立“三星科技大学”", "http://www.jssvc.edu.cn/UploadFiles/2016123092519552.jpg", "机电工程学院", "2016/12/30"));
        msgList.add(new MsgBean("7", "我校阳光体育第三届冬季长跑接力比赛圆满落幕", "http://www.jssvc.edu.cn/UploadFiles/201612309311118.jpg", "体育部", "2016/12/30"));
        msgList.add(new MsgBean("8", "“校商合作”育人才——苏州市职业大学电梯学院共建签约", "http://www.jssvc.edu.cn/UploadFiles/20161227163112923.jpg", "电工程学院", "2016/12/27"));
        msgList.add(new MsgBean("9", "我校2016年度学生“校园之星”新鲜出炉", "http://www.jssvc.edu.cn/UploadFiles/20161227161616863.jpg", "学生工作处 团委", "2016/12/27"));
        msgList.add(new MsgBean("10", "我校参加第二届全国职业大学办学经验交流会暨职业教育发展论坛", "http://www.jssvc.edu.cn/UploadFiles/20161226132646745.jpg", "校长办公室", "2016/12/27"));

        //创建默认的线性LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        msgAdapter = new MsgAdapter(context, msgList);
        recyclerView.setAdapter(msgAdapter);

        msgAdapter.setOnItemClickListener(new MsgAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, MsgBean item) {
                Intent intent = new Intent(context, MsgBrowseActivity.class);
                intent.putExtra("item", (Serializable) item);
                startActivity(intent);
            }
        });
    }
}