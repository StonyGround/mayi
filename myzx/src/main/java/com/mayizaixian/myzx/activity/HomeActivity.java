package com.mayizaixian.myzx.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.http.HttpPostMap;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class HomeActivity extends Activity {

    /**
     * 标识是否进入过主页面
     */
    public static final String IS_START_MAIN = "is_start_main";

    private static final int TIME = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            boolean isStartMain = CacheUtils.getBoolean(HomeActivity.this,
                    IS_START_MAIN);
            Intent intent;
            if (isStartMain) {
                // 之前进入过主页面-直接进入主页面
                intent = new Intent(HomeActivity.this, MainActivity.class);
            } else {
                //软件刚安装或者升级的时候
                intent = new Intent(HomeActivity.this, GuideActivity.class);
                createShortCut();
            }
            startActivity(intent);
            // 关闭欢迎页面
            finish();
        }
    };

    /**
     * 生成快捷方式
     */
    private void createShortCut() {

        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));

        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R
                .mipmap.ic_launcher);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        Intent intent = new Intent(this, HomeActivity.class);

        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        sendBroadcast(shortcutIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

//        //友盟推送(弃用)
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.enable();
//        mPushAgent.onAppStart();
//        String device_token = UmengRegistrar.getRegistrationId(this);
//        Log.e("device_token", device_token + "-------------");
//        Log.e("getDeviceInfo", getDeviceInfo(this) + "-------------");

        //ShareSDK
        ShareSDK.initSDK(this);

        handler.sendEmptyMessageDelayed(TIME, 3000);

        if (CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_TOKEN).isEmpty()) {
//            Log.e("HomeActivity", "token------" + CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_TOKEN).isEmpty());
//            Map<String, String> map = new HashMap<>();
//            HttpPostMap postMap = new HttpPostMap(this, ConstantUtils.GET_TOKEN, map);
//            postMap.postBackInMain(new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    String json = msg.getData().getString(ConstantUtils.DATA_ON_NET);
//                    try {
//                        JSONObject jsonObject = new JSONObject(json);
//                        CacheUtils.setString(HomeActivity.this, ConstantUtils.SP_TOKEN, jsonObject.getString("token"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

            RequestParams requestParams=new RequestParams(ConstantUtils.HOST+ConstantUtils.SP_TOKEN);
            requestParams.addHeader("User-Agent", "MAYI_ZX_ANDROID");
            x.http().request(HttpMethod.GET, requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        CacheUtils.setString(HomeActivity.this, ConstantUtils.SP_TOKEN, jsonObject.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
//        Log.e("HomeActivity", "SP_TOKEN----" + CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_TOKEN));
//        Log.e("HomeActivity", "SP_SECRET----" + CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_SECRET));
//        Log.e("HomeActivity", "SP_WEB_SIGNATURE----" + CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_WEB_SIGNATURE));
//        Log.e("HomeActivity", "SP_SIGNATURE----" + CacheUtils.getString(HomeActivity.this, ConstantUtils.SP_SIGNATURE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 友盟SDK获取设备标识
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
