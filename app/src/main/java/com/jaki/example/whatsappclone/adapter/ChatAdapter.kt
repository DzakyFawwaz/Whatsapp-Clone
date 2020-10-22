package com.jaki.example.whatsappclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jaki.example.whatsappclone.R
import com.jaki.example.whatsappclone.model.Chat
import com.jaki.example.whatsappclone.model.ChatList
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_message_chat.view.*
import kotlinx.android.synthetic.main.message_chat_left.view.*
import kotlinx.android.synthetic.main.message_chat_right.view.*

class ChatAdapter(val receiverID: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val arrayChat = arrayListOf<Chat>()
    private var imageUrl = ""

    fun addChats(chats: List<Chat>, receiverImage: String) {
        arrayChat.clear()
        arrayChat.addAll(chats)
        imageUrl = receiverImage
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            val view = inflater.inflate(R.layout.message_chat_right, parent, false)
            ChatItemRightVH(view)
        } else {
            val view = inflater.inflate(R.layout.message_chat_left, parent, false)
            ChatItemLeftVH(view)
        }
    }

    override fun getItemCount(): Int = arrayChat.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = arrayChat[position]
        val viewType = getItemViewType(position)
        if (viewType == 1) (holder as ChatItemRightVH).bind(chat) else (holder as ChatItemLeftVH).bind(
            chat,
            imageUrl
        )
    }

    override fun getItemViewType(position: Int): Int {
        val chat = arrayChat[position]
        return if (chat.sender == receiverID) 0 else 1
    }

//    (
//    mContext: Context,
//    mChatList: List<Chat>,
//    imageUrl: String
//) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {
//
//    private  val mContext: Context
//    private  val mChatList: List<Chat>
//    private  val imageUrl: String
//    var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//    init {
//        this.mChatList = mChatList
//        this.mContext = mContext
//        this.imageUrl = imageUrl
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val chat = mChatList[position]
//
//        Picasso
//            .get()
//            .load(imageUrl)
//            .into(holder.right_profile_pict)
//
//        // image message
//        if (chat.message.equals("sent you an image") && chat.url.equals(""))
//        // image right side
//            if (chat.sender.equals(firebaseUser.uid)) {
//                holder.right_text.visibility = View.GONE
//                holder.right_profile_pict.visibility = View.VISIBLE
//                Picasso
//                    .get()
//                    .load(chat.url)
//                    .into(holder.right_profile_pict)
//
//                // image left side
//            } else if (chat.sender.equals(firebaseUser.uid)) {
//                holder.right_text.visibility = View.GONE
//                holder.left_profile_pict.visibility = View.VISIBLE
//                Picasso
//                    .get()
//                    .load(chat.url)
//                    .into(holder.left_profile_pict)
//
//            }
//            // text message
//            else {
//                holder.right_text.text = chat.message
//            }
//
//        if (position == mChatList.size - 1) {
//            if (chat.isseen) {
//                holder.text_seen.text = "seen"
//                if (chat.message.equals("sent you an image") && chat.url.equals("")) {
//                    val lp: RelativeLayout.LayoutParams =
//                        holder.text_seen.layoutParams as RelativeLayout.LayoutParams
//                    lp.setMargins(0, 245, 10, 0)
//                    holder.text_seen.layoutParams = lp
//                } else {
//                    holder.text_seen.text = "sent"
//                    if (chat.message.equals("sent you an image") && chat.url.equals("")) {
//                        val lp: RelativeLayout.LayoutParams =
//                            holder.text_seen.layoutParams as RelativeLayout.LayoutParams
//                        lp.setMargins(0, 245, 10, 0)
//                        holder.text_seen.layoutParams = lp
//                    }
//                }
//            } else {
//                holder.text_seen.visibility = View.GONE
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
//        return if (position == 1){
//
//            val view = LayoutInflater.from(mContext).inflate(R.layout.message_chat_right, parent, false)
//            ViewHolder(view)
//        }
//        else{
//            val view = LayoutInflater.from(mContext).inflate(R.layout.message_chat_left, parent, false)
//            ViewHolder(view)
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return mChatList.size
//    }
//
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        lateinit var right_text: TextView
//        lateinit var left_text: TextView
//        lateinit var left_profile_pict: CircleImageView
//        lateinit var text_seen: TextView
//        lateinit var right_profile_pict: CircleImageView
//
//        init {
//            right_text = itemView.text_chat_right
//            left_text = itemView.text_chat_left
//            left_profile_pict = itemView.profile_chat_left
//            text_seen = itemView.text_seen
//            right_profile_pict = itemView.profile_chat_right
//
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (mChatList[position].sender.equals(firebaseUser.uid)){
//            1
//        }else{
//            0
//        }
//    }
//
//
//
}