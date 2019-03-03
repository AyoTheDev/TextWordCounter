package com.ayo.ctmtest.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.ayo.ctmtest.view.adapter.WordListAdapter

class WordViewHolder(itemView: View, private val listener: WordListAdapter.ItemClickListener?) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        listener?.onClick(layoutPosition)
    }
}