package com.jaki.example.whatsappclone.adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaki.example.whatsappclone.MessageChatActivity
import com.jaki.example.whatsappclone.R
import com.jaki.example.whatsappclone.model.User
import kotlin.coroutines.coroutineContext

class UserSearchItemAdapter : RecyclerView.Adapter<UserSearchItemVH>() {

    private val listUser = arrayListOf<User>()

    fun addUser(users: List<User>) {
        listUser.clear()
        listUser.addAll(users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchItemVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_search_item_layout, parent, false)
        return UserSearchItemVH(view)
    }

    override fun onBindViewHolder(holder: UserSearchItemVH, position: Int ) {
        val data = listUser[position]
        val context = holder.itemView.context

        holder.bindData(data)
        holder.itemView.setOnClickListener {
            val option = arrayOf<CharSequence>(
                "Send Mesage",
                "Visit Profile"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("What do you want")
            builder.setItems(option, DialogInterface.OnClickListener { dialog, which ->
                if (position == 0){
                    val intent = Intent(context, MessageChatActivity::class.java)
                    intent.putExtra("VISIT_ID", data.uid)
                    context.startActivity(intent)
                }
                if (position == 1){

                }
            })
            builder.show()

        }
    }

    override fun getItemCount(): Int = listUser.size

}
