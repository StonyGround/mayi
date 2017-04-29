package com.mayizaixian.myzx.activity;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayizaixian.myzx.R;

public abstract class BaseActivity extends Activity {

    protected TextView tv_title;
    protected FrameLayout fl_content_child;
    protected LinearLayout ll_back;
    protected RelativeLayout rl_titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        tv_title = (TextView) findViewById(R.id.tv_title);
        fl_content_child = (FrameLayout) findViewById(R.id.fl_content_child);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);

//        ll_back.setOnClickListener(mOnClickListener);

        fl_content_child.addView(getChildView());
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 让子View实现自己特有的效果
     *
     * @return
     */
    protected abstract View getChildView();
}
