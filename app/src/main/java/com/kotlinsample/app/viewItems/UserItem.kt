package com.kotlinsample.app.viewItems

import com.kotlinsample.app.R
import com.kotlinsample.app.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

/**
 * Created on 4/5/2019.
 */
class UserItem(val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_edittext_new_message.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.user_avatar_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}