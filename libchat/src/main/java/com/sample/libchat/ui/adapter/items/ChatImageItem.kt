package com.sample.libchat.ui.adapter.items

import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.sample.libchat.R

abstract class ChatImageItem : ChatBaseItem {

    var count = 1

    var previewWidth: Int? = null

    var previewHeight: Int? = null

    var image: Bitmap? = null

    @DrawableRes
    var imageTextIcon: Int = R.drawable.ic_pic_black

    @ColorRes
    var color: Int = R.color.colorWhite

    constructor()

    constructor(width: Int, height: Int) {
        previewWidth = width
        previewHeight = height
    }

    constructor(item: ChatImageItem) : super(item) {
        previewWidth = item.previewWidth
        previewHeight = item.previewHeight
        imageTextIcon = item.imageTextIcon
        color = item.color
        image = item.image
    }

    init {
        isStrokeVisible = false
        setTransparentBackground()
    }

    fun setTransparentBackground() {
        backgroundColor = android.R.color.transparent
        shadowColor = android.R.color.transparent
    }

    fun setWhiteBackground() {
        backgroundColor = android.R.color.white
        shadowColor = R.color.colorGreyMidTint
    }
}

class ChatImageUserItem() : ChatImageItem() {
    init {
        position = ChatItemPosition.RIGHT
    }
}

class ChatImageCompanionItem() : ChatImageItem() {
    init {
        position = ChatItemPosition.LEFT
    }
}