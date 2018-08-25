package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ViewItem extends BaseLocalAdapter<RecyclerView.ViewHolder> implements Cloneable {

    private int layoutResourceId;
    private View.OnClickListener clickListener;

    public ViewItem(@NonNull Context context, @NonNull int layoutResourceId) {
        this(context, layoutResourceId, null);
    }

    public ViewItem(@NonNull Context context, @NonNull int layoutResourceId, @Nullable View.OnClickListener clickListener) {
        super(context);
        this.layoutResourceId = layoutResourceId;
        this.clickListener = clickListener;
    }

    @Override
    public int getAdapterId() {
        return layoutResourceId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return new RecyclerView.ViewHolder(inflater.inflate(layoutResourceId, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public ViewItem clone() {
        return new ViewItem(getContext(), layoutResourceId, clickListener);
    }
}
