package com.sample.libchat.ui.adapter.items

import android.view.Gravity
import androidx.annotation.ColorRes
import com.sample.libchat.R

abstract class ChatMessageItem : ChatBaseItem() {

    @ColorRes
    var headerTextColor: Int = R.color.colorDarkTint

    var headerTextGravity: Int = Gravity.LEFT or Gravity.CENTER_VERTICAL

    @ColorRes
    var contentTextColor: Int = R.color.colorDarkTint

    var contentTextGravity: Int = Gravity.LEFT or Gravity.CENTER_VERTICAL

    var message: String = ""
}

class ChatMessageUserItem : ChatMessageItem() {
    init {
        position = ChatItemPosition.RIGHT
        backgroundColor = R.color.colorChatBackground
        contentTextColor = android.R.color.white
        paddingLeft = DEFAULT_MARGIN
    }
}

class ChatMessageCompanionItem : ChatMessageItem() {
    init {
        position = ChatItemPosition.LEFT
        paddingRight = DEFAULT_MARGIN
    }
}