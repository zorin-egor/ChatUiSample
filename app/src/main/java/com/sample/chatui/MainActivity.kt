package com.sample.chatui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.sample.libchat.ui.adapter.base.OnItemClickListener
import com.sample.libchat.ui.adapter.base.OnItemLongClickListener
import com.sample.libchat.ui.adapter.items.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener,
    OnItemClickListener<ChatBaseItem>, OnItemLongClickListener<ChatBaseItem> {

    private val mMessages by lazy {
        listOf(
            ChatMessageUserItem().apply {
                message = getString(R.string.chat_message_hello)
            },

            ChatMessageUserItem().apply {
                message = getString(R.string.chat_message_info)
            },

            ChatMessageCompanionItem().apply {
                message = getString(R.string.chat_message_companion)
            },

            ChatImageUserItem().apply {
                image = BitmapFactory.decodeResource(resources, R.drawable.image_sample_1)
                count = 1
            },

            ChatImageCompanionItem().apply {
                image = BitmapFactory.decodeResource(resources, R.drawable.image_sample_2)
                count = 2
            },

            ChatImageCompanionItem()
        )
    }

    private val mMessageHandler = Handler(Looper.getMainLooper())
    private var mMessageCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(savedInstanceState)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.chatSendButton -> {
                chatView.addMessage(if (Random.nextBoolean()) {
                    ChatMessageUserItem().apply {
                        message = chatView.messageText
                    }
                } else {
                    ChatMessageCompanionItem().apply {
                        message = chatView.messageText
                    }
                })
            }

            R.id.chatCameraButton -> {
                chatView.addMessage(when (Random.nextInt(4)) {
                    0 -> {
                        ChatImageUserItem().apply {
                            image = BitmapFactory.decodeResource(resources, R.drawable.image_sample_1)
                            count = Random.nextInt(4)
                        }
                    }

                    1 -> {
                        ChatImageCompanionItem().apply {
                            image = BitmapFactory.decodeResource(resources, R.drawable.image_sample_2)
                            count = Random.nextInt(4)
                        }
                    }

                    2 -> {
                        ChatImageUserItem()
                    }

                    else -> {
                        ChatImageCompanionItem()
                    }
                })
            }
        }
    }

    override fun onItemClick(item: ChatBaseItem, view: View, position: Int) {
        Snackbar.make(findViewById(android.R.id.content), "Position clicked: $position", Snackbar.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(item: ChatBaseItem, view: View, position: Int): Boolean {
        Snackbar.make(findViewById(android.R.id.content), "Position deleted: $position", Snackbar.LENGTH_SHORT).show()
        chatView.deleteMessage(item)
        return true
    }

    private fun init(savedInstanceState: Bundle?) {
        chatView.apply {
            onSendClick = this@MainActivity
            onCameraClick = this@MainActivity
            onItemClick = this@MainActivity
            onItemLongClick = this@MainActivity
        }

        mMessages.forEach {
            addActionDelayed {
                chatView.addMessage(it)
            }
        }
    }

    private fun addActionDelayed(action: () -> Unit) {
        mMessageHandler.postDelayed({
            action.invoke()
            --mMessageCount
        }, ++mMessageCount * 300L)
    }

}
