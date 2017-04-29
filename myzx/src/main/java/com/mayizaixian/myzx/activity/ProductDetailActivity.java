package com.mayizaixian.myzx.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.mayizaixian.myzx.R;

public class ProductDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View getChildView() {
        View view=View.inflate(this,R.layout.activity_product_detail,null);

        return view;
    }
}
