package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.github.chuross.recyclerviewadapters.internal.EventExecutor;

public abstract class BaseItemAdapter<I, VH extends RecyclerView.ViewHolder> extends BaseLocalAdapter<VH> {

    private OnItemClickListener<I> clickListener;
    private OnItemLongPressedListener<I> longPressedListener;
    private GestureDetector gestureDetector;

    public BaseItemAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    public abstract I get(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewAttachedToWindow(final VH holder) {
        super.onViewAttachedToWindow(holder);
        final int position = getPosition(holder);
        if (position < 0) return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onItemClicked(holder, position, get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longPressedListener != null) longPressedListener.onItemLongPressed(holder, position, get(position));
                return true;
            }
        });
    }

    private int getPosition(VH holder) {
        if (!hasParentAdapter()) return holder.getAdapterPosition();

        LocalAdapterItem item = getParentAdapter().getLocalAdapterItem(holder.getAdapterPosition());
        if (item == null) return -1;
        return item.getLocalAdapterPosition();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<I> clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemLongPressListener(@Nullable OnItemLongPressedListener<I> longPressListener) {
        this.longPressedListener = longPressListener;
    }
}
