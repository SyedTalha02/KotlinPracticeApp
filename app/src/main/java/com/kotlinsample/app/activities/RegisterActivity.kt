package com.kotlinsample.app.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kotlinsample.app.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var selectedImageUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        createOnClickListener()
        auth = FirebaseAuth.getInstance()
    }

    private fun createOnClickListener() {


        register_button_register.setOnClickListener {
            if (email_editText_register.text.isEmpty() || password_edittext_register.text.isEmpty()) {
                Toast.makeText(this, "Please fill in email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerUser(email_editText_register.text.toString(), password_edittext_register.text.toString())

        }

        login_textview_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Create User Status", "createUserWithEmail:success")
                    val user = auth.currentUser
                    uploadImageToFirebaseStorage()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Create User Status", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }.addOnFailureListener {
                Log.d("FAILURE", "Unable to create user")
            }


    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedImageUri == null) {
            return
        }

        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectedImageUri!!).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                Log.d("Register ", "${it}")

                saveUserToFirebaseStorage(it.toString())
            }
        }
    }

    private fun saveUserToFirebaseStorage(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edittext_register.text.toString(), profileImageUrl, email_editText_register.text.toString())
        ref.setValue(user)
            .addOnSuccessListener {
                navigateToLatestMessageActivity()
            }
    }

    private fun navigateToLatestMessageActivity() {
        var intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            circularimageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha = 0f
        }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String, val email: String) {
    constructor() : this("","","", "")

}