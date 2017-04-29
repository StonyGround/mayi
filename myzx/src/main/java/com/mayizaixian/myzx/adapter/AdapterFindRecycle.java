package com.mayizaixian.myzx.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.bean.FindListBean;
import com.mayizaixian.myzx.bean.HomeBean;
import com.mayizaixian.myzx.widget.HorizontalScrollViewPager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/14.
 */
public class AdapterFindRecycle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<FindListBean.ListBean> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener mListener;

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

    public void addDatas(List<FindListBean.ListBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(viewHolder);
        final FindListBean.ListBean data = mDatas.get(pos);
        if (viewHolder instanceof Holder) {
            x.image().bind(((Holder) viewHolder).iv_find_activity, data.getImage());
            ((Holder) viewHolder).tv_find_days.setText("剩" + data.getDays() + "天");
            if (Integer.valueOf(data.getDays()) > 365) {
                ((Holder) viewHolder).tv_find_days.setText("长期");
            }
            ((Holder) viewHolder).tv_find_name.setText(data.getActivity_name());
            ((Holder) viewHolder).tv_find_count.setText(data.getCount());
            if (data.getOver().equals("act_cur")) {
                ((Holder) viewHolder).ll_find_restday.setVisibility(View.GONE);
                ((Holder) viewHolder).iv_find_over.setVisibility(View.VISIBLE);
                ((Holder) viewHolder).rl_find_over.setVisibility(View.VISIBLE);
                viewHolder.itemView.setClickable(false);
            } else {
                if (mListener == null) return;
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos, data);
                    }
                });
            }
        }
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

        ImageView iv_find_activity;
        TextView tv_find_days;
        TextView tv_find_name;
        TextView tv_find_count;
        ImageView iv_find_over;
        LinearLayout ll_find_restday;
        RelativeLayout rl_find_over;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            iv_find_activity = (ImageView) itemView.findViewById(R.id.iv_find_activity);
            tv_find_days = (TextView) itemView.findViewById(R.id.tv_find_days);
            tv_find_name = (TextView) itemView.findViewById(R.id.tv_find_name);
            tv_find_count = (TextView) itemView.findViewById(R.id.tv_find_count);
            iv_find_over = (ImageView) itemView.findViewById(R.id.iv_find_over);
            ll_find_restday = (LinearLayout) itemView.findViewById(R.id.ll_find_restday);
            rl_find_over = (RelativeLayout) itemView.findViewById(R.id.rl_find_over);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, FindListBean.ListBean data);
    }
}
