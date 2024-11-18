package com.example.dailyexpense

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button


class Mainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        setupEdgeToEdge()
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, CatatAnggaran::class.java))
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, CatatPengeluaran::class.java))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, LihatLaporan::class.java))
        }
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

