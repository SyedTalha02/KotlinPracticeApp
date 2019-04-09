package com.kotlinsample.app.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kotlinsample.app.R
import com.kotlinsample.app.listeners.AuthServiceListeners
import com.kotlinsample.app.models.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : BaseActivity() {

    var selectedImageUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        createOnClickListener()
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
            navigateToActivity(LoginActivity())
        }

        selectphoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun registerUser(email: String, password: String) {
       showLoader()
        serviceManager.registerUser(email, password, authServiceListeners = object:AuthServiceListeners{

            override fun onSuccess() {

                hideLoader()
            }

            override fun onFailure() {
                hideLoader()
            }

            override fun onSuccess(authResult: AuthResult?) {
                val user = authResult?.user
                uploadImageToFirebaseStorage()
            }

            override fun onFailure(exception: Exception?) {
                Log.w("Create User Status", "createUserWithEmail:failure $exception")
                hideLoader()
            }
        })
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
            }.addOnFailureListener { hideLoader() }
        }
    }

    private fun saveUserToFirebaseStorage(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edittext_register.text.toString(), profileImageUrl, email_editText_register.text.toString())
        ref.setValue(user)
            .addOnSuccessListener {
//                navigateToLatestMessageActivity()

                hideLoader()
                navigateToActivityClearTask(LatestMessagesActivity())
            }.addOnFailureListener { hideLoader() }
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

    fun showLoader(){
        loader.visibility = View.VISIBLE
    }

    fun hideLoader(){
        loader.visibility = View.GONE
    }
}
