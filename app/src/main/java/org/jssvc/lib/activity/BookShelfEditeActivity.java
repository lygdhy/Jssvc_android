package org.jssvc.lib.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BookShelfEditeAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.BookShelfBean;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.utils.HtmlParseUtils;
import org.jssvc.lib.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

// 书架编辑列表
public class BookShelfEditeActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    List<BookShelfBean> shelfList = new ArrayList<>();
    BookShelfEditeAdapter bookShelfEditeAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_shelf_edite;
    }

    @Override
    protected void initView() {
        getBookShelf();
    }

    @OnClick({R.id.tvBack, R.id.ivAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                onBackPressed();
                break;
            case R.id.ivAdd:
                // 添加书架
                showInputAlert("0", "", "");
                break;
        }
    }

    // 获取书架目录
    private void getBookShelf() {
        showProgressDialog();
        OkGo.post(HttpUrlParams.URL_LIB_BOOK_SHELF)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        // s 即为所需要的结果
                        parseHtml2List(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        dealNetError(e);
                    }
                });
    }

    // 解析网页
    private void parseHtml2List(String s) {
        shelfList.clear();
        shelfList.addAll(HtmlParseUtils.getBookShelfList(s));

        if (shelfList.size() > 0) {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        //创建默认的线性LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        bookShelfEditeAdapter = new BookShelfEditeAdapter(context, shelfList);
        recyclerView.setAdapter(bookShelfEditeAdapter);

        bookShelfEditeAdapter.setOnItemClickListener(new BookShelfEditeAdapter.IMyViewHolderClicks() {
            @Override
            public void onItemClick(View view, BookShelfBean item) {
            }

            @Override
            public void onEditClick(View view, BookShelfBean item) {
                // 编辑书架
                showInputAlert("2", item.getId() + "", item.getName() + "");
            }

            @Override
            public void onDeleteClick(View view, BookShelfBean item) {
                // 删除书架
                deleteAlertDialog(item);
            }
        });
    }

    // 删除书架
    public void deleteAlertDialog(final BookShelfBean item) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("删除此类，将删除该类下所有数据。确认删除?");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();

                deleteBookShelf(item.getDeleteurl());
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    // 删除书架
    private void deleteBookShelf(String url) {
        showProgressDialog();
        OkGo.post(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        // s 即为所需要的结果
                        parseHtml2List(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        dealNetError(e);
                    }
                });
    }

    // 编辑框
    public void showInputAlert(final String code, final String shelfId, final String shelfName) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
     * @param code     新增0 删除1 修改2
     * @param classid  书架Id
     * @param cls_name 书架名称
     */
    // http://opac.jssvc.edu.cn:8080/reader/book_shelf_man.php?action=0&classid=&cls_name=路人甲&remark=这是一个测试书架
    // http://opac.jssvc.edu.cn:8080/reader/book_shelf_man.php?action=2&classid=0000000501&cls_name=路人甲&remark=这是一个测试书架
    private void editBookShelf(String code, String classid, String cls_name) {
        showProgressDialog();
        OkGo.get(HttpUrlParams.URL_LIB_BOOK_SHELF)
                .params("action", code)
                .params("classid", classid)
                .params("cls_name", cls_name)
                .params("remark", "")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dissmissProgressDialog();
                        // s 即为所需要的结果
                        parseHtml2List(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dissmissProgressDialog();
                        dealNetError(e);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {// 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent();
//        mIntent.putExtra("change02", "2000");
        this.setResult(0, mIntent);
    }
}
