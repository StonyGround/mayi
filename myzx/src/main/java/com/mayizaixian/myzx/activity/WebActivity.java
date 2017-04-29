package com.mayizaixian.myzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class WebActivity extends BaseActivity implements View.OnClickListener {

    private String url;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ll_back.setOnClickListener(this);
    }

    @Override
    protected View getChildView() {
        View view = View.inflate(WebActivity.this, R.layout.activity_web, null);
        webView = (WebView) view.findViewById(R.id.webview);

        url = getIntent().getStringExtra("url");
        Log.e("WebActivity", "url----------" + url);

        webView.requestFocusFromTouch();

        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        WebSettings webSettings = webView.getSettings();
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + ";MAYI_ZX_ANDROID");
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Log.e("WebActivity", "currentUrl1111--------" + url);
                Intent intent = new Intent();
                if (url.equals(ConstantUtils.HOST)) {
                    intent.putExtra("tab", 0);
                    intent.setClass(WebActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (url.equals(ConstantUtils.HOST + ConstantUtils.USER)) {
                    intent.putExtra("tab", 2);
                    intent.setClass(WebActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (url.contains(ConstantUtils.HOST + "passport/index")) {
                    intent.setClass(WebActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if (url.equals("https://m.mayizaixian.cn/my_find")) {
                    intent.putExtra("tab", 1);
                    intent.setClass(WebActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (url.equals("https://m.mayizaixian.cn/passport/register")) {
                    intent.setClass(WebActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else if (url.equals("https://m.mayizaixian.cn/passport/find_pwd/")) {
                    intent.setClass(WebActivity.this, RetrieveActivity.class);
                    startActivity(intent);
                } else if (url.equals("https://m.mayizaixian.cn/setting")) {
                    intent.setClass(WebActivity.this, SettingActivity.class);
                    startActivity(intent);
                }

                view.loadUrl(url);

                //新浪支付页面跳转问题
                boolean isRecharge = false;
                if (url.contains("https://m.mayizaixian.cn/recharge/recharge_sure")) {
                    isRecharge = true;
                }
                return isRecharge;
            }
        });

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (s.length() > 8) {
                    s = s.substring(0, 8) + "...";
                }
                tv_title.setText(s);
            }
        };
        webView.setWebChromeClient(webChromeClient);

        String token = CacheUtils.getString(WebActivity.this, ConstantUtils.SP_TOKEN);
        String web_signature = CacheUtils.getString(WebActivity.this, ConstantUtils.SP_WEB_SIGNATURE);

        if (url.equals(ConstantUtils.AGREEMENT)) {   //加载蚂蚁在线协议
            webView.loadUrl(url);
        } else {
            if (CacheUtils.getString(WebActivity.this, ConstantUtils.SP_SECRET).equals("")) {
                Intent intent = new Intent();
                intent.setClass(WebActivity.this, LoginActivity.class);
                intent.putExtra("url", url);
                startActivityForResult(intent, 0);
            } else {
                url = url + "?access_token=" + token + "&signature=" + web_signature;
                webView.loadUrl(url);
            }
        }

        /**
         * js互调接口
         */
//        webView.addJavascriptInterface(new JSInterface(this, rl_titlebar), "Android");
        webView.addJavascriptInterface(new MyJSInterface(), "Android");

        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    rl_titlebar.setVisibility(View.GONE);
                    break;
                case 2:
                    rl_titlebar.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    class MyJSInterface {

        //绑定银行卡页面隐藏title
        @JavascriptInterface
        public void hideTitle() {
            handler.sendEmptyMessage(1);
        }

        @JavascriptInterface
        public void showTitle() {
            handler.sendEmptyMessage(2);
        }

        //ShareSDK
        @JavascriptInterface
        public void shareActivity(String title, String desc, String imageUrl, String loadUrl, final String momentsTitle) {
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(title);
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            //oks.setTitleUrl("http://m.mayizaixian.cn/");
            // text是分享文本，所有平台都需要这个字段
            oks.setText(desc);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//             oks.setImagePath(R.mipmap.ic_launcher);//确保SDcard下面存在此张图片
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(WebActivity.this.getString(R.string.app_name));
            oks.setTitleUrl(loadUrl);
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(loadUrl);
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(loadUrl);
            // 设置图片链接
            oks.setImageUrl(imageUrl);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // 启动分享GUI

            if (!momentsTitle.equals("")) {
                oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                    @Override
                    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                        if (platform.getName().equals("WechatMoments")) {
                            paramsToShare.setTitle(momentsTitle);
                        }
                    }
                });
            }
            oks.show(WebActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (CacheUtils.getString(WebActivity.this, ConstantUtils.SP_SECRET).equals("")) {
            Log.e("WebActivity", "SP_SECRET---" + CacheUtils.getString(WebActivity.this, ConstantUtils.SP_SECRET));
            finish();
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {
            url = data.getExtras().getString("url");
            if (url == null) {
                finish();
            }
            String token = CacheUtils.getString(WebActivity.this, ConstantUtils.SP_TOKEN);
            String web_signature = CacheUtils.getString(WebActivity.this, ConstantUtils.SP_WEB_SIGNATURE);
            url = url + "?access_token=" + token + "&signature=" + web_signature;
            webView.loadUrl(url);
        }
    }

    @Override
    public void onClick(View v) {
        if (webView.canGoBack()) {

            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_titlebar.getVisibility() == View.GONE) {
                handler.sendEmptyMessage(2);
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();//返回上一页面
                    return true;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
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
}
