package org.jssvc.lib.push;

import android.content.Context;
import android.content.Intent;

import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;

import org.android.agoo.common.AgooConstants;

/**
 * Created by lygdh on 2016/12/5.
 */

public class UmengNotificationService extends UmengMessageService {
    @Override
    public void onMessage(Context context, Intent intent) {
        UmLog.d("UmengNotificationService", "onMessage");
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Intent intent1 = new Intent();
        intent1.setClass(context, MyNotificationService.class);
        intent1.putExtra("UmengMsg", message);
        context.startService(intent1);
    }
}