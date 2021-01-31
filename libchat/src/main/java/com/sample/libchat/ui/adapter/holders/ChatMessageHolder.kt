package com.sample.libchat.ui.adapter.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sample.libchat.R
import com.sample.libchat.extensions.toSpanned
import com.sample.libchat.ui.adapter.items.ChatBaseItem
import com.sample.libchat.ui.adapter.items.ChatMessageItem
import kotlinx.android.synthetic.main.view_chat_item_message.view.*

class ChatMessageHolder(view: View) : ChatBaseHolder(view) {

    private val mContent: ConstraintLayout = LayoutInflater.from(view.context)
            .inflate(R.layout.view_chat_item_message, view as ViewGroup, false) as ConstraintLayout

    private val mHeaderText: AppCompatTextView
        get() = mContent.chatMessageHeaderText

    private val mContentText: AppCompatTextView
        get() = mContent.chatMessageContentText

    private lateinit var mMessageItem: ChatMessageItem

    init {
        setContainerItemPadding()
        addView(mContent)
    }

    override fun bind(item : ChatBaseItem) {
        super.bind(item)
        mMessageItem = item as ChatMessageItem
        setTopText()
        setBottomText()
    }

    private fun setTopText() {
        mHeaderText.visibility = if (false) {
            mHeaderText.text = mMessageItem.message
            mHeaderText.gravity = mMessageItem.headerTextGravity
            mHeaderText.setTextColor(ContextCompat.getColor(mContext, mMessageItem.headerTextColor))
            View.VISIBLE
        } else {
            mHeaderText.text = ""
            View.GONE
        }
    }

    private fun setBottomText() {
        mContentText.text = mMessageItem.message
            .replace("\n", "<br>")
            .replace(" ", "&nbsp;")
            .toSpanned()
        mContentText.gravity = mMessageItem.contentTextGravity
        mContentText.setTextColor(ContextCompat.getColor(mContext, mMessageItem.contentTextColor))
        mContentText.visibility = View.VISIBLE
    }
}