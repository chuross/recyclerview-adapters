package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chuross.recyclerviewadapters.BaseLocalAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


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
        releaseVisibleChangeBinding();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void bindVisible(final ObservableField<Boolean> visibleChangeField) {
        if (visibleChangeField.get() != null) setVisible(visibleChangeField.get());

        releaseVisibleChangeBinding();

        visibleChangeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setVisible(visibleChangeField.get(), false);
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

    private void releaseVisibleChangeBinding() {
        if (visibleChangeField == null || visibleChangeCallback == null) {
            return;
        }

        visibleChangeField.removeOnPropertyChangedCallback(visibleChangeCallback);
        visibleChangeField = null;
        visibleChangeCallback = null;
    }

}
