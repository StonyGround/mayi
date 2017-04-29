package com.mayizaixian.myzx.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.WebActivity;
import com.mayizaixian.myzx.adapter.AdapterHomeBanner;
import com.mayizaixian.myzx.adapter.AdapterHomeRecyler;
import com.mayizaixian.myzx.bean.BannerBean;
import com.mayizaixian.myzx.bean.HomeBean;
import com.mayizaixian.myzx.bean.IndexDevBean;
import com.mayizaixian.myzx.http.HttpPostMap;
import com.mayizaixian.myzx.presenter.PresenterHome;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.MyRecyclerOnScrollListener;
import com.mayizaixian.myzx.view.ViewHome;
import com.mayizaixian.myzx.widget.HorizontalScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/13.
 */
public class FragmentHome extends BaseFragment implements ViewHome, SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {


    private PresenterHome presenterHome;

    /**
     * 上拉加载
     */
    private boolean isUpPullRefresh = false;

    private SwipeRefreshLayout home_swipe;
    private RecyclerView home_recyclerview;
    private LinearLayoutManager mLinearLayoutManager;

    private HorizontalScrollViewPager viewpager_banner;

    private int page = 1;
    private BannerBean bannerBean;
    private List<BannerBean.DataBean> bannerList = new ArrayList<>();
    private HomeBean homeBean;
    private IndexDevBean indexDevBean;
    private List<HomeBean.DataBean> homeList = new ArrayList<>();

    private int currentItem = Integer.MAX_VALUE / 2;
    private AdapterHomeRecyler mAdapter;
    private LinearLayout ll_point_group;

    private int preSelectPositon = 0;

