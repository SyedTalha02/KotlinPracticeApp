package com.kotlinsample.app.activities

import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.kotlinsample.app.R
import com.kotlinsample.app.listeners.DataServiceListeners
import com.kotlinsample.app.models.User
import com.kotlinsample.app.viewItems.UserItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : BaseActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = getString(R.string.title_select_user)
        fetchUsers()

        createOnClickListener()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun createOnClickListener() {
        adapter.setOnItemClickListener { item, view ->

            val user = item as UserItem

            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, user.user)
            navigateToActivityWithIntent(intent)
            finish()

        }
    }

    private fun fetchUsers() {

        serviceManager.fetchUsers(object : DataServiceListeners {
            override fun onSuccess() {

            }

            override fun onFailure() {

            }

            override fun onSuccess(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != serviceManager.getCurrentUser()?.uid) {
                        adapter.add(UserItem(user))
                    }

                    newmessage_recyclerview.adapter = adapter
                }
            }

            override fun onFailure(error: DatabaseError) {
            }
        })
    }
}

