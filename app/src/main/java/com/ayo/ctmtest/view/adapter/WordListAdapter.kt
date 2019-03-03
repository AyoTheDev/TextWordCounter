package com.ayo.ctmtest.view.adapter

import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ayo.ctmtest.R
import com.ayo.ctmtest.data.Word
import com.ayo.ctmtest.view.adapter.viewholder.WordViewHolder
import kotlinx.android.synthetic.main.item_word.view.*


class WordListAdapter(private val listener: ItemClickListener? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = listOf<Word>()

    fun getItem(position: Int): Word = itemList[position]

    fun update(list: List<Word>) {
        itemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view, listener)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = itemList[position]
        holder.itemView.apply {
            val stringBuilder = StringBuilder()
            stringBuilder.append("The word ${word.key}\n")
            stringBuilder.append("appears ${word.count} ")
            stringBuilder.append(if (word.count == 1) "time\nthis " else "times\nthis ")
            stringBuilder.append(if (word.isPrime) "is " else "is not ")
            stringBuilder.append("a prime number.")
            val sb = SpannableString(stringBuilder.toString())
            sb.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 9, 9 + word.key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            ui_count.text = sb
        }
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }
}