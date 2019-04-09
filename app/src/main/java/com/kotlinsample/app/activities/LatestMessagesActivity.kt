package com.kotlinsample.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.kotlinsample.app.R
import com.kotlinsample.app.listeners.DataServiceListeners
import com.kotlinsample.app.models.NewMessage
import com.kotlinsample.app.models.User
import com.kotlinsample.app.viewItems.LatestMessagesItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : BaseActivity() {


    private lateinit var adapter: GroupAdapter<ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        verifyUserLoggedIn()
    }

    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            navigateToActivityClearTask(RegisterActivity())
        } else {
            serviceManager.fetchCurrentUserDetails()
            createAdapter()
            listenLatestMessages()
        }
    }

    val latestMessagesMap = HashMap<String, NewMessage>()

    private fun listenLatestMessages() {
        serviceManager.listenLatestMessages(object : DataServiceListeners {
            override fun onSuccess(snapshot: DataSnapshot) {
                getChatBuddyImage(snapshot)
            }

            override fun onSuccess() {
            }

            override fun onFailure(error: DatabaseError) {
            }

            override fun onFailure() {
            }
        })
    }

    private fun getChatBuddyImage(snapshot: DataSnapshot) {
        val newMessage = snapshot.getValue(NewMessage::class.java) ?: return

        val chatBuddyId: String
        if(serviceManager.getCurrentUser() != null ) {
            if (newMessage.fromId == serviceManager.getCurrentUser()?.uid) {
                chatBuddyId = newMessage.toId
            } else {
                chatBuddyId = newMessage.fromId
            }

            serviceManager.getUserFromId(chatBuddyId, object : DataServiceListeners {
                override fun onSuccess(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    newMessage.user = user!!
                    latestMessagesMap[snapshot.key!!] = newMessage
                    refreshRecyclerview()
                }

                override fun onSuccess() {
                }

                override fun onFailure(error: DatabaseError) {
                }

                override fun onFailure() {
                }
            })
        }
    }

    private fun refreshRecyclerview() {
        adapter.clear()
        for (newMessage in latestMessagesMap.values) {
            adapter.add(LatestMessagesItem(newMessage))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                navigateToActivity(NewMessageActivity())
            }
            R.id.menu_sign_out -> {
                signoutUser()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createAdapter() {
        adapter = GroupAdapter<ViewHolder>()
        recylerview_lates_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recylerview_lates_messages.adapter = adapter

        adapter.setOnItemClickListener { item, _ ->
            val latestMessagesItem = item as LatestMessagesItem
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY, latestMessagesItem.newMessage.user)
            navigateToActivityWithIntent(intent)
        }
    }

}
