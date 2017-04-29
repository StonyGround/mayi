package com.mayizaixian.myzx.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.MainActivity;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by Administrator on 2016/4/18.
 */
public class JSInterface {

    private Context context;
    private RelativeLayout rl_titlebar;

    public JSInterface(Context context, RelativeLayout rl_titlebar) {
        this.context = context;
        this.rl_titlebar = rl_titlebar;
    }

    @JavascriptInterface
    public void hideTitle(){
        Toast.makeText(context,"hideTitle",Toast.LENGTH_SHORT).show();
        rl_titlebar.setVisibility(View.GONE);
    }

    @JavascriptInterface
    public void showTitle(){
        Toast.makeText(context,"showTitle",Toast.LENGTH_SHORT).show();
        rl_titlebar.setVisibility(View.VISIBLE);
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
        oks.setSite(context.getString(R.string.app_name));

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
        oks.show(context);
    }

//    //ShareSDK
//    @JavascriptInterface
//    public void share(String vCode) {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("注册送3000元特权本金");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        //oks.setTitleUrl("http://m.mayizaixian.cn/");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("蚂蚁在线是专注于大型电商应收账款的在线供应链金融平台，我们为您提供京东、天猫国际、苏宁、国美等知名企业优质供应链金融产品.");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        // oks.setImagePath(R.mipmap.ic_launcher);//确保SDcard下面存在此张图片
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//
//        oks.setTitleUrl("https://m.mayizaixian.cn/activity/online/index/" + vCode);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("https://m.mayizaixian.cn/activity/online/index/" + vCode);
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("https://m.mayizaixian.cn/activity/online/index/" + vCode);
//        // 设置图片链接
//        oks.setImageUrl("https://m.mayizaixian.cn/resources/images/wx_share.png");
//
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // 启动分享GUI
//        oks.show(context);
//
////        new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
////                .withText("蚂蚁在线是专注于大型电商应收账款的在线供应链金融平台，我们为您提供京东、天猫国际、苏宁、国美等知名企业优质供应链金融产品.")
////                .withTitle("注册送3000元特权本金")
////                .withTargetUrl("https://m.mayizaixian.cn/activity/online/index/" + vCode)
////                .withMedia(new UMImage(context, "https://m.mayizaixian.cn/resources/images/wx_share.png"))
////                .setListenerList(umShareListener)
////                .open();
//    }

//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            Log.d("plat", "platform" + platform);
//            Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }
//    };


}
