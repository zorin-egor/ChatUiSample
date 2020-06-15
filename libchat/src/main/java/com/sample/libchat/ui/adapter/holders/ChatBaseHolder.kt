package com.sample.libchat.ui.adapter.holders

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.Size
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sample.libchat.R
import com.sample.libchat.extensions.getChildren
import com.sample.libchat.ui.adapter.base.OnItemClickListener
import com.sample.libchat.ui.adapter.base.OnItemLongClickListener
import com.sample.libchat.ui.adapter.items.ChatBaseItem
import com.sample.libchat.ui.adapter.items.ChatItemPosition
import com.sample.libchat.ui.views.drawable.BitmapRoundRectDrawable
import kotlinx.android.synthetic.main.list_item_chat.view.*


open class ChatBaseHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    protected inner class OnGlobalLayout : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            onLayoutReady()
        }
    }

    var onClickListener: OnItemClickListener<ChatBaseItem>? = null

    var onLongClickListener: OnItemLongClickListener<ChatBaseItem>? = null

    protected val mContainerLayout: ConstraintLayout
        get() = view.chatItemLayout

    protected val mContainerItem: FrameLayout
        get() = view.chatItemContainer

    protected val mContext: Context
        get() = mContainerLayout.context

    protected val mLargePadding: Int by lazy {
        mContext.resources.getDimensionPixelOffset(R.dimen.default_large)
    }

    protected val mMediumPadding: Int by lazy {
        mContext.resources.getDimensionPixelOffset(R.dimen.default_medium)
    }

    protected val mShadowSize: Int by lazy {
        mContext.resources.getDimensionPixelOffset(R.dimen.default_small)
    }

    protected val mLeftRightPadding: Int by lazy {
        mLargePadding + mShadowSize
    }

    protected val mTopBottomPadding: Int by lazy {
        mMediumPadding + mShadowSize
    }

    protected lateinit var mChatItem: ChatBaseItem
    protected lateinit var mViewItem: View
    protected var mContainerLayoutSize: Size? = null

    init {
        setIsRecyclable(true)
    }

    protected fun addView(content: View) {
        mViewItem = content.apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }

            isClickable = false
            isFocusable = false
            isFocusableInTouchMode = false

            getChildren { view ->
                view.isClickable = false
                view.isFocusable = false
                view.isFocusableInTouchMode = false
            }
        }

        mContainerItem.apply {
            addView(mViewItem)
            isClickable = true
            isFocusable = true
            isFocusableInTouchMode = true

            setOnClickListener {
                onClickListener?.onItemClick(mChatItem, mContainerItem, layoutPosition)
            }

            setOnLongClickListener {
                onLongClickListener?.onItemLongClick(mChatItem, mContainerItem, layoutPosition) ?: false
            }
        }
    }

    open fun bind(item: ChatBaseItem) {
        mChatItem = item
        setContainerItemLayoutParams()
        setContainerItemBackground()
        setViewItemLayoutParams()
        setGlobalLayoutListener()
    }

    protected fun setGlobalLayoutListener() {
        view.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayout())
    }

    protected open fun onLayoutReady() {
        mContainerLayoutSize = Size(view.width, view.height)
    }

    protected fun getRoundedDrawable(item: ChatBaseItem): Drawable {
        when(item.position) {
            ChatItemPosition.LEFT -> { item.cornerLeftBottom = 0.0f }
            ChatItemPosition.RIGHT -> { item.cornerRightBottom = 0.0f }
        }

        var drawable: Drawable = BitmapRoundRectDrawable().apply {
            color = ContextCompat.getColor(mContext, item.backgroundColor)
            bitmap = item.backgroundBitmap
            shadowColor = ContextCompat.getColor(mContext, item.shadowColor)
            shadowSize = item.shadowSize
            isShadowVisible = item.isShadowVisible
            strokeColor = item.strokeColor
            strokeSize = item.strokeSize
            isStrokeVisible = item.isStrokeVisible

            cornerRadii = floatArrayOf(item.cornerLeftTop, item.cornerLeftTop,
                item.cornerRightTop, item.cornerRightTop,
                item.cornerRightBottom, item.cornerRightBottom,
                item.cornerLeftBottom, item.cornerLeftBottom)
        }

        if (item.isRippleEffect) {
            val rippleColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(mContext, item.rippleColor)))
            drawable = RippleDrawable(rippleColor, drawable, null)
        }

        return drawable
    }

    protected fun setContainerItemBackground() {
        mContainerItem.background = getRoundedDrawable(mChatItem)
//        mContainerItem.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    protected fun setContainerItemLayoutParams() {
        ConstraintSet().apply {
            clone(mContainerLayout)

            // Set vertical ic_position
            setHorizontalBias(mContainerItem.id, when(mChatItem.position) {
                ChatItemPosition.RIGHT -> 1.0f
                ChatItemPosition.LEFT -> 0.0f
                ChatItemPosition.CENTER -> 0.5f
            })

            // Set view item sizes
            constrainWidth(mContainerItem.id, ConstraintSet.WRAP_CONTENT)
            constrainHeight(mContainerItem.id, ConstraintSet.WRAP_CONTENT)
            applyTo(mContainerLayout)
        }

        var leftPadding = mChatItem.paddingLeft?.let { mContext.resources.getDimensionPixelSize(it) } ?: 0
        var rightPadding = mChatItem.paddingRight?.let { mContext.resources.getDimensionPixelSize(it) } ?: 0
        mContainerLayout.setPadding(leftPadding, 0, rightPadding, 0)
    }

    /*
    * Wrong item size, when change dynamically. Use fix xml padding instead
    * */
    protected fun setContainerItemPadding() {
        mContainerItem.setPadding(mLeftRightPadding, mTopBottomPadding, mLeftRightPadding, mTopBottomPadding)
    }

    protected open fun setViewItemLayoutParams() {
        when {
            mChatItem.width != null || mChatItem.height != null -> {
                var width = mChatItem.width ?: mViewItem.layoutParams.width
                var height = mChatItem.height ?: mViewItem.layoutParams.height
                mViewItem.layoutParams = FrameLayout.LayoutParams(width, height).apply {
                    gravity = Gravity.CENTER
                }
            }

            else -> {
                mViewItem.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER
                }
            }
        }
    }

}