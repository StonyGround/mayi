package com.mayizaixian.myzx.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.LoginActivity;
import com.mayizaixian.myzx.activity.MainActivity;
import com.mayizaixian.myzx.activity.SettingActivity;
import com.mayizaixian.myzx.activity.WebActivity;
import com.mayizaixian.myzx.adapter.VerticalPagerAdapter;
import com.mayizaixian.myzx.bean.ArticleBean;
import com.mayizaixian.myzx.bean.UserInfoBean;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.widget.VerticalViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class FragmentUser extends BaseFragment implements View.OnClickListener {

    private static final int GET_USER_DATA = 0;
    private static final int GET_ARTICLE = 1;
    private static final int ROLLING_ARTICLE = 2;

    private LinearLayout ll_user_setting;
    private LinearLayout ll_personal_msg;
    private RelativeLayout rl_user_income_mayi;
    private RelativeLayout rl_user_haitao_income;
    private RelativeLayout rl_user_wallet;
    private RelativeLayout rl_user_haitao;
    private RelativeLayout rl_user_invite;
    private LinearLayout ll_user_recharge;
    private LinearLayout ll_user_invest;
    private RelativeLayout ll_user_banner;

    private ImageView iv_user_photo;
    private TextView tv_income_left;
    private TextView tv_income_right;
    private TextView tv_haitao_left;
    private TextView tv_haitao_right;
    private TextView tv_wallet_income;
    private TextView tv_haitao_num;
    private TextView tv_user_invite;
    private LinearLayout ll_article_gone;
    private ImageView iv_wallet_icon;
    private ImageView iv_haitao_icon;
    private ImageView iv_invite_icon;
    private TextView tv_user_capital;

    private TextView tv_title;
    private LinearLayout ll_back;

    private UserInfoBean userInfoBean;
    private ArticleBean articleBean;
    private List<ArticleBean.DataBean> articleList;

    private VerticalViewPager vertical_viewpager;

    int currentItem = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case GET_USER_DATA:
                    bindUserData();
                    break;
                case GET_ARTICLE:
                    bindArticle();
                    break;
                case ROLLING_ARTICLE:
                    if (hasMessages(ROLLING_ARTICLE)) {
                        removeMessages(ROLLING_ARTICLE);
                    }
                    currentItem++;
                    vertical_viewpager.setCurrentItem(currentItem);
                    sendEmptyMessageDelayed(ROLLING_ARTICLE, 3000);
                    break;
            }
        }
    };

    private void bindArticle() {
        vertical_viewpager.setAdapter(new VerticalPagerAdapter(getActivity(), articleList));
        handler.sendEmptyMessageDelayed(ROLLING_ARTICLE, 4000);
    }

    private void bindUserData() {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setUseMemCache(true)
                .setCircular(true)
                .build();
        x.image().bind(iv_user_photo, userInfoBean.getData().getAvatar(), imageOptions);
        tv_income_left.setText(userInfoBean.getData().getTotal_interest_month_q());
        tv_income_right.setText(userInfoBean.getData().getTotal_interest_month_h());
        tv_haitao_left.setText(userInfoBean.getData().getHaitao_amount_left());
        tv_haitao_right.setText(userInfoBean.getData().getHaitao_amount_right());
        tv_haitao_num.setText(userInfoBean.getData().getTotal_count() + "张海淘券可用");
        tv_wallet_income.setText("昨日收益：" + userInfoBean.getData().getToday_interrest() + "元");
        tv_user_invite.setText("已赚" + userInfoBean.getData().getTotal_interest_show() + "元特权本金");
        tv_user_capital.setText("(相当于" + userInfoBean.getData().getCapital_sum_user_interest() + "元)");
    }

    @Override
    public void prepareData() {

    }

    @Override
    public View prepareView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_user, null);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);

        tv_title.setText("用户中心");
        ll_back.setVisibility(View.GONE);

        ll_user_setting = (LinearLayout) view.findViewById(R.id.ll_user_setting);
        ll_personal_msg = (LinearLayout) view.findViewById(R.id.ll_personal_msg);
        rl_user_income_mayi = (RelativeLayout) view.findViewById(R.id.rl_user_income_mayi);
        rl_user_haitao_income = (RelativeLayout) view.findViewById(R.id.rl_user_haitao_income);
        rl_user_wallet = (RelativeLayout) view.findViewById(R.id.rl_user_wallet);
        rl_user_haitao = (RelativeLayout) view.findViewById(R.id.rl_user_haitao);
        rl_user_invite = (RelativeLayout) view.findViewById(R.id.rl_user_invite);
        ll_user_recharge = (LinearLayout) view.findViewById(R.id.ll_user_recharge);
        ll_user_invest = (LinearLayout) view.findViewById(R.id.ll_user_invest);
        ll_user_banner = (RelativeLayout) view.findViewById(R.id.ll_user_banner);


        iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
        tv_income_left = (TextView) view.findViewById(R.id.tv_income_left);
        tv_income_right = (TextView) view.findViewById(R.id.tv_income_right);
        tv_haitao_left = (TextView) view.findViewById(R.id.tv_haitao_left);
        tv_haitao_right = (TextView) view.findViewById(R.id.tv_haitao_right);
        tv_wallet_income = (TextView) view.findViewById(R.id.tv_wallet_income);
        tv_haitao_num = (TextView) view.findViewById(R.id.tv_haitao_num);
        tv_user_invite = (TextView) view.findViewById(R.id.tv_user_invite);
        tv_user_capital = (TextView) view.findViewById(R.id.tv_user_capital);
        ll_article_gone = (LinearLayout) view.findViewById(R.id.ll_article_gone);
        iv_wallet_icon = (ImageView) view.findViewById(R.id.iv_wallet_icon);
        iv_haitao_icon = (ImageView) view.findViewById(R.id.iv_haitao_icon);
        iv_invite_icon = (ImageView) view.findViewById(R.id.iv_invite_icon);

        vertical_viewpager = (VerticalViewPager) view.findViewById(R.id.vertical_viewpager);

        ll_user_setting.setOnClickListener(this);
        ll_personal_msg.setOnClickListener(this);
        rl_user_income_mayi.setOnClickListener(this);
        rl_user_haitao_income.setOnClickListener(this);
        rl_user_wallet.setOnClickListener(this);
        rl_user_haitao.setOnClickListener(this);
        rl_user_invite.setOnClickListener(this);
        ll_user_recharge.setOnClickListener(this);
        ll_user_invest.setOnClickListener(this);
        ll_article_gone.setOnClickListener(this);

