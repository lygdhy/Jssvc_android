package org.jssvc.lib.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import org.jssvc.lib.R;
import org.jssvc.lib.adapter.ShowTabAdapter;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.fragment.LibResumeFragment;
import org.jssvc.lib.fragment.LibScheduleFragment;
import org.jssvc.lib.fragment.LibYellowPagesFragment;

/**
 * 图书馆宣传片
 */
public class VideoActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback {
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_TITLE = "苏州市职业大学图书馆宣传片";
    private static final String VIDEO_URL = "http://www.hydong.me/app/libadvertisingvideo2.mp4";

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.rlTop)
    RelativeLayout rlTop;
    @BindView(R.id.llbody)
    LinearLayout llbody;
    @BindView(R.id.video_layout)
    FrameLayout mVideoLayout;
    @BindView(R.id.videoView)
    UniversalVideoView mVideoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mMediaController;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<String> list_title;
    private List<Fragment> list_fragment;
    private ShowTabAdapter showTabAdapter;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(new LibResumeFragment());
        list_fragment.add(new LibScheduleFragment());
        list_fragment.add(new LibYellowPagesFragment());

        //将名称加载tab名字列表
        list_title = new ArrayList<>();
        list_title.add("概况");
        list_title.add("开馆时间");
        list_title.add("黄页");

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));

        showTabAdapter = new ShowTabAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(showTabAdapter);

        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);

        // 开始播放
        startPlayVideo();
    }

    @OnClick({R.id.tvBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }

    // 开始播放
    private void startPlayVideo() {
        if (mSeekPosition > 0) {
            mVideoView.seekTo(mSeekPosition);
        }
        mVideoView.start();
        mMediaController.setTitle(VIDEO_TITLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();
            }
        });
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            rlTop.setVisibility(View.GONE);
            llbody.setVisibility(View.GONE);

        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            rlTop.setVisibility(View.VISIBLE);
            llbody.setVisibility(View.VISIBLE);
        }
        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }
}
