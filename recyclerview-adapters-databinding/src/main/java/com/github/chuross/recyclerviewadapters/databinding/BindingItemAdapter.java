package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.github.chuross.recyclerviewadapters.BaseItemAdapter;


public abstract class BindingItemAdapter<I, VH extends RecyclerView.ViewHolder> extends BaseItemAdapter<I, VH> {

    private ObservableList<I> observableList;
    private ObservableList.OnListChangedCallback<ObservableList<I>> callback;

    public BindingItemAdapter(@NonNull Context context, @NonNull ObservableList<I> observableList) {
        super(context);
        this.observableList = observableList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        callback = new ObservableList.OnListChangedCallback<ObservableList<I>>() {
            @Override
            public void onChanged(ObservableList<I> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<I> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<I> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<I> sender, int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<I> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        };
        observableList.addOnListChangedCallback(callback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (callback != null) observableList.removeOnListChangedCallback(callback);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return observableList.size();
    }

    @NonNull
    @Override
    public I get(int position) {
        return observableList.get(position);
    }
}
