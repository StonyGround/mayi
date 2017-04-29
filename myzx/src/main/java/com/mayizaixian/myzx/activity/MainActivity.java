package com.mayizaixian.myzx.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.bean.UpdateInfo;
import com.mayizaixian.myzx.fragment.FragmentFind;
import com.mayizaixian.myzx.fragment.FragmentHaitao;
import com.mayizaixian.myzx.fragment.FragmentHome;
import com.mayizaixian.myzx.fragment.FragmentUser;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.DensityUtil;
import com.mayizaixian.myzx.utils.JSInterface;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends FragmentActivity {

    /**
     * 检查升级成功
     */
    protected static final int WHAT_REQUEST_UPDATE_SUCCESS = 3;

    /**
     * 下载apk成功
     */
    private static final int WHAT_DOWNLOAD_SUCCESS = 5;

    /**
     * 下载apk失败
     */
    private static final int WHAT_DOWNLOAD_ERROR = 6;
    /**
     * 版本升级信息
     */
    private UpdateInfo updateInfo;

    private File apkFile;

    /**
     * 升级提示Dialog
     */
    private ProgressDialog pd;

    private long mExitTime;

    public static final String TAG = GuideActivity.class.getSimpleName();

    private LayoutInflater inflater;
    private static List<View> tabList;
    public static FragmentTabHost tabHost;

    private Class[] fragmentArray = new Class[]{FragmentHome.class, FragmentHaitao.class, FragmentFind.class, FragmentUser.class};

    private static final String[] tabTitles = new String[]{"理财", "购物", "发现", "我的"};

    private int[] imgRes = new int[]{R.drawable.tab_home_btn, R.drawable.tab_haitao_btn, R.drawable.tab_find_btn, R.drawable.tab_user_btn};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ConstantUtils.HEIGHT_SCREEN <= 0) {
            ConstantUtils.initiScreenParam(this);
        }

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        mPushAgent.onAppStart();
//        String device_token = UmengRegistrar.getRegistrationId(this);
//        Log.e("device_token",device_token+"-------------");

        //初始化sharesdk
        ShareSDK.initSDK(this);

        checkUpdate();

        inflater = LayoutInflater.from(this);
        tabList = getTabViewList(tabTitles.length);
        initiTabHost();
//        initFragment();
    }

    private List<View> getTabViewList(int len) {
        List<View> list = new ArrayList<>();
        if (len <= 0) return list;
        for (int i = 0; i < len; i++) {
            list.add(getTabItemView(i));
        }
        return list;
    }

    private View getTabItemView(int tabIndex) {
        View view = inflater.inflate(R.layout.item_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(imgRes[tabIndex]);
        return view;
    }

    private void initiTabHost() {
        int tab = getIntent().getIntExtra("tab", 0);
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        tabHost.setCurrentTab(tab);
        tabHost.setup(this, getSupportFragmentManager(), R.id.content);
        tabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < fragmentArray.length; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabTitles[i]).setIndicator(tabList.get(i));
            //将Tab按钮添加进Tab选项卡中
            tabHost.addTab(tabSpec, fragmentArray[i], null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("我的")) {
                    if (CacheUtils.getString(MainActivity.this, ConstantUtils.SP_SECRET).equals("")) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }
            }
        });
    }

    /**
     * 检查升级
     */
    private void checkUpdate() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    //获取json字符串
                    String result = CommonUtils.getDataFromNet(ConstantUtils.UPDATE_URL_HTTPS);

                    if (!result.equals("")) {
                        updateInfo = new Gson().fromJson(result, UpdateInfo.class);
                        CacheUtils.setString(MainActivity.this, ConstantUtils.SP_VERSION, updateInfo.getVersion());
                        CacheUtils.setString(MainActivity.this, ConstantUtils.SP_VERSION_DESC, updateInfo.getDesc());
                        CacheUtils.setString(MainActivity.this, ConstantUtils.SP_VERSION_URL, updateInfo.getApkUrl());
                        handler.sendEmptyMessage(WHAT_REQUEST_UPDATE_SUCCESS);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_REQUEST_UPDATE_SUCCESS:
                    if (!CommonUtils.getVersion(MainActivity.this).equals(updateInfo.getVersion())
                            && System.currentTimeMillis() - CacheUtils.getLong(MainActivity.this, ConstantUtils.SP_SYS_TIME) > 86400000) {
                        showHintDownloadDialog();
                    }
                    break;
                case WHAT_DOWNLOAD_SUCCESS:
                    pd.dismiss();
                    installApk();
                    break;
                case WHAT_DOWNLOAD_ERROR:
                    pd.dismiss();
                    CommonUtils.showMessage(getApplicationContext(), "下载apk失败");
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 安装apk文件
     */
    private void installApk() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 更新Dialog
     */
    private void showHintDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(updateInfo.getDesc())
                .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startDownloadApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheUtils.setLong(MainActivity.this, ConstantUtils.SP_SYS_TIME, System.currentTimeMillis());
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 下载apk
     */
    private void startDownloadApk() {
        apkFile = new File(getExternalFilesDir(null), "myzx.apk");
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgressNumberFormat("%1d kb/%2d kb");
        pd.show();

        new Thread() {
            public void run() {
                try {
                    CommonUtils.downloadApk(updateInfo.getApkUrl(), apkFile, pd);
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_ERROR);
                }
            }
        }.start();


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //    /**
//     * 初始化Fragment
//     */
//    private void initFragment() {
//        //通过FragmentManager把两个Fragment添加到Activity中
//        //不能用getFragmentManager();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.replace(R.id.fl_main, new ContentFragment());
//        ft.commit();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }
}
