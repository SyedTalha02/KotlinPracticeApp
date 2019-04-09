package com.kotlinsample.app.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.kotlinsample.app.R
import com.kotlinsample.app.listeners.AuthServiceListeners
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setOnClickListeners()
        auth = FirebaseAuth.getInstance()
    }

    private fun setOnClickListeners() {
        backtoreg_textview_login.setOnClickListener { finish() }

        login_button_login.setOnClickListener {
            if (email_edittext_login.text.isEmpty() || password_edittext_login.text.isEmpty()) {
                Toast.makeText(this, "Please provide email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email_edittext_login.text.toString(), password_edittext_login.text.toString())
        }
    }


    private fun loginUser(email: String, password: String) {
        showLoader()
        serviceManager.loginUser(email, password, authServiceListeners = object : AuthServiceListeners {
            override fun onSuccess() {
                hideLoader()
            }

            override fun onFailure() {
                hideLoader()
            }

            override fun onSuccess(authResult: AuthResult?) {
                hideLoader()
                navigateToActivityClearTask(LatestMessagesActivity())
            }

            override fun onFailure(exception: Exception?) {
                hideLoader()
                Log.w("Login User Status", "loginUser:failure", exception)
            }

        })

    }


    fun showLoader() {
        loader.visibility = View.VISIBLE
    }

    fun hideLoader() {
        loader.visibility = View.GONE
    }

}
