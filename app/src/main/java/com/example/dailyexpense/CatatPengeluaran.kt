package com.example.dailyexpense

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CatatPengeluaran : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_pengeluaran)

        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi elemen UI
        val btnPilihTanggal: Button = findViewById(R.id.btnPilihTanggal)
        val tvTanggalDipilih: TextView = findViewById(R.id.tvTanggalDipilih)
        val etJudul: EditText = findViewById(R.id.editTextText)
        val etNominal: EditText = findViewById(R.id.editTextNumberDecimal)
        val spinnerKategori: Spinner = findViewById(R.id.spinner)
        val btnSave: Button = findViewById(R.id.buttonAddPengeluaran)

        // Listener untuk memilih tanggal
        btnPilihTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Tampilkan DatePickerDialog
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvTanggalDipilih.text = selectedDate
            }, year, month, day).show()
        }

        // Listener untuk menyimpan data pengeluaran
        btnSave.setOnClickListener {
            val tanggal = tvTanggalDipilih.text.toString()
            val judul = etJudul.text.toString()
            val nominal = etNominal.text.toString()
            val kategori = spinnerKategori.selectedItem.toString()

            if (judul.isBlank() || nominal.isBlank() || tanggal == "Tanggal belum dipilih") {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveDataToFirestore(tanggal, judul, nominal, kategori)
        }
    }

    private fun saveDataToFirestore(tanggal: String, judul: String, nominal: String, kategori: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User tidak terautentikasi.", Toast.LENGTH_SHORT).show()
            return
        }

        val pengeluaranData = hashMapOf(
            "userId" to userId,
            "tanggal" to tanggal,
            "judul" to judul,
            "nominal" to nominal,
            "kategori" to kategori
        )

        firestore.collection("pengeluaran")
            .add(pengeluaranData)
            .addOnSuccessListener {
                Toast.makeText(this, "Pengeluaran berhasil disimpan!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menyimpan pengeluaran: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
