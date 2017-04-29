package com.mayizaixian.myht.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mayizaixian.myht.tabPage.HotPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiddo on 16-9-21.
 */

public class AdapterTabIndicator extends PagerAdapter {

    private Context context;
    private List<String> titleList = new ArrayList<>();

    private SwipeRefreshLayout tab_swipe;
    private RecyclerView tab_recyclerview;

    public AdapterTabIndicator(Context context, List<String> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HotPage hotPage = new HotPage(context);
        View view = hotPage.rootView;
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
