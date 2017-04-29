package com.mayizaixian.myht.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mayizaixian.myht.activity.MainActivity;
import com.mayizaixian.myht.R;
import com.mayizaixian.myht.utils.CacheUtils;
import com.mayizaixian.myht.utils.ConstantUtils;

/**
 * Created by kiddo on 16-9-21.
 */

public class FragmentMy extends BaseFragment {
    @Override
    public void prepareData() {

    }

    @Override
    public View prepareView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        return view;
    }

    @Override
    public void onloadData(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!CacheUtils.getBoolean(getActivity(), ConstantUtils.SP_IS_LOGIN)) {
            MainActivity.tabHost.setCurrentTab(0);
        }
    }
}