//        if (!CacheUtils.getString(getActivity(), ConstantUtils.SP_SECRET).isEmpty()) {
//            Log.e("getUserData", "getUserData------");
//            getUserData();
//        }else{
//            startActivity(new Intent(getActivity(),LoginActivity.class));
//        }
        if (System.currentTimeMillis() - CacheUtils.getLong(getActivity(), ConstantUtils.SP_ARTICLE) > 86400000) {
            ll_user_banner.setVisibility(View.VISIBLE);
        } else {
            ll_user_banner.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onloadData(View view) {

    }

    @Override
    public void adapterScreen() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_msg:
                startWebActivity(ConstantUtils.USER_MESSAGE);
                break;
            case R.id.ll_user_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_user_income_mayi:
                startWebActivity(ConstantUtils.USER_WALLET);
                break;
            case R.id.rl_user_haitao_income:
                startWebActivity(ConstantUtils.USER_COUPON);
                break;
            case R.id.rl_user_wallet:
                startWebActivity(ConstantUtils.USER_WALLET);
                break;
            case R.id.rl_user_haitao:
                startWebActivity(ConstantUtils.USER_COUPON);
                break;
            case R.id.rl_user_invite:
                startWebActivity(ConstantUtils.USER_INVITE);
                break;
            case R.id.ll_user_recharge:
                startWebActivity(ConstantUtils.RECHARGE);
                break;
            case R.id.ll_user_invest:
                MainActivity.tabHost.setCurrentTab(0);
                break;
            case R.id.ll_article_gone:
                ll_user_banner.setVisibility(View.GONE);
                CacheUtils.setLong(getActivity(), ConstantUtils.SP_ARTICLE, System.currentTimeMillis());
                break;
        }
    }

    private void startWebActivity(String url) {
        Intent intent = new Intent();
        intent.putExtra("url", ConstantUtils.HOST + url);
        intent.setClass(getActivity(), WebActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!CacheUtils.getString(getActivity(), ConstantUtils.SP_SECRET).equals("")) {
            getUserData();
            getArticle();
        } else {
            MainActivity.tabHost.setCurrentTab(0);
        }
    }

    private void getArticle() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.ARTICLE);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        params.addBodyParameter("signature", CacheUtils.getString(getActivity(), ConstantUtils.SP_SIGNATURE));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("FragmentUser", "getArticle" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String errcode = jsonObject.getString("errcode");
                    if (!errcode.equals("")) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                articleBean = new Gson().fromJson(result, ArticleBean.class);
                articleList = articleBean.getData();
                handler.sendEmptyMessage(GET_ARTICLE);
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

    private void getUserData() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.USER_INFO);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        params.addBodyParameter("signature", CacheUtils.getString(getActivity(), ConstantUtils.SP_SIGNATURE));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUserData", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String errcode = jsonObject.getString("errcode");
                    if (!errcode.equals("")) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                userInfoBean = new Gson().fromJson(result, UserInfoBean.class);
                handler.sendEmptyMessage(GET_USER_DATA);
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

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
