package com.kotlinsample.app.listeners

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

/**
 * Created on 4/5/2019.
 */
interface DataServiceListeners: ServiceListener{

    fun onSuccess(snapshot: DataSnapshot)
    fun onFailure(error: DatabaseError)
}