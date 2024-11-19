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

class CatatAnggaran : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore  // Firestore instance
    private lateinit var auth: FirebaseAuth           // Firebase Auth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_anggaran)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()  // Initialize Firestore

        val btnPilihTanggal: Button = findViewById(R.id.btnPilihTanggalAnggaran)
        val tvTanggalDipilih: TextView = findViewById(R.id.tvTanggalDipilihAnggaran)
        val editTextAmount: EditText = findViewById(R.id.editTextNumber)
        val editTextTitle: EditText = findViewById(R.id.editTextText4)
        val spinnerCategory: Spinner = findViewById(R.id.spinner4)
        val btnSave: Button = findViewById(R.id.buttonAddPengeluaran3)

        btnPilihTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                tvTanggalDipilih.text = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnSave.setOnClickListener {
            val date = tvTanggalDipilih.text.toString()
            val amount = editTextAmount.text.toString()
            val title = editTextTitle.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            saveDataToFirestore(date, amount, title, category)
        }
    }

    private fun saveDataToFirestore(date: String, amount: String, title: String, category: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User tidak terautentikasi.", Toast.LENGTH_SHORT).show()
            return
        }

        val budgetData = hashMapOf(
            "userId" to userId,
            "date" to date,
            "amount" to amount,
            "title" to title,
            "category" to category
        )

        firestore.collection("budgets")
            .add(budgetData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Anggaran tersimpan dengan ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding document: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
