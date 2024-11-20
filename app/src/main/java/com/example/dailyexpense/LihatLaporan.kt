package com.example.dailyexpense

import android.graphics.Color
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat
import java.text.NumberFormat

class LihatLaporan : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_laporan)

        val pieChart: PieChart = findViewById(R.id.pieChart)
        val spinnerBulan: Spinner = findViewById(R.id.spinner2)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User tidak terautentikasi", Toast.LENGTH_SHORT).show()
            return
        }

        // Listener untuk Spinner
        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedMonth = position + 1 // Indeks Spinner dimulai dari 0
                val selectedYear = "2024" // Anda bisa mengganti ini sesuai kebutuhan

                val selectedMonthYear = "$selectedMonth/$selectedYear"
                fetchDataAndGenerateChart(pieChart, userId, selectedMonthYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada tindakan jika tidak ada bagian yang dipilih
            }
        }
    }

    private fun fetchDataAndGenerateChart(pieChart: PieChart, userId: String, selectedMonthYear: String) {
        firestore.collection("budgets")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { budgetSnapshot ->
                firestore.collection("pengeluaran")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { expenseSnapshot ->
                        val totalBudgets = mutableMapOf<String, Float>()
                        val totalExpenses = mutableMapOf<String, Float>()

                        for (document in budgetSnapshot) {
                            val date = document.getString("date") ?: continue
                            val category = document.getString("category") ?: continue
                            val amount = document.getString("amount")?.toFloatOrNull() ?: continue

                            // Filter berdasarkan bulan dan tahun
                            val monthYear = date.substring(date.indexOf("/") + 1)
                            if (monthYear == selectedMonthYear) {
                                totalBudgets[category] = totalBudgets.getOrDefault(category, 0f) + amount
                            }
                        }

                        for (document in expenseSnapshot) {
                            val date = document.getString("tanggal") ?: continue
                            val category = document.getString("kategori") ?: continue
                            val amount = document.getString("nominal")?.toFloatOrNull() ?: continue

                            // Filter berdasarkan bulan dan tahun
                            val monthYear = date.substring(date.indexOf("/") + 1)
                            if (monthYear == selectedMonthYear) {
                                totalExpenses[category] = totalExpenses.getOrDefault(category, 0f) + amount
                            }
                        }

                        // Update PieChart
                        updatePieChart(pieChart, totalBudgets, totalExpenses)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal membaca data pengeluaran: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal membaca data anggaran: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePieChart(
        pieChart: PieChart,
        totalBudgets: Map<String, Float>,
        totalExpenses: Map<String, Float>
    ) {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()
        val predefinedColors = listOf(
            Color.rgb(255, 99, 132),
            Color.rgb(54, 162, 235),
            Color.rgb(255, 206, 86),
            Color.rgb(75, 192, 192),
            Color.rgb(153, 102, 255)
        )
        var colorIndex = 0

        for ((category, budget) in totalBudgets) {
            val expense = totalExpenses.getOrDefault(category, 0f)
            val remaining = (budget - expense).coerceAtLeast(0f)

            if (remaining > 0) {
                entries.add(PieEntry(remaining, category))
                colors.add(predefinedColors[colorIndex % predefinedColors.size])
                colorIndex++
            }
        }

        if (entries.isEmpty()) {
            Toast.makeText(this, "Tidak ada data untuk ditampilkan di PieChart.", Toast.LENGTH_SHORT).show()
            return
        }

        val dataSet = PieDataSet(entries, "Progres Anggaran")
        dataSet.colors = colors
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.BLACK

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.description.isEnabled = false
        pieChart.centerText = "Progres Anggaran"
        pieChart.setCenterTextSize(16f)
        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 45f
        pieChart.animateY(1000)

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
                if (e is PieEntry) {
                    val category = e.label
                    val remaining = e.value
                    Toast.makeText(
                        this@LihatLaporan,
                        "Sisa anggaran untuk $category: ${formatRupiah(remaining)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected() {
                // Tidak ada tindakan jika tidak ada bagian yang dipilih
            }
        })

        pieChart.invalidate()
    }

    private fun formatRupiah(value: Float): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return "Rp ${formatter.format(value)}"
    }
}
