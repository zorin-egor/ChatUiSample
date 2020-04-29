package com.sample.libchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.libchat.R
import com.sample.libchat.ui.adapter.base.BaseAdapter
import com.sample.libchat.ui.adapter.holders.ChatBaseHolder
import com.sample.libchat.ui.adapter.holders.ChatImageHolder
import com.sample.libchat.ui.adapter.holders.ChatMessageHolder
import com.sample.libchat.ui.adapter.items.ChatBaseItem
import com.sample.libchat.ui.adapter.items.ChatImageItem
import com.sample.libchat.ui.adapter.items.ChatMessageItem
import java.util.*

class ChatAdapter : BaseAdapter<ChatBaseItem>() {

    companion object {
        private const val TYPE_MESSAGE = 0
        private const val TYPE_IMAGE = 1
    }

    init {
        list = LinkedList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBaseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat, parent, false)
        return when(viewType) {
            TYPE_MESSAGE -> ChatMessageHolder(view).apply { onClickListener = onItemClickListener }
            TYPE_IMAGE -> ChatImageHolder(view).apply { onClickListener = onItemClickListener }
            else -> ChatMessageHolder(view).apply { onClickListener = onItemClickListener }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ChatBaseHolder)?.let { holder ->
            getItem(position)?.let { item ->
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatMessageItem -> TYPE_MESSAGE
            is ChatImageItem -> TYPE_IMAGE
            else -> TYPE_MESSAGE
        }
    }

}