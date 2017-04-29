package com.mayizaixian.myzx.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.mayizaixian.myzx.R;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MessageTimer extends CountDownTimer {
    private TextView textView;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MessageTimer(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setBackgroundResource(R.drawable.shape_codebutton_grey);
        textView.setClickable(false);
        textView.setText(millisUntilFinished / 1000 + "s后重发");
    }

    @Override
    public void onFinish() {
        textView.setBackgroundResource(R.drawable.shape_codebutton);
        textView.setText("发送验证码");
        textView.setClickable(true);
    }
}
