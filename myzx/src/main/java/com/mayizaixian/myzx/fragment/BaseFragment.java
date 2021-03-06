package com.mayizaixian.myzx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mayizaixian.myzx.utils.ConstantUtils;

/**
 * Created by Administrator on 2015/10/10.
 */
public abstract class BaseFragment extends Fragment {

    private View view;
    private boolean isNeedAdapterScreen = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //准备数据
        prepareData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = prepareView(inflater, container);
            //加载数据
            onloadData(view);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    public abstract void prepareData();

    public abstract View prepareView(LayoutInflater inflater, ViewGroup container);

    public abstract void onloadData(View view);

    @Override
    public void onResume() {
        super.onResume();
        if (ConstantUtils.HEIGHT_SCREEN <= 0) {
            ConstantUtils.initiScreenParam(getActivity());
            isNeedAdapterScreen = true;
        }
        if (isNeedAdapterScreen) {
            adapterScreen();
            isNeedAdapterScreen = false;
        }
        resume();
    }

    public void resume() {

    }

    public abstract void adapterScreen();
}
