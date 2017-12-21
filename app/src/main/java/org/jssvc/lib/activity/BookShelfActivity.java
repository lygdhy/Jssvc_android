package org.jssvc.lib.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import java.util.ArrayList;
import java.util.List;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookShelfBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.fragment.BookShelfFragment;
import org.jssvc.lib.utils.HtmlParseUtils;

/**
 * 我的书架
 */
public class BookShelfActivity extends BaseActivity {

  @BindView(R.id.iv_edite) ImageView ivEdite;
  @BindView(R.id.empty_layout) LinearLayout emptyLayout;
  @BindView(R.id.date_layout) LinearLayout dateLayout;

  @BindView(R.id.tab_layout) TabLayout tabLayout;
  @BindView(R.id.view_pager) ViewPager viewPager;

  private List<String> list_title;
  private List<Fragment> list_fragment;

  List<BookShelfBean> shelfList = new ArrayList<>();
  private ShowTabAdapter showTabAdapter;

  @Override protected int getContentViewId() {
    return R.layout.activity_book_shelf;
  }

  @Override protected void initView() {
    ivEdite.setVisibility(View.GONE);
    emptyLayout.setVisibility(View.GONE);
    dateLayout.setVisibility(View.GONE);
    getBookShelf();
  }

  @OnClick({ R.id.tv_back, R.id.iv_edite }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
        finish();
        break;
      case R.id.iv_edite:
        if (shelfList.size() == 0) {
          // 添加书架
          showInputAlert("0", "", "");
        } else {
          // 书架管理
          startActivityForResult(new Intent(mContext, BookShelfEditeActivity.class), 10086);
        }
        break;
    }
  }

  // 获取书架目录
  private void getBookShelf() {
    OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_SHELF).tag(this).execute(new StringCallback() {
      @Override public void onSuccess(Response<String> response) {
        parseHtml2List(response.body());
      }

      @Override public void onError(Response<String> response) {
        super.onError(response);
        dealNetError(response);
      }

      @Override public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        showProgressDialog();
      }

      @Override public void onFinish() {
        super.onFinish();
        dissmissProgressDialog();
      }
    });
  }

  // 解析网页
  private void parseHtml2List(String s) {
    shelfList.clear();
    shelfList.addAll(HtmlParseUtils.getBookShelfList(s));

    if (shelfList.size() > 0) {
      ivEdite.setVisibility(View.VISIBLE);
      ivEdite.setImageResource(R.drawable.icon_collect_edite);
      emptyLayout.setVisibility(View.GONE);
      dateLayout.setVisibility(View.VISIBLE);

      list_title = new ArrayList<>();
      list_fragment = new ArrayList<>();
      //设置TabLayout的模式
      tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

      for (int i = 0; i < shelfList.size(); i++) {
        //将名称加载tab名字列表
        list_title.add(shelfList.get(i).getName() + "");

        //初始化各fragment
        BookShelfFragment shelfFragment = new BookShelfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", shelfList.get(i).getUrl());
        shelfFragment.setArguments(bundle);

        //将fragment装进列表中
        list_fragment.add(shelfFragment);

        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(i)));
      }

      showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
      viewPager.setAdapter(showTabAdapter);

      //TabLayout加载viewpager
      tabLayout.setupWithViewPager(viewPager);
    } else {
      // 没有书架
      ivEdite.setVisibility(View.VISIBLE);
      ivEdite.setImageResource(R.drawable.icon_collect_off);
      emptyLayout.setVisibility(View.VISIBLE);
      dateLayout.setVisibility(View.GONE);
    }
  }

  // 编辑框
  public void showInputAlert(final String code, final String shelfId, final String shelfName) {
    final AlertDialog dlg = new AlertDialog.Builder(mContext).create();
    dlg.show();
    dlg.getWindow()
        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = dlg.getWindow();
    window.setContentView(R.layout.dialog_input_layout);

    TextView title = (TextView) window.findViewById(R.id.title);
    title.setText("编辑");

    final EditText inputInfo = (EditText) window.findViewById(R.id.inputInfo);
    inputInfo.setHint("请输入书架名称");
    inputInfo.setText(shelfName);

    Button positiveButton = (Button) window.findViewById(R.id.positiveButton);
    positiveButton.setText("提交");
    positiveButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String newStr = inputInfo.getText().toString();
        if (TextUtils.isEmpty(newStr)) {
          showToast("书架名称不能为空");
        } else {
          dlg.dismiss();
          editBookShelf(code, shelfId, newStr);
        }
      }
    });

    Button negativeButton = (Button) window.findViewById(R.id.negativeButton);
    negativeButton.setText("取消");
    negativeButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        dlg.dismiss();
      }
    });
  }

  /**
   * 编辑书架
   *
   * @param code 新增0 删除1 修改2
   * @param classid 书架Id
   * @param cls_name 书架名称
   */
  // http://opac.jssvc.edu.cn:8080/reader/book_shelf_man.php?action=0&classid=&cls_name=路人甲&remark=这是一个测试书架
  // http://opac.jssvc.edu.cn:8080/reader/book_shelf_man.php?action=2&classid=0000000501&cls_name=路人甲&remark=这是一个测试书架
  private void editBookShelf(String code, String classid, String cls_name) {
    OkGo.<String>post(HttpUrlParams.URL_LIB_BOOK_SHELF).tag(this)
        .params("action", code)
        .params("classid", classid)
        .params("cls_name", cls_name)
        .params("remark", "")
        .execute(new StringCallback() {
          @Override public void onSuccess(Response<String> response) {
            parseHtml2List(response.body());
          }

          @Override public void onError(Response<String> response) {
            super.onError(response);
            dealNetError(response);
          }

          @Override public void onStart(Request<String, ? extends Request> request) {
            super.onStart(request);
            showProgressDialog();
          }

          @Override public void onFinish() {
            super.onFinish();
            dissmissProgressDialog();
          }
        });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10086) {
      getBookShelf();
    }
  }
}
