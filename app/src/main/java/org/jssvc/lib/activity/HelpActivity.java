package org.jssvc.lib.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.DialogListSelecterAdapter;
import org.jssvc.lib.adapter.QleftAdapter;
import org.jssvc.lib.adapter.QrightAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.CategoryBean;
import org.jssvc.lib.bean.ListSelecterBean;
import org.jssvc.lib.bean.QuestionBean;
import org.jssvc.lib.data.AppPref;
import org.jssvc.lib.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 帮助指南
 */
public class HelpActivity extends BaseActivity
    implements StickyListHeadersListView.OnHeaderClickListener,
    StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
    StickyListHeadersListView.OnStickyHeaderChangedListener {

  @BindView(R.id.tvBack) TextView tvBack;
  @BindView(R.id.tvChat) TextView tvChat;
  @BindView(R.id.ivClose) ImageView ivClose;
  @BindView(R.id.tvTip) TextView tvTip;
  @BindView(R.id.rlTip) RelativeLayout rlTip;
  @BindView(R.id.lv_left) ListView lvLeft;
  @BindView(R.id.lv_right) StickyListHeadersListView lvRight;
  @BindView(R.id.empty) TextView empty;

  // 左侧内容adapter
  private QleftAdapter qleftAdapter;
  // 右侧类别adapter
  private QrightAdapter qRightAdapter;

  // 类别数据(左侧)
  private List<CategoryBean> leftList;
  // 菜品数据（右侧）
  private List<QuestionBean> rightList;

  @Override protected int getContentViewId() {
    return R.layout.activity_help;
  }

  @Override protected void initView() {
    if (AppPref.isFirstHelp(context)) {
      rlTip.setVisibility(View.VISIBLE);
    } else {
      rlTip.setVisibility(View.GONE);
    }

    // 获取焦点
    lvLeft.setFocusable(false);
    lvRight.setFocusable(false);
    tvTip.setFocusable(true);

    leftList = initLeftData();
    rightList = initRightData();

    qleftAdapter = new QleftAdapter(this, leftList);
    qRightAdapter = new QrightAdapter(this, rightList);

    lvLeft.setAdapter(qleftAdapter);
    lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 点击类别item，右侧菜品跳转到相应的位置
        CategoryBean categoryBean = leftList.get(position);
        for (int i = 0; i < rightList.size(); i++) {
          QuestionBean dishBean = rightList.get(i);
          if (categoryBean.id == dishBean.id) {
            lvRight.setSelection(i);

            lvLeft.setSelection(position);
            notifyLeftAdapter(position);
            break;
          }
        }
      }
    });

    lvRight.setOnHeaderClickListener(this);
    lvRight.setOnStickyHeaderChangedListener(this);
    lvRight.setOnStickyHeaderOffsetChangedListener(this);
    lvRight.setEmptyView(findViewById(R.id.empty));
    lvRight.setDrawingListUnderStickyHeader(true);
    lvRight.setAreHeadersSticky(true);
    lvRight.setAdapter(qRightAdapter);

    lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, HelpDetailsActivity.class);
        intent.putExtra("c", "" + rightList.get(position).getCategory());//类别
        intent.putExtra("q", "" + rightList.get(position).getTitle());
        intent.putExtra("a", "" + rightList.get(position).getContent());
        intent.putExtra("t", "" + rightList.get(position).getTip());
        startActivity(intent);
      }
    });
  }

  @OnClick({ R.id.tvBack, R.id.tvChat, R.id.ivClose }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvBack:
        finish();
        break;
      case R.id.tvChat:
        // 客服弹框
        List<ListSelecterBean> dataList = new ArrayList<>();
        dataList.add(
            new ListSelecterBean(R.drawable.icon_chat_qq, "1872237872", "图多多", "QQ 1872237872"));
        dataList.add(
            new ListSelecterBean(R.drawable.icon_chat_qq, "897457690", "图小小", "QQ 897457690"));
        dataList.add(
            new ListSelecterBean(R.drawable.icon_chat_qq, "893196521", "图妹妹", "QQ 893196521"));
        dataList.add(
            new ListSelecterBean(R.drawable.icon_chat_qq, "149553453", "图姐姐", "QQ 149553453"));
        qqCheckDialog("在线服务", "正常工作日9:00~16:30", dataList);
        break;
      case R.id.ivClose:
        // 关闭滚动提示
        AppPref.setHelpClose(context);
        rlTip.setVisibility(View.GONE);
        break;
    }
  }

  // 客服弹框
  private void qqCheckDialog(String dTitle, String dSubTitle, List<ListSelecterBean> dataList) {
    final AlertDialog dlg = new AlertDialog.Builder(context).create();
    dlg.show();
    dlg.getWindow()
        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = dlg.getWindow();
    window.setContentView(R.layout.dialog_list_select_layout);

    TextView tvDialogTitle = (TextView) window.findViewById(R.id.tvDialogTitle);
    TextView tvDialogSubTitle = (TextView) window.findViewById(R.id.tvDialogSubTitle);
    tvDialogTitle.setText(dTitle);
    tvDialogSubTitle.setText(dSubTitle);

    DialogListSelecterAdapter selecterAdapter;
    RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recyclerView);

    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    selecterAdapter = new DialogListSelecterAdapter(context, dataList);
    recyclerView.setAdapter(selecterAdapter);

    selecterAdapter.setOnItemClickListener(new DialogListSelecterAdapter.IMyViewHolderClicks() {
      @Override public void onItemClick(View view, ListSelecterBean item) {
        callQQCell(item.getId());
        dlg.dismiss();
      }
    });
  }

  @Override public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition,
      long headerId, boolean currentlySticky) {

  }

  // 滑动右侧listview出现类型改变时调用该方法
  @Override public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
      int itemPosition, long headerId) {
    QuestionBean dishBean = rightList.get(itemPosition);
    long id = dishBean.getId();
    int position = getCateId(id);
    if (position != -1) {
      lvLeft.setSelection(position);
      notifyLeftAdapter(position);
    }
  }

  @Override
  public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
  }

  // ===================数据获取=============================

  /**
   * 更新右侧adapter
   */
  private void notifyLeftAdapter(int position) {
    for (CategoryBean categoryBean : leftList) {
      categoryBean.isSelected = false;
    }

    leftList.get(position).isSelected = true;
    qleftAdapter.notifyDataSetChanged();
  }

  /**
   * 获取菜品id对应的类别所在position
   */
  private int getCateId(long id) {
    int position = 0;
    for (int i = 0; i < leftList.size(); i++) {
      position = i;
      if (id == leftList.get(i).id) {
        return position;
      }
    }

    return -1;
  }

  // ===================初始化数据=============================
  private List<QuestionBean> initRightData() {
    List<QuestionBean> tempList = new ArrayList<>();
    String[] categoryArr = getResources().getStringArray(R.array.question_category);
    // 第一组
    String[] questArr1 = getResources().getStringArray(R.array.question_list_1);
    String[] answerArr1 = getResources().getStringArray(R.array.answer_list_1);
    String[] tipArr1 = getResources().getStringArray(R.array.tip_list_1);
    for (int j = 0; j < questArr1.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(0);
      item.setCategory(categoryArr[0]);
      item.setTitle(questArr1[j]);
      item.setContent(answerArr1[j]);
      item.setTip(tipArr1[j]);
      tempList.add(item);
    }

    // 第二组
    String[] questArr2 = getResources().getStringArray(R.array.question_list_2);
    String[] answerArr2 = getResources().getStringArray(R.array.answer_list_2);
    String[] tipArr2 = getResources().getStringArray(R.array.tip_list_2);
    for (int j = 0; j < questArr2.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(1);
      item.setCategory(categoryArr[1]);
      item.setTitle(questArr2[j]);
      item.setContent(answerArr2[j]);
      item.setTip(tipArr2[j]);
      tempList.add(item);
    }

    // 第三组
    String[] questArr3 = getResources().getStringArray(R.array.question_list_3);
    String[] answerArr3 = getResources().getStringArray(R.array.answer_list_3);
    String[] tipArr3 = getResources().getStringArray(R.array.tip_list_3);
    for (int j = 0; j < questArr3.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(2);
      item.setCategory(categoryArr[2]);
      item.setTitle(questArr3[j]);
      item.setContent(answerArr3[j]);
      item.setTip(tipArr3[j]);
      tempList.add(item);
    }

    // 第四组
    String[] questArr4 = getResources().getStringArray(R.array.question_list_4);
    String[] answerArr4 = getResources().getStringArray(R.array.answer_list_4);
    String[] tipArr4 = getResources().getStringArray(R.array.tip_list_4);
    for (int j = 0; j < questArr4.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(3);
      item.setCategory(categoryArr[3]);
      item.setTitle(questArr4[j]);
      item.setContent(answerArr4[j]);
      item.setTip(tipArr4[j]);
      tempList.add(item);
    }

    // 第五组
    String[] questArr5 = getResources().getStringArray(R.array.question_list_5);
    String[] answerArr5 = getResources().getStringArray(R.array.answer_list_5);
    String[] tipArr5 = getResources().getStringArray(R.array.tip_list_5);
    for (int j = 0; j < questArr5.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(4);
      item.setCategory(categoryArr[4]);
      item.setTitle(questArr5[j]);
      item.setContent(answerArr5[j]);
      item.setTip(tipArr5[j]);
      tempList.add(item);
    }

    // 第六组
    String[] questArr6 = getResources().getStringArray(R.array.question_list_6);
    String[] answerArr6 = getResources().getStringArray(R.array.answer_list_6);
    String[] tipArr6 = getResources().getStringArray(R.array.tip_list_6);
    for (int j = 0; j < questArr6.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(5);
      item.setCategory(categoryArr[5]);
      item.setTitle(questArr6[j]);
      item.setContent(answerArr6[j]);
      item.setTip(tipArr6[j]);
      tempList.add(item);
    }

    // 第七组
    String[] questArr7 = getResources().getStringArray(R.array.question_list_7);
    String[] answerArr7 = getResources().getStringArray(R.array.answer_list_7);
    String[] tipArr7 = getResources().getStringArray(R.array.tip_list_7);
    for (int j = 0; j < questArr7.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(6);
      item.setCategory(categoryArr[6]);
      item.setTitle(questArr7[j]);
      item.setContent(answerArr7[j]);
      item.setTip(tipArr7[j]);
      tempList.add(item);
    }

    // 第八组
    String[] questArr8 = getResources().getStringArray(R.array.question_list_8);
    String[] answerArr8 = getResources().getStringArray(R.array.answer_list_8);
    String[] tipArr8 = getResources().getStringArray(R.array.tip_list_8);
    for (int j = 0; j < questArr8.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(7);
      item.setCategory(categoryArr[7]);
      item.setTitle(questArr8[j]);
      item.setContent(answerArr8[j]);
      item.setTip(tipArr8[j]);
      tempList.add(item);
    }

    // 第九组
    String[] questArr9 = getResources().getStringArray(R.array.question_list_9);
    String[] answerArr9 = getResources().getStringArray(R.array.answer_list_9);
    String[] tipArr9 = getResources().getStringArray(R.array.tip_list_9);
    for (int j = 0; j < questArr9.length; j++) {
      QuestionBean item = new QuestionBean();
      item.setId(8);
      item.setCategory(categoryArr[8]);
      item.setTitle(questArr9[j]);
      item.setContent(answerArr9[j]);
      item.setTip(tipArr9[j]);
      tempList.add(item);
    }

    return tempList;
  }

  private List<CategoryBean> initLeftData() {
    String[] categoryArr = getResources().getStringArray(R.array.question_category);
    List<CategoryBean> tempList = new ArrayList<>();
    for (int i = 0; i < categoryArr.length; i++) {
      CategoryBean item = new CategoryBean();
      if (i == 0) {
        item.isSelected = true;
      } else {
        item.isSelected = false;
      }
      item.id = i;
      item.name = categoryArr[i];
      tempList.add(item);
    }
    return tempList;
  }
}
