package com.kotlinsample.app.activities

import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.kotlinsample.app.R
import com.kotlinsample.app.listeners.DataServiceListeners
import com.kotlinsample.app.listeners.ServiceListener
import com.kotlinsample.app.models.NewMessage
import com.kotlinsample.app.models.User
import com.kotlinsample.app.services.ServiceManager
import com.kotlinsample.app.viewItems.ChatFromItem
import com.kotlinsample.app.viewItems.ChatToItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : BaseActivity() {

    var toUseriId: String? = null
    private lateinit var adapter: GroupAdapter<ViewHolder>
    var fromUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        fromUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        toUseriId = fromUser?.uid
        createAdapter()

        supportActionBar?.title = fromUser?.username

        setOnClickListeners()

        initMessageListener()
    }

    private fun initMessageListener() {
        serviceManager.listenMessages(fromUser?.uid,object : DataServiceListeners {

            override fun onSuccess(snapshot: DataSnapshot) {
                val newMessage = snapshot.getValue(NewMessage::class.java)
                if (newMessage != null) {
                    if (serviceManager.getCurrentUser()?.uid == newMessage.fromId) {
                        adapter.add(ChatToItem(newMessage.message,ServiceManager.user!! ))
                    } else {
                        adapter.add(ChatFromItem(newMessage.message,fromUser!!))
                    }
                    message_recylcerview_chat_log.scrollToPosition(adapter.itemCount -1)
                }

            }

            override fun onSuccess() {
            }

            override fun onFailure(error: DatabaseError) {
            }

            override fun onFailure() {
            }
        })
    }

    private fun setOnClickListeners() {
        send_message_button_chat_log.setOnClickListener {
            if (send_message_edittext_chat_log.text.isEmpty()) {
                return@setOnClickListener
            }

            serviceManager.sendMessage(
                send_message_edittext_chat_log.text.toString(),
                toUseriId!!,
                serviceListener = object :
                    ServiceListener {
                    override fun onSuccess() {
                        send_message_edittext_chat_log.text.clear()
                        message_recylcerview_chat_log.scrollToPosition(adapter.itemCount -1)
                        Log.e("MESSAGE_STATUS", "Message sent successfully")
                    }

                    override fun onFailure() {
                        Log.e("MESSAGE_STATUS", "Message sent failed")

                    }
                })
        }
    }

    private fun createAdapter() {
        adapter = GroupAdapter<ViewHolder>()

        message_recylcerview_chat_log.adapter = adapter
    }
}
