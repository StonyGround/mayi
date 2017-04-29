package com.mayizaixian.myzx.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.bean.HomeBean;
import com.mayizaixian.myzx.utils.DaysTimer;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class AdapterHomeRecyler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int NOTIFY = 2;
    private DaysTimer daysTimer;

    long restTime = 0;

    private List<HomeBean.DataBean> mDatas = new ArrayList<>();

    private View mHeaderView;

    private TextView textView;

    private OnItemClickListener mListener;
    int position;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addDatas(List<HomeBean.DataBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void removeDatas(){
        mDatas.removeAll(mDatas);
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        this.position = position;
        final int pos = getRealPosition(viewHolder);
        final HomeBean.DataBean data = mDatas.get(pos);
        if (viewHolder instanceof Holder) {
//            ((Holder) viewHolder).text.setText(data);
            ((Holder) viewHolder).tv_home_title.setText(data.getCat());
            ((Holder) viewHolder).tv_home_origin.setText(data.getBorrow_title());
            x.image().bind(((Holder) viewHolder).iv_home_image, data.getPic());
            x.image().bind(((Holder) viewHolder).iv_home_titlepic, data.getTitle_pic());
            ((Holder) viewHolder).tv_home_income.setText(formatDiscount(data.getApr()));
            if ((int) data.getTotal_discount() > 0) {
                ((Holder) viewHolder).tv_home_discount.setText(String.valueOf(data.getDiscount()));
            } else {
                ((Holder) viewHolder).tv_home_discount.setText("无");
            }
            ((Holder) viewHolder).tv_home_highest.setText(formatDiscount(data.getTotal_discount()) + "%");
            ((Holder) viewHolder).tv_home_valid.setText(data.getTime_limit() + "天");
            boolean is_repay = false;
            if (data.getIs_repay() == null) {
                is_repay = false;
            } else {
                is_repay = (boolean) data.getIs_repay();
            }
            if (data.getStatus().equals("5")) {
                ((Holder) viewHolder).rl_home_status.setBackgroundResource(R.drawable.shape_invest_button_grey);
                if (data.getIs_full() && is_repay) {
                    ((Holder) viewHolder).tv_home_status.setText("还款已完毕");
                } else {
                    ((Holder) viewHolder).tv_home_status.setText("正在计息中");
                }
            }
            if (data.getStatus().equals("2")) {
                if (data.getIs_full()) {
                    ((Holder) viewHolder).rl_home_status.setBackgroundResource(R.drawable.shape_invest_button_grey);
                    ((Holder) viewHolder).tv_home_status.setText("已售罄");
                } else if (data.getIs_time()) {
                    ((Holder) viewHolder).rl_home_status.setBackgroundResource(R.drawable.shape_invest_button);
                    ((Holder) viewHolder).tv_home_status.setText("立即投资");

                } else {
//                    Log.e("countDown", "countDown------");
//                    ((Holder) viewHolder).rl_home_status.setBackgroundResource(R.drawable.shape_invest_button);
//                    countDown(((Holder) viewHolder).tv_home_status, data);
//                    ((Holder) viewHolder).tv_home_status.setText("倒计时");
                    restTime = Long.valueOf(data.getTime_start()) - System.currentTimeMillis() / 1000;
                    long second = restTime % 60;
                    long minute = restTime / 60 % 60;
                    long hour = restTime / 60 / 60 % 24;
                    long day = restTime / 60 / 60 / 24;
                    if (restTime > 0) {
                        ((Holder) viewHolder).tv_home_status.setText(day + "天" + hour + "小时" + minute + "分" + second + "秒");
                    } else {
                        ((Holder) viewHolder).tv_home_status.setText("立即投资");
                    }
                }
            }
//            if (((Holder) viewHolder).tv_home_status.getText().toString().equals("立即投资")) {
//                ((Holder) viewHolder).rl_home_surprise.setVisibility(View.VISIBLE);
//            }
            if (mListener == null) return;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
        }
    }

    private void countDown(TextView textView, HomeBean.DataBean data) {
        long startTime = Long.valueOf(data.getTime_start()) * 1000;
        long nowTime = System.currentTimeMillis();
        Log.e("countDown", startTime + "------");
        Log.e("countDown", nowTime + "------");
        long millisInFuture = startTime - nowTime;
        if (daysTimer != null) {
            daysTimer = null;
        }
        daysTimer = new DaysTimer(textView, millisInFuture, 1000);
        daysTimer.start();
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
//        int position = holder.getLayoutPosition();
        int position = holder.getPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_home_title;
        TextView tv_home_origin;
        ImageView iv_home_image;
        ImageView iv_home_titlepic;
        TextView tv_home_income;
        TextView tv_home_discount;
        TextView tv_home_highest;
        TextView tv_home_valid;
        TextView tv_home_status;
        RelativeLayout rl_home_status;
        RelativeLayout rl_home_surprise;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_home_title = (TextView) itemView.findViewById(R.id.tv_home_title);
            tv_home_origin = (TextView) itemView.findViewById(R.id.tv_home_origin);
            iv_home_image = (ImageView) itemView.findViewById(R.id.iv_home_image);
            iv_home_titlepic = (ImageView) itemView.findViewById(R.id.iv_home_titlepic);
            tv_home_income = (TextView) itemView.findViewById(R.id.tv_home_income);
            tv_home_discount = (TextView) itemView.findViewById(R.id.tv_home_discount);
            tv_home_highest = (TextView) itemView.findViewById(R.id.tv_home_highest);
            tv_home_status = (TextView) itemView.findViewById(R.id.tv_home_status);
            tv_home_valid = (TextView) itemView.findViewById(R.id.tv_home_valid);
            rl_home_status = (RelativeLayout) itemView.findViewById(R.id.rl_home_status);
            rl_home_surprise = (RelativeLayout) itemView.findViewById(R.id.rl_home_surprise);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, HomeBean.DataBean data);
    }

    public String formatDiscount(double d) {
        String s = String.valueOf(d);
        if (d * 10 % 10 == 0) {
            s = s.substring(0, s.indexOf("."));
        }
        return s;
    }
}