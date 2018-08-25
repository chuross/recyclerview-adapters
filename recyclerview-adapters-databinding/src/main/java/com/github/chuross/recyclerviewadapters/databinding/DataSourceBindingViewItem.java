package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class DataSourceBindingViewItem<T, BINDING extends ViewDataBinding> extends BindingViewItem<BINDING> {

    private ObservableField<T> source;
    private Observable.OnPropertyChangedCallback callback;

    public DataSourceBindingViewItem(@NonNull Context context, int layoutResourceId, ObservableField<T> source) {
        super(context, layoutResourceId);
        this.source = source;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notifyDataSetChanged();
            }
        };
        source.addOnPropertyChangedCallback(callback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (callback == null) return;
        source.removeOnPropertyChangedCallback(callback);
    }

    @Nullable
    public T getData() {
        return source != null ? source.get() : null;
    }
}
