package com.mayizaixian.myzx.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.WebActivity;
import com.mayizaixian.myzx.bean.ArticleBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class VerticalPagerAdapter extends PagerAdapter {

    private Context context;

    private List<ArticleBean.DataBean> articleList;

    public VerticalPagerAdapter(Context context, List<ArticleBean.DataBean> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        TextView textView = new TextView(context);
        textView.setText(articleList.get(position % articleList.size()).getName());
        textView.setTextColor(context.getResources().getColor(R.color.article));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Log.e("instantiateItem", "url---" + articleList.get(position % articleList.size()).getUrl());
                intent.putExtra("url", articleList.get(position % articleList.size()).getUrl());
                intent.setClass(context, WebActivity.class);
                context.startActivity(intent);
            }
        });

        container.addView(textView);

        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
