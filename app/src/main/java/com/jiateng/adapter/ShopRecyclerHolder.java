package com.jiateng.adapter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ShopRecyclerHolder extends RecyclerView.ViewHolder {

    private SparseArrayCompat<View> mViews;

    public ShopRecyclerHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArrayCompat<>();
    }

    public <V extends View> V getView(@IdRes int ids) {
        View v = mViews.get(ids);
        if (v == null) {
            v = itemView.findViewById(ids);
            mViews.put(ids, v);
        }
        return (V) v;
    }
}
