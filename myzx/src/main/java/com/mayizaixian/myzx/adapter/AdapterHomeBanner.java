package com.mayizaixian.myzx.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.activity.LoginActivity;
import com.mayizaixian.myzx.activity.WebActivity;
import com.mayizaixian.myzx.bean.BannerBean;
import com.mayizaixian.myzx.utils.ConstantUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 */
public class AdapterHomeBanner extends PagerAdapter {

    private Context context;

    private List<BannerBean.DataBean> bannerList;

    public AdapterHomeBanner(Context context, List<BannerBean.DataBean> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
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
    public Object instantiateItem(ViewGroup container,final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final int diff = (Integer.MAX_VALUE / 2) % (bannerList.size());
        x.image().bind(imageView, bannerList.get((position - diff) % bannerList.size()).getPic());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("url", bannerList.get((position - diff) % bannerList.size()).getUrl());
                intent.setClass(context, WebActivity.class);
                context.startActivity(intent);
            }
        });


        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
