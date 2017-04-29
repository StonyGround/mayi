package com.mayizaixian.myzx.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mayizaixian.myzx.http.HttpCallBack;
import com.mayizaixian.myzx.http.ParseJson;
import com.mayizaixian.myzx.model.ModelHome;
import com.mayizaixian.myzx.view.ViewHome;

/**
 * Created by Administrator on 2016/6/14.
 */
public class PresenterHome implements BasePresenter {

    private ViewHome viewHome;
    private ModelHome modelHome;

    public PresenterHome(ViewHome viewHome) {
        this.viewHome = viewHome;
        modelHome=new ModelHome(viewHome.getActivity());
    }

    @Override
    public void initiData() {
//        viewHome.getRecyclerView().setAdapter(modelHome.getAdapterHomeRecycle());
//        modelHome.setHomeList();
    }

    @Override
    public void setOnClickListeners() {

    }

    @Override
    public void setOnItemClickListeners() {

    }

    private class HomeListCallBack implements HttpCallBack{

        @Override
        public boolean handleStr(String data) {
//            boolean isSucess = new ParseJson(data).pa(modelHome.getBeanHomeGoodsList(), totalPage, isFirstIniti);
            return false;
        }
    }
}
