package com.sample.libchat.ui.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*


@FunctionalInterface
interface OnItemLongClickListener<T> {
    fun onItemLongClick(item: T, view: View, position: Int): Boolean
}

@FunctionalInterface
interface OnItemClickListener<T> {
    fun onItemClick(item: T, view: View, position: Int)
}

abstract class BaseAdapter<D> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var list: MutableList<D>

    override fun getItemCount(): Int = list.size

    fun addItems(items: List<D>) {
        list.addAll(items)
        notifyItemRangeInserted(list.size, items.size)
    }

    fun addItemsAtTop(items: List<D>) {
        list.addAll(0, items)
        notifyItemRangeInserted(0, items.size)
    }

    fun setItems(items: List<D>) {
        list = LinkedList(items)
        notifyDataSetChanged()
    }

    fun addItem(item: D) {
        list.add(item)
        notifyItemInserted(list.size)
    }

    fun addItemAtTop(item: D) {
        list.add(0, item)
        notifyItemInserted(0)
    }

    fun getItem(index: Int): D? {
        return list[index]
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun remove(item: D) {
        val index = list.indexOf(item)
        if (index >= 0) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun update(item: D) {
        val index = list.indexOf(item)
        if (index >= 0) {
            list[index] = item
            notifyItemChanged(index)
        }
    }
}