package com.kotlinsample.app.listeners

import com.google.firebase.auth.AuthResult

/**
 * Created on 4/5/2019.
 */
interface AuthServiceListeners : ServiceListener {
//    fun onCompletion()
    fun onSuccess(authResult: AuthResult?)
    fun onFailure(exception: Exception?)
}