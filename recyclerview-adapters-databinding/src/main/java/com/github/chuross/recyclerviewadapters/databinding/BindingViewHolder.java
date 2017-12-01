package com.github.chuross.recyclerviewadapters.databinding;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BindingViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private B binding;

    public BindingViewHolder(B binding) {
        super(binding.getRoot());
    }

    public B getBinding() {
        return binding;
    }
}
