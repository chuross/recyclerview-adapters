package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.chuross.recyclerviewadapters.ViewItem;

public class BindingViewItem extends ViewItem {

    private ObservableField<Boolean> visibleChangeField;
    private Observable.OnPropertyChangedCallback visibleChangeCallback;

    public BindingViewItem(@NonNull Context context, @NonNull int layoutResourceId) {
        super(context, layoutResourceId);
    }

    public BindingViewItem(@NonNull Context context, @NonNull int layoutResourceId, @Nullable View.OnClickListener clickListener) {
        super(context, layoutResourceId, clickListener);
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

}
