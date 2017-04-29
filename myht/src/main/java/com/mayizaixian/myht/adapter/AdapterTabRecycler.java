package com.mayizaixian.myht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mayizaixian.myht.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiddo on 16-9-22.
 */

public class AdapterTabRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<String> mDatas = new ArrayList<>();

    private View mHeaderView;

    private Context context;

    public AdapterTabRecycler(Context context) {
        this.context = context;
    }

//    private OnItemClickListener mListener;
//
//    public void setOnItemClickListener(OnItemClickListener li) {
//        mListener = li;
//    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addDatas(List<String> datas) {
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
        View layout = LayoutInflater.from(context).inflate(R.layout.item_hot, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
//
//        final int pos = getRealPosition(viewHolder);
//        final String data = mDatas.get(pos);
//        if (viewHolder instanceof Holder) {
//
//        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
//        int position = holder.getLayoutPosition();
        int position = holder.getPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? 5 : 5 + 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tv_tab_price;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_tab_price = (TextView) itemView.findViewById(R.id.tv_tab_price);
        }
    }

//    public interface OnItemClickListener {
//        void onItemClick(int position, FindListBean.ListBean data);
//    }
}
