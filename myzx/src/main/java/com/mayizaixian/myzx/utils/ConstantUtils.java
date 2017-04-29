package com.mayizaixian.myzx.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.webkit.WebView;

public class ConstantUtils {

    public static final String SP_USER_NAME = "userName";
    public static final String SP_USER_PWD = "pwd";
    public static final String SP_COOKIE = "cookie";
    public static final String SP_UID = "uid";
    public static final String SP_SECRET = "secret";
    public static final String SP_KEEP_CODE = "keep_code";
    public static final String SP_WEB_SIGNATURE = "web_signature";
    public static final String SP_SIGNATURE = "signature";
    public static final String SP_ARTICLE = "article";
    public static final String SP_SHOW_UPDATE = "show_update";
    public static final String SP_SYS_TIME = "sys_time";
    public static final String SP_VERSION = "version";
    public static final String SP_VERSION_DESC = "version_desc";
    public static final String SP_VERSION_URL = "version_url";

    public static final String WECHAT_APPID = "wxf1bbb15ab8fc0424";

    /**
     * apk更新链接
     */
    public static final String UPDATE_URL = "http://m.mayizaixian.cn/data/app/updateinfo.json";

    /**
     * apk更新链接(https)
     */
    public static final String UPDATE_URL_HTTPS = "https://m.mayizaixian.cn/data/app/updateinfo.json";

//        public static final String HOST = "http://t.mayionline.cn/";
    public static final String HOST = "https://m.mayizaixian.cn/";
//    public static final String HOST = "http://staging.mayionline.cn/";
//    public static final String HOST = "http://dev.mayionline.cn/";


    public static final String GET_TOKEN = "app_api/api/get_token";
    public static final String HOME_LIST = "app_api/index/borrow_list";
    public static final String HOME_BANNER = "app_api/index/banner";
    public static final String LOGIN = "app_api/passport/login_app";
    public static final String FIND_LIST = "app_api/my_find/find_list";
    public static final String FIND_BANNER = "app_api/my_find/banner";
    public static final String USER_INFO = "app_api/users/user_info";
    public static final String ARTICLE = "app_api/users/article";
    public static final String QINIU_TOKEN = "app_api/api/qiniu_token";
    public static final String SEND_CODE = "app_api/passport/app_code";
    public static final String REGISTER = "app_api/passport/reg_app";
    public static final String RETRIEVE = "app_api/passport/find_login_password";
    public static final String SETTING = "app_api/users/setting_app";
    public static final String UPLOAD_KEY = "app_api/users/avatar_app";
    public static final String FIND_PWD_CODE = "app_api/passport/find_pws_code";
    public static final String INDEX_DEV = "app_api/index/index_dev";

    public static final String USER = "users";
    public static final String USER_MESSAGE = "messagelist/index";
    public static final String USER_WALLET = "users/wallet";
    public static final String USER_COUPON = "users/coupon";
    public static final String USER_INVITE = "invite";
    public static final String RECHARGE = "recharge";
    public static final String SETTING_CODE = "users/ewm_share";
    public static final String SETTING_QRCODE = "users/ewm_share";
    public static final String SETTING_IDCARD = "users/bankset";
    public static final String SETTING_BANK = "users/bankset";
    public static final String SETTING_LOGINPWD = "passport/up_pwd";
    public static final String SETTING_WITHDRAWPWD = "setting/up_cash_pwd";
    public static final String SETTING_FINDPWD = "setting/find_cash_pwd";
    public static final String SETTING_HELP = "about/problem";
    public static final String AGREEMENT = "file:///android_asset/reg.html";
    public static final String HOME_SAFE_1 = "activity/advert/index/1";
    public static final String HOME_SAFE_2 = "activity/advert/index/2";
    public static final String HOME_SAFE_3 = "activity/advert/index/3";
    public static final String HOME_SAFE_4 = "activity/advert/index/4";

    public static final String SP_TOKEN = "token";
    public final static int SUCESS = 0;
    public final static int FAILURE = -1;
    public static String DATA_ON_NET = "data_on_net";

    //banner
    public static final int BANNER_START_ROLLING = 0;
    public static final int BANNER_KEEP_ROLLING = 1;
    public static final int BANNER_STOP_ROLLING = 2;
    public static final int BANNER_CHANGE_ROLLING = 3;
    public static final int BANNER_PAUSE_ROLLING = 4;
    public static final int BANNER_CONTINUE_ROLLING = 5;
    public static final int GET_BANNER_DATA = 6;
    public static final int INIT_RECYCLER_VIEW = 7;
    public static final int REQUEST_TAKE_PHOTO = 9;
    public static final int REQUEST_PHOTO_ZOOM = 10;
    public static final int REQUEST_PHOTO_RESULT = 11;
    public static final int GET_INDEX_DEV = 12;

    /**
     * 当前屏幕宽高
     */
    public static int HEIGHT_SCREEN;
    public static int WIDTH_SCREEN;

    public static void initiScreenParam(Activity activity) {
        DisplayMetrics display = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display);
        ConstantUtils.WIDTH_SCREEN = display.widthPixels;
        ConstantUtils.HEIGHT_SCREEN = display.heightPixels;
    }
}
