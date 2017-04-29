package com.mayizaixian.myht.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mayizaixian.myht.adapter.AdapterTabIndicator;
import com.mayizaixian.myht.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiddo on 16-9-21.
 */

public class FragmentHome extends BaseFragment {

    private TabPageIndicator tab_indicator;
    private ViewPager viewpager_tab;

    private String[] title = {"爆款", "衣服", "饰品", "箱包", "配件", "家居", "办公"};
    private List<String> titleList = new ArrayList<>();

    @Override
    public void prepareData() {
        initTitle();
    }

    private void initTitle() {
        for (int i = 0; i < title.length; i++) {
            titleList.add(title[i]);
        }
    }

    @Override
    public View prepareView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        tab_indicator = (TabPageIndicator) view.findViewById(R.id.tab_indicator);
        viewpager_tab = (ViewPager) view.findViewById(R.id.viewpager_tab);

        viewpager_tab.setAdapter(new AdapterTabIndicator(getActivity(), titleList));
        tab_indicator.setViewPager(viewpager_tab);

        return view;
    }

    @Override
    public void onloadData(View view) {

    }

}
