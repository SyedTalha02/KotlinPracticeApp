package com.kotlinsample.app.viewItems

import com.kotlinsample.app.R
import com.kotlinsample.app.models.NewMessage
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.details_row_latest_messages.view.*

/**
 * Created on 4/8/2019.
 */
class LatestMessagesItem(val newMessage: NewMessage) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.details_row_latest_messages
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_messages.text = newMessage.message
        viewHolder.itemView.username_textview_latest_messages.text = newMessage.user.username
        Picasso.get().load(newMessage.user.profileImageUrl).into(viewHolder.itemView.avatar_imageview_latest_messages)
    }
}