package com.jiateng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.jiateng.bean.StoreBean;
import com.jiateng.bean.Subject;

public abstract class ShopRecyclerViewAdater<T> extends RecyclerView.Adapter<ShopRecyclerHolder> {

    private List<T> mData;
    public Context context;
    private List<Integer> resLayout;

    public ShopRecyclerViewAdater(Context context, int resLayout) {
        this.resLayout = new ArrayList<>();
        this.context = context;
        this.resLayout.add(resLayout);
        mData = new ArrayList<>();
    }

    public ShopRecyclerViewAdater(Context context, int resLayout, List<T> data) {
        this.resLayout = new ArrayList<>();
        this.context = context;
        this.resLayout.add(resLayout);
        mData = data;
    }

    //需要重写getItemViewType方法自行给布局分类
    public ShopRecyclerViewAdater(Context context, List<Integer> resLayouts) {
        this.context = context;
        this.resLayout = resLayouts;
        mData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public List<T> getmData() {
        return mData;
    }

    @NonNull
    @Override
    public ShopRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ShopRecyclerHolder holder = new ShopRecyclerHolder(LayoutInflater.from(context).inflate(resLayout.get(viewType), parent, false));
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopRecyclerHolder holder, int position) {
        convert(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 需要重写的方法
     *
     * @param holder
     * @param position
     */
    public abstract void convert(ShopRecyclerHolder holder, int position);


    public void setData(List<T> items) {
        this.mData = items == null ? new ArrayList<T>() : items;
        notifyDataSetChanged();
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(position);
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size());
    }


    public void remove(int position) {
        mData.remove(position);
        int internalPosition = position;
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public ShopRecyclerViewAdater setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    protected ShoppingItemClickListener shoppingItemClickListener;

    public void setShoppingItemClickListener(ShoppingItemClickListener listener) {
        this.shoppingItemClickListener = listener;
    }


    public interface ShoppingItemClickListener {
        void addClick(ShopRecyclerHolder holder, List<Subject> data, int position);

        void reduceClick(ShopRecyclerHolder holder, List<Subject> data, int position);
    }

    private RecyclerView recyclerView;

    public void bindToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
