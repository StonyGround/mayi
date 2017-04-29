package com.mayizaixian.myht.tabPage;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mayizaixian.myht.adapter.AdapterTabBanner;
import com.mayizaixian.myht.adapter.AdapterTabRecycler;
import com.mayizaixian.myht.R;
import com.mayizaixian.myht.utils.CommonUtil;
import com.mayizaixian.myht.widget.HorizontalScrollViewPager;

/**
 * Created by kiddo on 16-9-22.
 */

public class HotPage extends BasePage {

    private SwipeRefreshLayout tab_swipe;
    private RecyclerView tab_recyclerview;
    private AdapterTabRecycler adapterTabRecycler;
    private HorizontalScrollViewPager viewpager_banner;
    private LinearLayout ll_point_group;
    private int dipsize_5;

    public HotPage(Context context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.home_tab, null);
        tab_swipe = (SwipeRefreshLayout) view.findViewById(R.id.tab_swipe);
        tab_recyclerview = (RecyclerView) view.findViewById(R.id.tab_recyclerview);

        adapterTabRecycler = new AdapterTabRecycler(context);

        tab_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        tab_recyclerview.setAdapter(adapterTabRecycler);

        initHeadView();

        return view;
    }

    private void initHeadView() {
        View topView = inflater.inflate(R.layout.home_tab_top, tab_recyclerview, false);
        viewpager_banner = (HorizontalScrollViewPager) topView.findViewById(R.id.viewpager_banner);
        viewpager_banner.setAdapter(new AdapterTabBanner(context));

        ll_point_group = (LinearLayout) topView.findViewById(R.id.ll_point_group);

        ll_point_group.removeAllViews();
        for (int i = 0; i < 4; i++) {
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);
            point.setScaleType(ImageView.ScaleType.FIT_CENTER);

            dipsize_5 = CommonUtil.dip2px(context, 5);

            int dipsize_15 = CommonUtil.dip2px(context, 15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dipsize_5, dipsize_5);



            if (i == 0) {
                point.setEnabled(true);
                params = new LinearLayout.LayoutParams(
                        dipsize_15, dipsize_5);
            } else {
                point.setEnabled(false);
                params.leftMargin = dipsize_5;
            }

            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }

        adapterTabRecycler.setHeaderView(topView);
    }
}
