package com.jarichichi.sportsdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var passwordConfirmField: EditText
    private lateinit var signupButton: Button
    private lateinit var switchToSignInButton: Button
    private lateinit var errorText: TextView

    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameField = findViewById(R.id.username_textfield)
        emailField = findViewById(R.id.email_reg_textfield)
        passwordField = findViewById(R.id.password_reg_textfield)
        passwordConfirmField = findViewById(R.id.confirmpassword_reg_textfield)
        signupButton = findViewById(R.id.signup_reg_button)
        switchToSignInButton = findViewById(R.id.signin_reg_button)
        errorText = findViewById(R.id.register_error_message)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        if(fAuth.getCurrentUser() != null){
            this.startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        switchToSignInButton.setOnClickListener{
            this.startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        signupButton.setOnClickListener {
            val username : String = usernameField.text.toString().trim()
            val email : String = emailField.text.toString().trim()
            val password : String = passwordField.text.toString().trim()
            val passwordConfirm : String = passwordConfirmField.text.toString().trim()

            if(TextUtils.isEmpty(username)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordConfirm)){
                errorText.text = "All fields are required for registration"
            }

            else if(password.length < 6){
                errorText.text = "Password must be longer then 6 characters"
            }

            else if(!password.equals(passwordConfirm)){
                errorText.text = "Passwords must match"
            }


            else {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                        userID = fAuth.currentUser!!.uid
                        val documentRefenence: DocumentReference = fStore.collection("users").document("userID")
                        val user: Map<String, String> =
                            mapOf("username" to username,
                                "email" to email)

                        documentRefenence.set(user)
                        this.startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        errorText.text = task.exception!!.message
                    }


                }
            }

        }
    }
}