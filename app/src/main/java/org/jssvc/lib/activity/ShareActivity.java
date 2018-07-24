package org.jssvc.lib.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.OnClick;

import org.jssvc.lib.R;
import org.jssvc.lib.base.BaseActivity;
import org.jssvc.lib.data.Constants;

/**
 * 分享APP
 */
public class ShareActivity extends BaseActivity {

    private IWXAPI wApi;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initView() {
        wApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        wApi.registerApp(Constants.WECHAT_APP_ID);
    }

    @OnClick({R.id.opt_back, R.id.opt_share, R.id.iv_share_moments})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.opt_back:
                finish();
                break;
            case R.id.opt_share:
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("text/plain");//设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));//添加分享内容标题
                share_intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name)
                        + "下载地址 http://www.jssvc.org/download/libapp.html");//添加分享内容
                share_intent = Intent.createChooser(share_intent, "分享");
                startActivity(share_intent);
                break;
            case R.id.iv_share_moments:
                // 分享至朋友圈
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://www.jssvc.org/";

                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = getString(R.string.app_name);
                msg.description = getString(R.string.app_slogan);
                msg.setThumbImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "app_share";
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;

                wApi.sendReq(req);
                break;
        }
    }
}
