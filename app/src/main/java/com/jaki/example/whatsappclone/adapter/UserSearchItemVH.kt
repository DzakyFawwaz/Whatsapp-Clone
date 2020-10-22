package com.jaki.example.whatsappclone.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jaki.example.whatsappclone.R
import com.jaki.example.whatsappclone.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class UserSearchItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun  bindData(user: User) {
        with(itemView) {
            if (user.profile.isNotBlank()) Picasso.get().load(user.profile)
                .placeholder(R.drawable._bruh).into(profile_search)
            username_search.text = user.username

            val colorOffline = if (user.status == "online") ContextCompat.getDrawable(
                itemView.context,
                R.drawable.status_bgr
            ) else ContextCompat.getDrawable(itemView.context, R.drawable.status_bgr_offline)
            img_onffline.setImageDrawable(colorOffline)
        }
    }
}