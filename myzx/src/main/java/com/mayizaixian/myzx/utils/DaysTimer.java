package com.mayizaixian.myzx.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DaysTimer extends CountDownTimer {
    private TextView textView;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public DaysTimer(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long second = millisUntilFinished / 1000 % 60;
        long minute = millisUntilFinished / 1000 / 60 % 60;
        long hour = millisUntilFinished / 1000 / 60 / 60 % 24;
        long day = millisUntilFinished / 1000 / 60 / 60 / 24;
//        textView.setText(day + "天" + hour + "小时" + minute + "分" + second + "秒");
        textView.setText(millisUntilFinished /1000+ "秒");
    }

    @Override
    public void onFinish() {
        textView.setText("立即投资");
    }
}
