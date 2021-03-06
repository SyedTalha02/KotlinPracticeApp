package com.kotlinsample.app.viewItems

import com.kotlinsample.app.R
import com.kotlinsample.app.models.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row_chat_log.view.*

/**
 * Created on 4/8/2019.
 */
class ChatFromItem(val message: String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row_chat_log
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_from_textview_chat_log.text = message
        val uri = user.profileImageUrl
    }
}