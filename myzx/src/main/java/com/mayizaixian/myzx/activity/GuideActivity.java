package com.mayizaixian.myzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.DensityUtil;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;

public class GuideActivity extends Activity {

    public static final String TAG = GuideActivity.class.getSimpleName();

    private ArrayList<ImageView> imageViews;

    private ViewPager viewpager;
    private TextView tv_start_main;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    int bg_guide[] = {R.color.bg_guide_1, R.color.bg_guide_2, R.color.bg_guide_3, R.color.bg_guide_4};

    /**
     * 两点间的间距
     */
    private int maxLeft;
    /**
     * 点在屏幕上的坐标
     */
    private float margLeft;

    private int dipsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setBackgroundColor(getResources().getColor(bg_guide[0]));
        tv_start_main = (TextView) findViewById(R.id.tv_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        //把这个单位当成dip -10 -
        dipsize = DensityUtil.dip2px(this, 10);
        Log.e(TAG, "dipsize====" + dipsize);

        int ids[] = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};

        //把图片资源转换-ImageView-->并且放入集合中
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            //根据资源id创建对应的ImageView
            ImageView imageView = new ImageView(this);
//            imageView.setBackgroundResource(ids[i]);
            imageView.setImageResource(ids[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            //加入到集合中
            imageViews.add(imageView);

            //添加灰色的点-有多少个页面就添加多少个点击
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);

            //10个像素 px
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dipsize, dipsize);//
            if (i != 0) {
                params.leftMargin = dipsize;
            }

            point.setLayoutParams(params);

            //图片
            //shape
            //放入到线性布局
            ll_point_group.addView(point);

        }


        //设置ViewPager的适配器
        viewpager.setAdapter(new MyPagerAdapter());

        //View从实例化到显示过程中的主要方法
        //两点的间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
        //onMeasure();-->onLayout()-->onDraw();
        //监听
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener
                ());

        //页面滑动了总页面宽的百分比
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());


        //设置监听按钮点击进入主页面
        tv_start_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CacheUtils.setBoolean(GuideActivity.this, HomeActivity.IS_START_MAIN, true);

                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);

                //关于引导页面
                finish();

            }
        });

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面回调了的时候回调这个方法
         * position：滑动的页面的下标位置
         * positionOffset：这个页面滑动的百分比
         * positionOffsetPixels：滑动了当前页面的多少个像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {


            //两点间滑动的距离 = 两点的间距*页面滑动了总页面宽的百分比
//			 margLeft = maxLeft *positionOffset;

            //两点间滑动在屏幕上的坐标 = 起始坐标 + 两点间滑动的距离后对应的坐标
//			 margLeft = maxLeft*position +maxLeft *positionOffset;
            margLeft = maxLeft * (position + positionOffset);
//			 Log.e(TAG, "position=="+position+",positionOffset=="+positionOffset+",
// positionOffsetPixels=="+positionOffsetPixels+",margLeft=="+margLeft);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dipsize, dipsize);
            params.leftMargin = (int) margLeft;//这种方式可以
//			 params.setMargins((int) margLeft, 0, 0, 0);//可以
            iv_red_point.setLayoutParams(params);

            //下面方式效果不行，但是最终也能实现类似效果
//			 if(positionOffsetPixels >0){
//				 iv_red_point.layout((int) margLeft, iv_red_point.getTop(), (int)
// (margLeft+iv_red_point.getWidth()), iv_red_point.getBottom());
//			 }

            Log.e(TAG, "margLeft==" + margLeft + ",t==" + iv_red_point.getTop() + ",r==" + (int)
                    (margLeft + iv_red_point.getWidth()) + ",b==" + iv_red_point.getBottom());
//
        }

        /**
         * 当某个页面被选中的时候回调这个方法
         */
        @Override
        public void onPageSelected(int position) {
            viewpager.setBackgroundColor(getResources().getColor(bg_guide[position]));
            if (position == (imageViews.size() - 1)) {//最后一个页面，2
                //把按钮显示出来
                tv_start_main.setVisibility(View.VISIBLE);
            } else {
                //把按钮隐藏
                tv_start_main.setVisibility(View.GONE);
            }

        }

        /**
         * 当页面的状态发送变化的时候回调这个方法
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            // TODO Auto-generated method stub
            //两点的间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
            maxLeft = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0)
                    .getLeft();
            Log.i(TAG, "maxLeft==" + maxLeft);
        }

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 相当于getView();
         * container：其实就是ViewPager
         * position:要实例化对应页面的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            ImageView imageView = imageViews.get(position);//更加位置得到对应的数据
            container.addView(imageView);//把对应的页面添加到容器中(ViewPager)
            //返回能够代表和当前这个控件有关系的对象就行
            //return position;//第一种方案
            return imageView;
        }

        /**
         * view：当前view
         * object:是有instantiateItem()方法返回的对象--position
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//			return view==imageViews.get(Integer.parseInt((String) object));//第一种方案
            return view == object;
        }


        /**
         * container:ViewPager
         * position:要移除页面的位置
         * object:要移除页面的对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
