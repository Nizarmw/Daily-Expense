package com.example.dailyexpense

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val signUpButton = findViewById<Button>(R.id.buttonRegister)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Email, username, and password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Bikin User
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Simpan Username
                        val user = auth.currentUser
                        user?.let {
                            val userId = user.uid
                            val userData = hashMapOf(
                                "username" to username,
                                "email" to email
                            )
                            firestore.collection("users").document(userId).set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Sign up and save username successful.", Toast.LENGTH_SHORT).show()
                                    navigateToSignIn()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to save user info: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
    private fun navigateToSignIn() {
        // navtoSignInpage
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
    }
}
