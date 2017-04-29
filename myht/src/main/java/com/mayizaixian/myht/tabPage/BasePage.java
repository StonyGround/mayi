package com.mayizaixian.myht.tabPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by kiddo on 16-9-22.
 */

public abstract class BasePage {

    public View rootView;

    public Context context;

    public LayoutInflater inflater;

    public BasePage(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        rootView = initView(inflater);
    }

    protected abstract View initView(LayoutInflater inflater);
}