    private final static int NOTIFY = 1;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.GET_BANNER_DATA:
                    getBannerListData();
                    break;
                case ConstantUtils.INIT_RECYCLER_VIEW:
                    initRecyclerView();
                    break;
                case ConstantUtils.GET_INDEX_DEV:
                    getIndexDev();
                    break;
                case NOTIFY:
                    mAdapter.notifyItemRangeChanged(1, homeList.size() + 1);
                    handler.sendEmptyMessageDelayed(NOTIFY, 1000);
                    break;
                case ConstantUtils.BANNER_START_ROLLING:
                    currentItem++;
                    viewpager_banner.setCurrentItem(currentItem);
                    if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
                        handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
                    }
                    handler.postDelayed(new InternalRunnable(), 4000);
                    break;
                case ConstantUtils.BANNER_CHANGE_ROLLING:
                    currentItem = msg.arg1;
                    handler.postDelayed(new InternalRunnable(), 4000);
                    break;
            }
        }
    };

    @Override
    public void prepareData() {
//        presenterHome = new PresenterHome(this);
//        Map<String, String> map = new HashMap<>();
//        map.put("access_token", CacheUtils.getString(getActivity(),ConstantUtils.SP_TOKEN));
//        map.put("page", String.valueOf(page));
//        HttpPostMap postMap = new HttpPostMap(getActivity(), ConstantUtils.HOST + ConstantUtils.HOME_BANNER, map);
//        postMap.postBackInMain(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                String json = msg.getData().getString(ConstantUtils.DATA_ON_NET);
//                Log.e("FragmentHome", "FragmentHome" + json);
//            }
//        });

    }

    private View topView;
    private LinearLayout ll_index_dev;
    private TextView tv_index_dev;
    private ImageView iv_index_dev;
    private LinearLayout ll_home_safe_1;
    private LinearLayout ll_home_safe_2;
    private LinearLayout ll_home_safe_3;
    private LinearLayout ll_home_safe_4;

    @Override
    public View prepareView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        home_swipe = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe);
        home_recyclerview = (RecyclerView) view.findViewById(R.id.home_recyclerview);

        home_swipe.setColorSchemeResources(R.color.black);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        home_recyclerview.setLayoutManager(mLinearLayoutManager);

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.home_top, home_recyclerview, false);
        viewpager_banner = (HorizontalScrollViewPager) topView.findViewById(R.id.viewpager_banner);
        ll_index_dev = (LinearLayout) topView.findViewById(R.id.ll_index_dev);
        tv_index_dev = (TextView) topView.findViewById(R.id.tv_index_dev);
        iv_index_dev = (ImageView) topView.findViewById(R.id.iv_index_dev);
        ll_home_safe_1 = (LinearLayout) topView.findViewById(R.id.ll_home_safe_1);
        ll_home_safe_2 = (LinearLayout) topView.findViewById(R.id.ll_home_safe_2);
        ll_home_safe_3 = (LinearLayout) topView.findViewById(R.id.ll_home_safe_3);
        ll_home_safe_4 = (LinearLayout) topView.findViewById(R.id.ll_home_safe_4);

        ll_home_safe_1.setOnClickListener(this);
        ll_home_safe_2.setOnClickListener(this);
        ll_home_safe_3.setOnClickListener(this);
        ll_home_safe_4.setOnClickListener(this);

        home_swipe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        home_swipe.setRefreshing(true);

        getHomeListData();

        home_swipe.setOnRefreshListener(this);

        return view;
    }

    public void getHomeListData() {
        Log.e("FragmentHome", "getHomeListData-------");
        if (isUpPullRefresh) {
            page++;
        }
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.HOME_LIST);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("getHomeListData", result);
                String errcode = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    errcode = jsonObject.getString("errcode");
                    if (errcode.equals("1001")) {
                        getNewToken();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (errcode.equals("")) {
                    homeBean = new Gson().fromJson(result, HomeBean.class);

                    if (isUpPullRefresh) {
                        mAdapter.addDatas(homeBean.getData());
                        home_swipe.setRefreshing(false);
                    } else {
                        homeList = homeBean.getData();
                        handler.sendEmptyMessage(ConstantUtils.GET_BANNER_DATA);
                    }
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

    private void getNewToken() {

        CacheUtils.setString(getActivity(), ConstantUtils.SP_SECRET, "");

        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.GET_TOKEN);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    CacheUtils.setString(getActivity(), ConstantUtils.SP_TOKEN, jsonObject.getString("token"));
                    getHomeListData();
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

    private void getBannerListData() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.HOME_BANNER);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                bannerBean = new Gson().fromJson(result, BannerBean.class);
                bannerList = bannerBean.getData();
                handler.sendEmptyMessage(ConstantUtils.GET_INDEX_DEV);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.showToast(getActivity(), ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getIndexDev() {
        RequestParams requestParams = new RequestParams(ConstantUtils.HOST + ConstantUtils.INDEX_DEV);
        requestParams.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        requestParams.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        x.http().request(HttpMethod.POST, requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getIndexDev", result);
                indexDevBean = new Gson().fromJson(result, IndexDevBean.class);
                handler.sendEmptyMessage(ConstantUtils.INIT_RECYCLER_VIEW);
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


    private void initRecyclerView() {
        if (mAdapter != null) {
            mAdapter = null;
        }
        mAdapter = new AdapterHomeRecyler();
        home_recyclerview.setAdapter(mAdapter);
        handler.sendEmptyMessageDelayed(NOTIFY, 1000);
        mAdapter.addDatas(homeList);
        mAdapter.setOnItemClickListener(new AdapterHomeRecyler.OnItemClickListener() {
            @Override
            public void onItemClick(int position, HomeBean.DataBean data) {
                Intent intent = new Intent();
                intent.putExtra("url", ConstantUtils.HOST + "invest/detail/" + data.getBorrow_coupon_id());
                intent.setClass(getActivity(), WebActivity.class);
                startActivity(intent);
            }
        });

        setHeader();
        home_swipe.setRefreshing(false);

        home_recyclerview.setOnScrollListener(new MyRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                isUpPullRefresh = true;
                home_swipe.setRefreshing(true);
                getHomeListData();
            }
        });
    }

    private void setHeader() {

        if (indexDevBean.getData().getAdv_pic().equals("")) {
            if (indexDevBean.getData().getIs_display().equals("1")) {
                ll_index_dev.setVisibility(View.GONE);
            } else {
                iv_index_dev.setVisibility(View.GONE);
                tv_index_dev.setText(indexDevBean.getData().getName());
                ll_index_dev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonUtils.startWebActivity(getActivity(), indexDevBean.getData().getSummary().substring(1));
                    }
                });
            }
        } else {
            ll_index_dev.setVisibility(View.GONE);
            x.image().bind(iv_index_dev, ConstantUtils.HOST + indexDevBean.getData().getAdv_pic());
            iv_index_dev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonUtils.startWebActivity(getActivity(), indexDevBean.getData().getAdv_url().substring(1));
                }
            });
        }

        ll_point_group = (LinearLayout) topView.findViewById(R.id.ll_point_group);
        viewpager_banner.setAdapter(new AdapterHomeBanner(getActivity(), bannerList));
        viewpager_banner.setCurrentItem(currentItem);

        viewpager_banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (handler.hasMessages(NOTIFY)) {
                            handler.removeMessages(NOTIFY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!handler.hasMessages(NOTIFY)) {
                            handler.sendEmptyMessageDelayed(NOTIFY, 1000);
                        }
                        break;
                }
                return false;
            }
        });

        ll_point_group.removeAllViews();
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView point = new ImageView(getActivity());
            point.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            point.setLayoutParams(params);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
                params.leftMargin = 20;
            }

            // 把点添加到LinearLayout中
            ll_point_group.addView(point);
        }

        if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
            handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
        }
        handler.postDelayed(new InternalRunnable(), 4000);

        viewpager_banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
                    handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
                }
                Message message = new Message();
                message.arg1 = position;
                message.what = ConstantUtils.BANNER_CHANGE_ROLLING;
                handler.sendMessage(message);

                int diff = (Integer.MAX_VALUE / 2) % (bannerList.size());
                ll_point_group.getChildAt(preSelectPositon).setEnabled(false);
                ll_point_group.getChildAt((position - diff) % bannerList.size()).setEnabled(true);
