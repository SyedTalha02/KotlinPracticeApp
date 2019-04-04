package com.kotlinsample.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kotlinsample.app.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setOnClickListeners()
        auth = FirebaseAuth.getInstance()
    }

    private fun setOnClickListeners() {
        backtoreg_textview_login.setOnClickListener{finish()}

        login_button_login.setOnClickListener {
           if( email_edittext_login.text.isEmpty() || password_edittext_login.text.isEmpty()){
               Toast.makeText(this, "Please provide email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email_edittext_login.text.toString(), password_edittext_login.text.toString()) }
    }


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    navigateToLatestMessageActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login User Status", "loginUser:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }


            }.addOnFailureListener{
                Log.d("FAILURE", "Unable to login user")
            }


    }


    private fun navigateToLatestMessageActivity() {
        var intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
