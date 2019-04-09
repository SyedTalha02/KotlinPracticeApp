package com.kotlinsample.app.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.kotlinsample.app.services.ServiceManager

/**
 * Created on 4/5/2019.
 */
open class BaseActivity:AppCompatActivity(){

    val serviceManager = ServiceManager()

    fun navigateToActivityClearTask(activity: BaseActivity) {
        val intent = Intent(this, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun navigateToActivity(activity: BaseActivity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    fun navigateToActivityWithIntent(intent: Intent) {
        startActivity(intent)
    }


    fun signoutUser() {
        serviceManager.signOut()
        navigateToActivityClearTask(RegisterActivity())
    }

}