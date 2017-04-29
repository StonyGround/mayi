package com.mayizaixian.myzx.model;


import android.content.Context;
import android.os.Handler;

import com.mayizaixian.myzx.bean.BannerBean;
import com.mayizaixian.myzx.bean.HomeBean;
import com.mayizaixian.myzx.http.HttpCallBack;
import com.mayizaixian.myzx.http.HttpPostMap;
import com.mayizaixian.myzx.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ModelHome {

    private List<BannerBean> bannerList = new ArrayList<>();


    private List<HomeBean> homeList;

    private Context context;

    public ModelHome(Context context) {
        this.context = context;
    }

    public void setHomeList(HttpCallBack callBack, Handler handler, int page) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        HttpPostMap postMap = new HttpPostMap(context, ConstantUtils.HOST + ConstantUtils.HOME_LIST, map);
        postMap.postBackInBackground(callBack, handler);
    }

    public List<HomeBean> getHomeList() {
        return homeList;
    }

    public List<BannerBean> getBannerList() {
        return bannerList;
    }
}
