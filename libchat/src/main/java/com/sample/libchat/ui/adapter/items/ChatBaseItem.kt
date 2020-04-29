package com.sample.libchat.ui.adapter.items

import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import com.sample.libchat.R


enum class ChatItemPosition {
    LEFT, RIGHT, CENTER
}

abstract class ChatBaseItem {

    companion object {
        const val DEFAULT_CORNER_RADIUS = 20.0f
        const val DEFAULT_ELEVATION = 5.0f
        const val DEFAULT_SHADOW_SIZE = 15.0f
        const val DEFAULT_STROKE_SIZE = 1.0f

        @DimenRes
        val DEFAULT_MARGIN = R.dimen.default_xxxlarge
    }
    
    constructor()
    
    constructor(item : ChatBaseItem) {
        backgroundColor = item.backgroundColor
        backgroundBitmap = item.backgroundBitmap
        shadowColor = item.shadowColor
        shadowSize = item.shadowSize
        strokeColor = item.strokeColor
        strokeSize = item.strokeSize
        rippleColor = item.rippleColor
        width = item.width
        height = item.height
        cornerLeftTop = item.cornerLeftTop
        cornerLeftBottom = item.cornerLeftBottom
        cornerRightTop = item.cornerRightTop
        cornerRightBottom = item.cornerRightBottom
        elevation = item.elevation
        isRippleEffect = item.isRippleEffect
        isShadowVisible = item.isShadowVisible
        isStrokeVisible = item.isStrokeVisible
    }


    @ColorRes
    var backgroundColor: Int = android.R.color.white

    var backgroundBitmap: Bitmap? = null

    @ColorRes
    var shadowColor: Int = R.color.colorGreyLowTint

    var shadowSize: Float = DEFAULT_SHADOW_SIZE

    @ColorRes
    var strokeColor: Int = R.color.colorGreyLowTint

    var strokeSize: Float = DEFAULT_STROKE_SIZE

    @ColorRes
    var rippleColor: Int = R.color.colorGreyLowTint

    var width: Int? = null

    var height: Int? = null

    var position: ChatItemPosition = ChatItemPosition.CENTER

    var cornerLeftTop: Float = DEFAULT_CORNER_RADIUS

    var cornerLeftBottom: Float = DEFAULT_CORNER_RADIUS

    var cornerRightTop: Float = DEFAULT_CORNER_RADIUS

    var cornerRightBottom: Float = DEFAULT_CORNER_RADIUS

    var elevation: Float = DEFAULT_ELEVATION

    @DimenRes
    var paddingLeft: Int? = null

    @DimenRes
    var paddingRight: Int? = null

    var isRippleEffect: Boolean = true

    var isShadowVisible: Boolean = true

    var isStrokeVisible: Boolean = false
    
}