package org.jssvc.lib.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import butterknife.BindView;

import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.BottomAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.bean.MenuBean;
import org.jssvc.lib.bean.VersionBean;
import org.jssvc.lib.data.Constants;
import org.jssvc.lib.data.DataSup;
import org.jssvc.lib.data.HttpUrlParams;
import org.jssvc.lib.fragment.HomeFragment;
import org.jssvc.lib.fragment.LabFragment;
import org.jssvc.lib.fragment.MineFragment;
import org.jssvc.lib.fragment.ReadingHubFragment;
import org.jssvc.lib.view.CustomDialog;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static org.jssvc.lib.base.BaseApplication.libOnline;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        initContent();

        checkVersion();

        // 请求打开摄像头和SD卡
        requestCodeQrcodePermissions();
    }

    private void setupViewPager(ViewPager viewPager) {
        BottomAdapter adapter = new BottomAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance());
        adapter.addFragment(ReadingHubFragment.newInstance());
        adapter.addFragment(LabFragment.newInstance());
        adapter.addFragment(MineFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

    private void initContent() {
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.item2:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.item3:
                        mViewPager.setCurrentItem(2);
                        return true;
                    case R.id.item4:
                        mViewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });
        mNavigation.setItemIconTintList(null);

        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void openActivityByMenu(MenuBean menu) {
        // Type 0链接类  !0 其他类别
        switch (menu.getType()) {
            case Constants.LIB_RESUME://证件信息
                judge2Activity(CardInfoActivity.class);
                break;
            case Constants.LIB_READ_ING://当前借阅
                judge2Activity(CurentBorrowActivity.class);
                break;
            case Constants.LIB_READ_HIS://借阅历史
                judge2Activity(HistoryBorrowActivity.class);
                break;
            case Constants.LIB_BOOK_REVIEW://我的书评
                showToast("即将上线");
                break;
            case Constants.LIB_BOOK_RECOMMEND://图书荐购
                showToast("即将上线");
                break;
            case Constants.LIB_BOOK_SHELF://我的书架
                judge2Activity(BookShelfActivity.class);
                break;
            case Constants.LIB_ABOUT://关于图书馆
                startActivity(new Intent(mContext, AboutSchoolActivity.class));
                break;
            case Constants.LIB_HELP://帮助指南
                startActivity(new Intent(mContext, HelpActivity.class));
                break;
            case Constants.LIB_SEARCH_BOOK://图书搜索
                startActivity(new Intent(mContext, BookSearchActivity.class));
                break;
            case Constants.MENU_NEWS://新闻资讯
                mViewPager.setCurrentItem(1);
                break;
            case Constants.MENU_ABOUT://关于我们
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case Constants.MENU_FEEDBACK://意见反馈
                startActivity(new Intent(mContext, FeedbackActivity.class));
                break;
            case Constants.MENU_WAITER://在线客服
                callQQCell(Constants.QQ_WAITER);
                break;
        }
    }

    // 含判断跳转
    private void judge2Activity(Class<?> activity) {
        if (DataSup.hasUserLogin()) {
            if (libOnline) {
                startActivity(new Intent(mContext, activity));
            } else {
                showToast("暂时无法使用");
            }
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    /**
     * 是否支持滑动返回
     */
    protected boolean supportSlideBack() {
        return false;
    }

    // 1、版本检查
    private void checkVersion() {
        OkGo.<String>get(HttpUrlParams.URL_GET_VERSION).tag(this)
                .params("platform", "android")
                .params("app_key", AppUtils.getAppPackageName())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                VersionBean item = new Gson().fromJson(jsonObject.optJSONObject("data").toString(),
                                        VersionBean.class);
                                analyzeVersion(item);// 版本判断
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
                });
    }

    // 2、版本判断
    private void analyzeVersion(VersionBean item) {
        int versioncode = AppUtils.getAppVersionCode();
        // 发现新版本
        if (versioncode < Integer.parseInt(item.getVersion_code())) {
            // force_version_code 低于该版本将强制升级
            boolean isForce = versioncode < Integer.parseInt(item.getForce_version_code());
            // 弹框提示
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
            builder.setTitle("发现新版本");
            builder.setMessage(item.getVersion_log() + "");
            builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 立即升级
                    doPgyUpdate();
                }
            });
            if (!isForce) {
                builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
            builder.create().show();
        }
    }

    // 3、蒲公英升级
    private void doPgyUpdate() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(String result) {
                AppBean appBean = getAppBeanFromString(result);
                startDownloadTask(MainActivity.this, appBean.getDownloadURL());
            }

            @Override
            public void onNoUpdateAvailable() {
                showToast("已经是最新版");
            }
        });
    }

    // =============================权限 =============================
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQrcodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "为保障APP正常工作，请授权APP使用拍照和SD卡读写功能！",
                    REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
