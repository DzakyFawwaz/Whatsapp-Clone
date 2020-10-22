package com.jaki.example.whatsappclone.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jaki.example.whatsappclone.MessageChatActivity
import com.jaki.example.whatsappclone.model.Chat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_chat_left.view.*
import kotlinx.android.synthetic.main.message_chat_right.view.*

class ChatItemLeftVH (itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(chat: Chat, imageUrl: String) {

        itemView.run {
            if (chat.message.isNotBlank()) {
                sent_image_left.visibility = View.GONE

                text_chat_left.text = chat.message
                text_chat_left.visibility = View.VISIBLE
            } else {
                if (chat.url.isNotBlank()) {
                    text_chat_left.visibility = View.GONE

                    Picasso.get().load(chat.url).fit().centerCrop().into(sent_image_left)
                    text_chat_left.visibility = View.VISIBLE
                }
            }
            text_seen_left.visibility =
                if (chat.isseen != MessageChatActivity.IS_SEEN_FALSE) View.VISIBLE else View.GONE
            if (imageUrl.isNotBlank()) Picasso.get().load(imageUrl).fit().centerCrop()
                .into(profile_chat_left)
        }
    }
}