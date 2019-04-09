package com.kotlinsample.app.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.kotlinsample.app.constants.LATEST_MESSAGES_PATH
import com.kotlinsample.app.constants.MESSAGES_PATH
import com.kotlinsample.app.constants.USERS_PATH
import com.kotlinsample.app.listeners.AuthServiceListeners
import com.kotlinsample.app.listeners.DataServiceListeners
import com.kotlinsample.app.listeners.ServiceListener
import com.kotlinsample.app.models.NewMessage
import com.kotlinsample.app.models.User


/**
 * Created on 4/5/2019.
 */
class ServiceManager() {

    companion object {
        var user: User? = null
    }


    private fun getAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    private fun getDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    fun getStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    fun loginUser(email: String, password: String, authServiceListeners: AuthServiceListeners) {
        getAuth().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult: AuthResult? -> authServiceListeners.onSuccess(authResult) }
            .addOnFailureListener { exception: Exception -> authServiceListeners.onFailure(exception) }


    }

    fun registerUser(email: String, password: String, authServiceListeners: AuthServiceListeners) {
        getAuth().createUserWithEmailAndPassword(email, password)

            .addOnSuccessListener { authResult: AuthResult? ->
                authServiceListeners.onSuccess(authResult)
            }
            .addOnFailureListener { exception: Exception ->
                authServiceListeners.onFailure(exception)
            }
    }

    fun fetchUsers(dataServiceListener: DataServiceListeners) {
        getDatabase().getReference(USERS_PATH).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                dataServiceListener.onFailure(p0)
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataServiceListener.onSuccess(p0)
            }
        })
    }

    fun signOut() {
        getAuth().signOut()
    }

    fun sendMessage(message: String, toId: String, serviceListener: ServiceListener) {
        val ref = getDatabase().getReference(MESSAGES_PATH + "/${getAuth().uid}/$toId").push()
        val revRef = getDatabase().getReference(MESSAGES_PATH + "/$toId/${getAuth().uid}").push()
        val latestMessagesRef = getDatabase().getReference(LATEST_MESSAGES_PATH + "/${getAuth().uid}/$toId")
        val latestMessagesRevRef = getDatabase().getReference(LATEST_MESSAGES_PATH + "/$toId/${getAuth().uid}")
        val newMessage =
            NewMessage(ref.key!!, getAuth().uid.toString(), toId, message, System.currentTimeMillis() / 1000,User())

        var count = 0

        ref.setValue(newMessage)
            .addOnSuccessListener {
                count++
                if (count == 4) {
                    serviceListener.onSuccess()
                }
            }
            .addOnFailureListener {
                serviceListener.onFailure()
            }
        revRef.setValue(newMessage)
            .addOnSuccessListener {
                count++
                if (count == 4) {
                    serviceListener.onSuccess()
                }
            }
            .addOnFailureListener {
                serviceListener.onFailure()
            }
        latestMessagesRef.setValue(newMessage)
            .addOnSuccessListener {
                count++
                if (count == 4) {
                    serviceListener.onSuccess()
                }
            }
            .addOnFailureListener {
                serviceListener.onFailure()
            }
        latestMessagesRevRef.setValue(newMessage)
            .addOnSuccessListener {
                count++
                if (count == 4) {
                    serviceListener.onSuccess()
                }
            }
            .addOnFailureListener {
                serviceListener.onFailure()
            }
    }

    fun listenMessages(toId: String?, dataServiceListener: DataServiceListeners) {
        val ref = getDatabase().getReference(MESSAGES_PATH + "/${getAuth().uid}/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                dataServiceListener.onSuccess(p0)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    fun listenLatestMessages(dataServiceListener: DataServiceListeners) {
        val ref = getDatabase().getReference(LATEST_MESSAGES_PATH + "/${getAuth().currentUser?.uid}")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                dataServiceListener.onSuccess(p0)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                dataServiceListener.onSuccess(p0)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    fun fetchCurrentUserDetails() {
        if (user == null) {
            val uid = getAuth().currentUser?.uid
            getDatabase().getReference(USERS_PATH + "/$uid")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        user = p0.getValue(User::class.java)
                    }
                })
        }
    }

    fun getUserFromId(userId: String, dataServiceListener: DataServiceListeners){
        getDatabase().getReference(USERS_PATH + "/$userId").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                dataServiceListener.onSuccess(p0)
            }
        })
    }

    fun getCurrentUser(): FirebaseUser? {
        return getAuth().currentUser
    }


}