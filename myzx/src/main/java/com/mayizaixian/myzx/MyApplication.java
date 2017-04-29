package com.mayizaixian.myzx;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

//import com.umeng.socialize.PlatformConfig;

/**
 * Created by ntop on 15/7/8.
 */
public class MyApplication extends Application {

    private PushAgent mPushAgent;


    @Override
    public void onCreate() {
        super.onCreate();
        mPushAgent = PushAgent.getInstance(this);
        x.Ext.init(this);
        JPushInterface.init(this);
    }
}
