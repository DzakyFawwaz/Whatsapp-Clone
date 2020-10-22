package com.jaki.example.whatsappclone.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jaki.example.whatsappclone.MessageChatActivity.Companion.IS_SEEN_FALSE
import com.jaki.example.whatsappclone.model.Chat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_chat_right.view.*

class ChatItemRightVH (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(chat: Chat) {
        itemView.run {
            if (chat.message.isNotBlank()) {
                sent_image_right.visibility = View.GONE

                text_chat_right.text = chat.message
                text_chat_right.visibility = View.VISIBLE
            } else {
                if (chat.url.isNotBlank()) {
                    text_chat_right.visibility = View.GONE

                    Picasso.get().load(chat.url).fit().centerCrop().into(sent_image_right)
                    sent_image_right.visibility = View.VISIBLE
                }
            }
            text_seen.visibility = if (chat.isseen != IS_SEEN_FALSE) View.VISIBLE else View.GONE
        }
    }
}