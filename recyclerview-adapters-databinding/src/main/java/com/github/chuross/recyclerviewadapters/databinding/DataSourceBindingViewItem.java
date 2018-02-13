package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

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