//
                preSelectPositon = (position - diff) % bannerList.size();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        home_swipe.setEnabled(false);
                        if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
                            handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        home_swipe.setEnabled(true);
                        handler.postDelayed(new InternalRunnable(), 4000);
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setHeaderView(topView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_home_safe_1:
                CommonUtils.startWebActivity(getActivity(),ConstantUtils.HOME_SAFE_1);
                break;
            case R.id.ll_home_safe_2:
                CommonUtils.startWebActivity(getActivity(),ConstantUtils.HOME_SAFE_2);
                break;
            case R.id.ll_home_safe_3:
                CommonUtils.startWebActivity(getActivity(),ConstantUtils.HOME_SAFE_3);
                break;
            case R.id.ll_home_safe_4:
                CommonUtils.startWebActivity(getActivity(),ConstantUtils.HOME_SAFE_4);
                break;
        }
    }

    class InternalRunnable implements Runnable {

        @Override
        public void run() {
            handler.sendEmptyMessage(ConstantUtils.BANNER_START_ROLLING);
        }

    }

    @Override
    public void onloadData(View view) {

    }

    @Override
    public void adapterScreen() {

    }

    @Override
    public void onRefresh() {
        Log.e("FragmentHome", "onRefresh-----");
        isUpPullRefresh = false;
        page = 1;
        currentItem = Integer.MAX_VALUE / 2;
        preSelectPositon = 0;
        getHomeListData();
    }


    @Override
    public RecyclerView getRecyclerView() {
        return home_recyclerview;
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if(home_swipe.isRefreshing()){
            home_swipe.setRefreshing(false);
        }
        super.onResume();
    }
}
