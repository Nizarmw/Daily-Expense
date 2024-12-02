package com.example.dailyexpense

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Mainpage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupEdgeToEdge()
        setupToolbarAndNavigation()
        setupButtonListeners()
        displayUserName()
    }

    private fun setupToolbarAndNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // skip
                }
                R.id.menu_settings -> {
                    // skip
                }
                R.id.menu_logout -> {
                    logoutUser()
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupButtonListeners() {
        val userId = auth.currentUser?.uid  // Get the current user ID

        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(this, CatatAnggaran::class.java)
            intent.putExtra("USER_ID", userId)  // Pass the userId to CatatAnggaran
            startActivity(intent)
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(this, CatatPengeluaran::class.java)
            intent.putExtra("USER_ID", userId)  // Pass the userId to CatatPengeluaran
            startActivity(intent)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, LihatLaporan::class.java)
            intent.putExtra("USER_ID", userId)  // Pass the userId to LihatLaporan
            startActivity(intent)
        }
    }

    private fun displayUserName() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val docRef = db.collection("users").document(userId)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("username")
                    findViewById<TextView>(R.id.textView10).text = userName
                }
            }.addOnFailureListener {
                findViewById<TextView>(R.id.textView10).text = "User not found"
            }
        }
    }
    private fun logoutUser() {
        auth.signOut()


        val intent = Intent(this, SignIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
