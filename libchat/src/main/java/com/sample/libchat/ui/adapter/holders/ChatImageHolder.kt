package com.sample.libchat.ui.adapter.holders

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sample.libchat.R
import com.sample.libchat.ui.adapter.items.ChatBaseItem
import com.sample.libchat.ui.adapter.items.ChatImageItem
import kotlinx.android.synthetic.main.view_chat_item_image.view.*


class ChatImageHolder(view: View) : ChatBaseHolder(view) {

    companion object {
        const val TILES_COUNT_MAX = 3
    }

    private val mContent: FrameLayout by lazy {
        FrameLayout(mContext).apply {
            layoutParams.let {
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            }
        }
    }
    
    private val mTitleLayout: ConstraintLayout = LayoutInflater.from(view.context)
            .inflate(R.layout.view_chat_item_image, view as ViewGroup, false) as ConstraintLayout

    private val mTitleImage: AppCompatImageView
        get() = mTitleLayout.chatImageTitleImage

    private val mTitleText: AppCompatTextView
        get() = mTitleLayout.chatImageTitleText

    private val mImageSize: Int by lazy {
        mContext.resources.getDimensionPixelSize(R.dimen.chat_image_size)
    }

    private val mImageMargin: Int by lazy {
        mContext.resources.getDimensionPixelSize(R.dimen.chat_image_margin)
    }

    private lateinit var mImageItem: ChatImageItem
    private var mImageWidth: Int = mImageSize
    private var mImageHeight: Int = mImageSize

    init {
        addView(mContent)
    }
    
    override fun bind(item : ChatBaseItem) {
        mImageItem = item as ChatImageItem
        mImageWidth = mImageItem.previewWidth ?: mImageSize
        mImageHeight = mImageItem.previewHeight ?: mImageSize

        if (mImageItem.image != null) {
            mImageItem.width = null
            mImageItem.height = null
            mImageItem.setTransparentBackground()
            super.bind(item)
            setViewTile()
        } else {
            mImageItem.width = mImageWidth
            mImageItem.height = mImageHeight
            mImageItem.setWhiteBackground()
            super.bind(item)
            setViewProgress()
        }
    }

    private fun setViewProgress() {
        mContent.apply {
            removeAllViews()
            addView(getProgressView())
        }
    }

    private fun getProgressView(): ProgressBar {
        return ProgressBar(mContext).apply {
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.CENTER
            layoutParams = params
            isIndeterminate = true
            indeterminateTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorGreyMidTint))
        }
    }

    private fun setViewTile() {
        mContent.removeAllViews()
        val countTiles = if (mImageItem.count < TILES_COUNT_MAX) mImageItem.count else TILES_COUNT_MAX
        (0 until countTiles).forEach { index ->
            val margins = index * mImageMargin
            val isLast = index == mImageItem.count - 1

            // Set params for tile
            mImageItem.backgroundColor = android.R.color.white
            mImageItem.shadowColor = R.color.colorGreyMidTint
            mImageItem.backgroundBitmap = mImageItem.image
            mImageItem.isRippleEffect = isLast

            val roundImage = getRoundImageView(getRoundedDrawable(mImageItem), isLast, margins)
            mContent.addView(roundImage)

            if (isLast) {
                mContent.addView(getTitle(margins))
            }
        }
    }

    private fun getRoundImageView(drawable: Drawable, isFirst: Boolean, margins: Int): AppCompatImageView {
        return AppCompatImageView(mContext).apply {
            background = drawable
            layoutParams = FrameLayout.LayoutParams(mImageWidth, mImageHeight).apply {
                gravity = Gravity.END or Gravity.BOTTOM
                rightMargin = margins
                bottomMargin = margins
            }
        }
    }

    private fun getTitle(margins: Int): View {
        mTitleText.text = mImageItem.count.toString()
        mTitleText.setTextColor(ContextCompat.getColor(mContext, mImageItem.color))
        mTitleImage.setImageResource(mImageItem.imageTextIcon)
        mTitleImage.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, mImageItem.color))
        mTitleImage.visibility = View.VISIBLE

        return mTitleLayout.apply {
            val params = FrameLayout.LayoutParams(mImageWidth, mImageHeight)
            params.gravity =  Gravity.END or Gravity.BOTTOM
            params.rightMargin = margins
            params.bottomMargin = margins
            layoutParams = params
        }
    }

}