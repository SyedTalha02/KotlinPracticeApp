package com.kotlinsample.app.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kotlinsample.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOnClickListener()
    }

    private fun setOnClickListener() {


        register_button_register.setOnClickListener {
            Toast.makeText(
                this,
                "Username: ${username_edittext_register.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        login_textview_register.setOnClickListener{
            val intent = Intent(this,  LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
