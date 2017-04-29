package com.mayizaixian.myzx.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.adapter.AdapterDetailBanner;
import com.mayizaixian.myzx.widget.HorizontalScrollViewPager;

public class DetailActivity extends BaseActivity {

    private HorizontalScrollViewPager detail_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View getChildView() {
        View view = View.inflate(this, R.layout.activity_detail, null);
        detail_viewpager = (HorizontalScrollViewPager) view.findViewById(R.id.detail_viewpager);
        detail_viewpager.setAdapter(new AdapterDetailBanner(this));
        return view;
    }
}
