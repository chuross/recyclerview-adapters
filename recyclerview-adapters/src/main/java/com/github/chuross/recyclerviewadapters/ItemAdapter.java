package com.github.chuross.recyclerviewadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.github.chuross.recyclerviewadapters.internal.EventExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.github.chuross.recyclerviewadapters.internal.RecyclerAdaptersUtils.checkNonNull;

public abstract class ItemAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseLocalAdapter<VH> implements RecyclerView.OnItemTouchListener {

    private final List<T> items = new ArrayList<>();
    private OnItemClickListener<T> clickListener;
    private OnItemDoubleClickListener<T> doubleClickListener;
    private OnItemLongPressedListener<T> longPressedListener;
    private GestureDetector gestureDetector;

    public ItemAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (longPressedListener == null) return;

                executeGestureEvent(recyclerView, e, new EventExecutor() {
                    @Override
                    public void execute(@NonNull View view, int position) {
                        longPressedListener.onItemLongPressed(recyclerView.getChildViewHolder(view), position, get(position));
                    }
                });
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (clickListener == null) return true;

                executeGestureEvent(recyclerView, e, new EventExecutor() {
                    @Override
                    public void execute(@NonNull View view, int position) {
                        clickListener.onItemClicked(recyclerView.getChildViewHolder(view), position, get(position));
                    }
                });
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (doubleClickListener == null) return true;

                executeGestureEvent(recyclerView, e, new EventExecutor() {
                    @Override
                    public void execute(@NonNull View view, int position) {
                        doubleClickListener.onItemDoubleClicked(recyclerView.getChildViewHolder(view), position, get(position));
                    }
                });
                return true;
            }
        });
        gestureDetector.setIsLongpressEnabled(true);
        recyclerView.addOnItemTouchListener(this);
    }

    private void executeGestureEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent ev, @Nullable EventExecutor executor) {
        if (executor == null) return;

        final View child = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

        if (child == null) return;

        int parentAdapterPosition = recyclerView.getChildAdapterPosition(child);
        if (!hasParentAdapter()) {
            executor.execute(child, parentAdapterPosition);
            return;
        }
        LocalAdapterItem localAdapterItem = getParentAdapter().getLocalAdapterItem(parentAdapterPosition);
        if (localAdapterItem == null || localAdapterItem.getLocalAdapter() != this) {
            return;
        }

        int localAdapterPosition = localAdapterItem.getLocalAdapterPosition();
        executor.execute(child, localAdapterPosition);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnItemTouchListener(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    public T get(int index) {
        return items.get(index);
    }

    @NonNull
    public List<T> getAll() {
        return items;
    }

    public void add(@NonNull T item) {
        checkNonNull(item);
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void add(int index, @NonNull T item) {
        checkNonNull(item);
        items.add(index, item);
        notifyItemInserted(index);
    }

    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public void addAll(Collection<T> items) {
        checkNonNull(items);
        this.items.addAll(items);
        notifyItemRangeInserted(this.items.size() - items.size(), items.size());
    }

    public void remove(@NonNull T item) {
        checkNonNull(item);
        int index = items.indexOf(item);
        if (index < 0) return;
        items.remove(item);
        notifyItemRemoved(index);
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemDoubleClickListener(@Nullable OnItemDoubleClickListener<T> doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

    public void setOnItemLongPressListener(@Nullable OnItemLongPressedListener<T> longPressListener) {
        this.longPressedListener = longPressListener;
    }
}
