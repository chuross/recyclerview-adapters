package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chuross.recyclerviewadapters.BaseLocalAdapter;
import com.github.chuross.recyclerviewadapters.ViewItem;

public class BindingViewItem<B extends ViewDataBinding> extends BaseLocalAdapter<BindingViewHolder<B>> implements Cloneable {

    private int layoutResourceId;
    private ObservableField<Boolean> visibleChangeField;
    private Observable.OnPropertyChangedCallback visibleChangeCallback;
    private View.OnClickListener clickListener;

    public BindingViewItem(@NonNull Context context, @LayoutRes int layoutResourceId) {
        super(context);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getAdapterId() {
        return layoutResourceId;
    }

    @Override
    public BindingViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), layoutResourceId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<B> holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClick(v);
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (visibleChangeCallback != null) visibleChangeField.removeOnPropertyChangedCallback(visibleChangeCallback);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void bindVisible(final ObservableField<Boolean> visibleChangeField) {
        visibleChangeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setVisible(visibleChangeField.get());
            }
        };
        visibleChangeField.addOnPropertyChangedCallback(visibleChangeCallback);
        this.visibleChangeField = visibleChangeField;
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    public BindingViewItem<B> clone() {
        return new BindingViewItem<>(getContext(), layoutResourceId);
    }

}
