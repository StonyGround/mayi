package com.mayizaixian.myht.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mayizaixian.myht.fragment.FragmentDistribute;
import com.mayizaixian.myht.fragment.FragmentHome;
import com.mayizaixian.myht.fragment.FragmentMy;
import com.mayizaixian.myht.fragment.FragmentShoppingCart;
import com.mayizaixian.myht.R;
import com.mayizaixian.myht.utils.CacheUtils;
import com.mayizaixian.myht.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Class[] fragmentArray = new Class[]{FragmentHome.class, FragmentDistribute.class, FragmentShoppingCart.class, FragmentMy.class};

    private static final String[] tabTitles = new String[]{"海淘", "开店赚钱", "购物车", "我的"};

    private int[] imgRes = new int[]{R.drawable.tab_home_btn, R.drawable.tab_distribute_btn, R.drawable.tab_shoppingcart_btn, R.drawable.tab_my_btn};

    private static List<View> tabList;

    public static FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CacheUtils.setBoolean(MainActivity.this, ConstantUtils.SP_IS_LOGIN, true);

        tabList = getTabViewList(tabTitles.length);
        initiTabHost();
    }

    private void initiTabHost() {
        int tab = getIntent().getIntExtra("tab", 0);
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        tabHost.setCurrentTab(tab);
        tabHost.setup(this, getSupportFragmentManager(), R.id.content);
        tabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < fragmentArray.length; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabTitles[i]).setIndicator(tabList.get(i));
            //将Tab按钮添加进Tab选项卡中
            tabHost.addTab(tabSpec, fragmentArray[i], null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("我的")) {
                    if (!CacheUtils.getBoolean(MainActivity.this, ConstantUtils.SP_IS_LOGIN)) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }
            }
        });

    }

    private List<View> getTabViewList(int len) {
        List<View> list = new ArrayList<>();
        if (len <= 0) return list;
        for (int i = 0; i < len; i++) {
            list.add(getTabItemView(i));
        }
        return list;
    }

    private View getTabItemView(int tabIndex) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(imgRes[tabIndex]);
        return view;
    }
}
