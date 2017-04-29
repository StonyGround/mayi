package com.mayizaixian.myzx.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.mayizaixian.myzx.R;
import com.umeng.analytics.MobclickAgent;

public class WalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected View getChildView() {
        View view = View.inflate(WalletActivity.this, R.layout.activity_wallet, null);
        return view;
    }
}
