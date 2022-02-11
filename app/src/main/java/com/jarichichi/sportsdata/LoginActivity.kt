package com.jarichichi.sportsdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signinButton: Button
    private lateinit var switchToSignUpButton: Button
    private lateinit var errorText: TextView
    private lateinit var rememberCheck: CheckBox

    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.email_textfield)
        passwordField = findViewById(R.id.password_textfield)
        signinButton = findViewById(R.id.signin_button)
        switchToSignUpButton = findViewById(R.id.signup_button)
        errorText = findViewById(R.id.signin_error_text)
        rememberCheck = findViewById(R.id.remember_checkbox)

        fAuth = FirebaseAuth.getInstance()

        if(fAuth.getCurrentUser() != null){
            this.startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        switchToSignUpButton.setOnClickListener{
            this.startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        signinButton.setOnClickListener {
            val email : String = emailField.text.toString().trim()
            val password : String = passwordField.text.toString().trim()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                errorText.text = "All fields are required to login"
            }

            else{
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        this.startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        errorText.text = task.exception!!.message
                    }


                }
            }
        }

    }
}