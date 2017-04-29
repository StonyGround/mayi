package com.mayizaixian.myht.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mayizaixian.myht.R;

/**
 * Created by Administrator on 2016/6/15.
 */
public class AdapterTabBanner extends PagerAdapter {

    private Context context;

    private int[] imageList = {R.drawable.tab_test, R.drawable.tab_test, R.drawable.tab_test, R.drawable.tab_test};

    private Bitmap bitmap;

    public AdapterTabBanner(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(ContextCompat.getDrawable(context,imageList[position]));
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
