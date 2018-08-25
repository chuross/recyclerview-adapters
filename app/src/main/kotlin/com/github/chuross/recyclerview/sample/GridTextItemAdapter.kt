package com.github.chuross.recyclerview.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.github.chuross.recyclerview.sample.databinding.ViewGridTextBinding
import com.github.chuross.recyclerviewadapters.databinding.BindingViewHolder
import com.github.chuross.rx.RxItemAdapter
import io.reactivex.Flowable

class GridTextItemAdapter(
        context: Context,
        source: Flowable<List<String>>,
        @ColorInt val textColor: Int? = null
) : RxItemAdapter<String, BindingViewHolder<ViewGridTextBinding>>(context, source) {

    override fun getAdapterId(): Int = R.layout.view_grid_text

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewGridTextBinding> {
        val inflater = LayoutInflater.from(context)
        return BindingViewHolder(ViewGridTextBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewGridTextBinding>, position: Int) {
        val text = get(position)
        holder.binding?.also {
            it.text = text
            it.textColor = textColor
        }?.executePendingBindings()
    }

}