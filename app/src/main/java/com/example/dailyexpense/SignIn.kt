package com.example.dailyexpense

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setupEdgeToEdge()

        // Setup click listener for the Sign Up prompt
        findViewById<TextView>(R.id.textViewSignUpPrompt).setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signin)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigateToSignUp() {
        // Intent to start SignUp Activity
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }
}
