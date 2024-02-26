package com.sample.libchat.ui.views.chat

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.libchat.R
import com.sample.libchat.ui.adapter.ChatAdapter
import com.sample.libchat.ui.adapter.base.OnItemClickListener
import com.sample.libchat.ui.adapter.base.OnItemLongClickListener
import com.sample.libchat.ui.adapter.items.ChatBaseItem

open class ChatView : ConstraintLayout, TextView.OnEditorActionListener, View.OnClickListener,
    OnItemClickListener<ChatBaseItem>, OnItemLongClickListener<ChatBaseItem> {

    companion object {
        val TAG = ChatView::class.java.simpleName
    }

    lateinit var messageEditLayout: ConstraintLayout

    lateinit var messageListView: RecyclerView

    val sendButton: AppCompatImageButton
        get() = messageEditLayout.findViewById(R.id.chatSendButton)

    val cameraButton: AppCompatImageButton
        get() = messageEditLayout.findViewById(R.id.chatCameraButton)

    val messageEdit: AppCompatEditText
        get() = messageEditLayout.findViewById(R.id.chatEditText)

    val messageText: String
        get() = messageEdit.text?.toString() ?: ""

    var onSendClick: OnClickListener? = null

    var onCameraClick: OnClickListener? = null

    var onItemClick: OnItemClickListener<ChatBaseItem>? = null

    var onItemLongClick: OnItemLongClickListener<ChatBaseItem>? = null

    protected lateinit var mLayoutManager: LinearLayoutManager

    protected lateinit var mLayoutAnimation: LayoutAnimationController

    protected lateinit var mAdapter: ChatAdapter

    protected val mMediumMargin: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.default_medium)

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            sendButton.id -> {
                onSendClick?.onClick(view)
            }

            cameraButton.id -> {
                onCameraClick?.onClick(view)
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
            sendButton.performClick()
        }
        return true
    }

    override fun onItemClick(item: ChatBaseItem, view: View, position: Int) {
        onItemClick?.onItemClick(item, view, position)
    }

    override fun onItemLongClick(item: ChatBaseItem, view: View, position: Int): Boolean {
        return onItemLongClick?.onItemLongClick(item, view, position) ?: false
    }

    protected fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null) {
        initMessageEdit(context, attrs, defStyleAttr)
        initMessageList(context, attrs, defStyleAttr)
    }

    protected fun initMessageEdit(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null) {
        messageEditLayout = (LayoutInflater.from(context).inflate(R.layout.view_chat_edit, null) as ConstraintLayout).apply {
            this@ChatView.addView(this)
        }

        ConstraintSet().apply {
            clone(this@ChatView)

            connect(messageEditLayout.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, mMediumMargin)
            connect(messageEditLayout.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, mMediumMargin)
            connect(messageEditLayout.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, mMediumMargin)

            constrainWidth(messageEditLayout.id, ConstraintSet.MATCH_CONSTRAINT)
            constrainHeight(messageEditLayout.id, ConstraintSet.WRAP_CONTENT)

            applyTo(this@ChatView)
        }

        sendButton.setOnClickListener(this)
        cameraButton.setOnClickListener(this)
    }

    protected fun initMessageList(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null) {
        messageListView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true).also {
                mLayoutManager = it
            }

            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_layout_chat).also {
                mLayoutAnimation = it
            }

            adapter = ChatAdapter().apply {
                mAdapter = this
                onItemClickListener = this@ChatView
                onItemLongClickListener = this@ChatView
            }

            id = View.generateViewId()
            this@ChatView.addView(this)
        }

        ConstraintSet().apply {
            clone(this@ChatView)

            connect(messageListView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(messageListView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(messageListView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(messageListView.id, ConstraintSet.BOTTOM, messageEditLayout.id, ConstraintSet.TOP)

            constrainWidth(messageListView.id, ConstraintSet.MATCH_CONSTRAINT)
            constrainHeight(messageListView.id, ConstraintSet.MATCH_CONSTRAINT)

            applyTo(this@ChatView)
        }
    }

    protected fun scrollTo() {
        messageListView.scrollToPosition(0)
    }

    fun addMessage(item: ChatBaseItem) {
        mAdapter.addItemAtTop(item)
        clearEdit()
        scrollTo()
    }

    fun deleteMessage(item: ChatBaseItem) {
        mAdapter.remove(item)
    }

    fun clearEdit() {
        messageEdit.text?.clear()
    }

    fun clear() {
        mAdapter.clear()
    }

}