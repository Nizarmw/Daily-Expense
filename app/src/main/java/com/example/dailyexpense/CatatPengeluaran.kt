package com.example.dailyexpense

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class CatatPengeluaran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_pengeluaran)
        // Inisialisasi tombol dan text view
        val btnPilihTanggal: Button = findViewById(R.id.btnPilihTanggal)
        val tvTanggalDipilih: TextView = findViewById(R.id.tvTanggalDipilih)

        // Listener untuk DatePickerDialog
        btnPilihTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Tampilkan DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    tvTanggalDipilih.text = selectedDate // Tampilkan tanggal di TextView
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Setup Edge-to-Edge untuk sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}