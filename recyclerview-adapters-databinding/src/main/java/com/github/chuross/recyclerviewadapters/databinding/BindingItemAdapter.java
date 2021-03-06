package com.github.chuross.recyclerviewadapters.databinding;


import android.content.Context;

import com.github.chuross.recyclerviewadapters.BaseItemAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BindingItemAdapter<I, VH extends RecyclerView.ViewHolder> extends BaseItemAdapter<I, VH> {

    private ObservableList<I> observableList;
    private ObservableList.OnListChangedCallback<ObservableList<I>> callback;
    private ObservableField<Boolean> visibleChangeField;
    private Observable.OnPropertyChangedCallback visibleChangeCallback;

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
        releaseVisibleChangeBinding();
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

    private void releaseVisibleChangeBinding() {
        if (visibleChangeField == null || visibleChangeCallback == null) {
            return;
        }

        visibleChangeField.removeOnPropertyChangedCallback(visibleChangeCallback);
        visibleChangeField = null;
        visibleChangeCallback = null;
    }
}
