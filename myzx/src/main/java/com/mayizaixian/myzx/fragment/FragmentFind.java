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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.WebActivity;
import com.mayizaixian.myzx.adapter.AdapterFindBanner;
import com.mayizaixian.myzx.adapter.AdapterFindRecycle;
import com.mayizaixian.myzx.bean.FindBannerBean;
import com.mayizaixian.myzx.bean.FindListBean;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.MyRecyclerOnScrollListener;
import com.mayizaixian.myzx.widget.HorizontalScrollViewPager;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class FragmentFind extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout find_swipe;
    private RecyclerView find_recyclerview;
    private LinearLayoutManager mLinearLayoutManager;

    private FindListBean findListBean;
    private FindBannerBean findBannerBean;
    private AdapterFindRecycle adapterFindRecycle;

    private List<FindListBean.ListBean> findList = new ArrayList<>();
    private List<FindBannerBean.BannerBean> findBannerList = new ArrayList<>();

    private HorizontalScrollViewPager find_banner_viewpager;
    private int currentItem;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConstantUtils.GET_BANNER_DATA:
                    getFindBannerList();
                    break;
                case ConstantUtils.INIT_RECYCLER_VIEW:
                    initRecyclerView();
                    break;
                case ConstantUtils.BANNER_START_ROLLING:
                    currentItem++;
                    find_banner_viewpager.setCurrentItem(currentItem);
                    if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
                        handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
                    }
                    handler.sendEmptyMessageDelayed(ConstantUtils.BANNER_START_ROLLING, 4000);
                    break;
                case ConstantUtils.BANNER_CHANGE_ROLLING:
                    currentItem = msg.arg1;
                    handler.sendEmptyMessageDelayed(ConstantUtils.BANNER_START_ROLLING, 4000);
                    break;
            }
        }
    };

    private void getFindBannerList() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.FIND_BANNER);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("getFindBannerList", "getFindBannerList" + result);
                findBannerBean = new Gson().fromJson(result, FindBannerBean.class);
                findBannerList = findBannerBean.getBanner();
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

    @Override
    public void prepareData() {

    }

    private TextView tv_title;
    private LinearLayout ll_back;

    @Override
    public View prepareView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_find, null);
        find_swipe = (SwipeRefreshLayout) view.findViewById(R.id.find_swipe);
        find_recyclerview = (RecyclerView) view.findViewById(R.id.find_recyclerview);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);

        tv_title.setText("发现中心");
        ll_back.setVisibility(View.GONE);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        find_recyclerview.setLayoutManager(mLinearLayoutManager);
        find_swipe.setColorSchemeResources(R.color.black);
        find_swipe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
//        initRecyclerView();
        find_swipe.setRefreshing(true);

        getFindListData();
        find_swipe.setOnRefreshListener(this);
        return view;
    }

    private int page = 1;
    /**
     * 上拉加载
     */
    private boolean isUpPullRefresh = false;

    private void getFindListData() {
        if (isUpPullRefresh) {
            page++;
            Log.e("FragmentHome", "page" + page);
        }

        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.FIND_LIST);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(getActivity(), ConstantUtils.SP_TOKEN));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("getFindListData", "getFindListData" + result);

                findListBean = new Gson().fromJson(result, FindListBean.class);

                if (isUpPullRefresh) {
                    adapterFindRecycle.addDatas(findListBean.getList());
                    find_swipe.setRefreshing(false);
                } else {
                    findList = findListBean.getList();
                    handler.sendEmptyMessage(ConstantUtils.GET_BANNER_DATA);
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

    private void initRecyclerView() {

        adapterFindRecycle = new AdapterFindRecycle();
        find_recyclerview.setAdapter(adapterFindRecycle);
        adapterFindRecycle.addDatas(findList);

        adapterFindRecycle.setOnItemClickListener(new AdapterFindRecycle.OnItemClickListener() {
            @Override
            public void onItemClick(int position, FindListBean.ListBean data) {
                Intent intent = new Intent();
                intent.putExtra("url", data.getUrl());
                intent.setClass(getActivity(), WebActivity.class);
                startActivity(intent);
            }
        });

        setHeader(find_recyclerview);
        find_swipe.setRefreshing(false);

        find_recyclerview.setOnScrollListener(new MyRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                isUpPullRefresh = true;
//                find_swipe.setRefreshing(true);
                getFindListData();
            }
        });
    }

    private void setHeader(RecyclerView view) {
        currentItem = Integer.MAX_VALUE / 2;
        View topView = LayoutInflater.from(getActivity()).inflate(R.layout.find_top, view, false);
        find_banner_viewpager = (HorizontalScrollViewPager) topView.findViewById(R.id.find_banner_viewpager);
        find_banner_viewpager.setAdapter(new AdapterFindBanner(getActivity(), findBannerList));
        find_banner_viewpager.setCurrentItem(currentItem);

        find_banner_viewpager.setClipToPadding(false);
        find_banner_viewpager.setPageMargin(20);

        if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
            handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
        }
        handler.sendEmptyMessageDelayed(ConstantUtils.BANNER_START_ROLLING, 4000);

        find_banner_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        find_swipe.setEnabled(false);
                        if (handler.hasMessages(ConstantUtils.BANNER_START_ROLLING)) {
                            handler.removeMessages(ConstantUtils.BANNER_START_ROLLING);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        find_swipe.setEnabled(true);
                        handler.sendEmptyMessageDelayed(ConstantUtils.BANNER_START_ROLLING, 4000);
                        break;
                    default:
                        break;
                }
            }
        });

        adapterFindRecycle.setHeaderView(topView);
    }

    @Override
    public void onloadData(View view) {

    }

    @Override
    public void adapterScreen() {

    }

    @Override
    public void onRefresh() {
        isUpPullRefresh = false;
        page = 1;
        getFindListData();
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if(find_swipe.isRefreshing()){
            find_swipe.setRefreshing(false);
        }
        super.onResume();
    }
}
