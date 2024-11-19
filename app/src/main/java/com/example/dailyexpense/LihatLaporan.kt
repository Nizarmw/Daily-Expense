package com.example.dailyexpense

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class LihatLaporan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_laporan)

        // Inisialisasi PieChart
        val pieChart: PieChart = findViewById(R.id.pieChart)

        // Data untuk Pie Chart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Makanan"))
        entries.add(PieEntry(30f, "Transportasi"))
        entries.add(PieEntry(20f, "Hiburan"))
        entries.add(PieEntry(10f, "Lainnya"))

        // Konfigurasi DataSet
        val dataSet = PieDataSet(entries, "Kategori Pengeluaran")
        dataSet.colors = listOf(
            Color.rgb(255, 123, 123), // Warna Makanan
            Color.rgb(123, 255, 123), // Warna Transportasi
            Color.rgb(123, 123, 255), // Warna Hiburan
            Color.rgb(255, 223, 123)  // Warna Lainnya
        )
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // Konfigurasi PieData
        val data = PieData(dataSet)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        // Atur ke PieChart
        pieChart.data = data
        pieChart.description.isEnabled = false // Hilangkan deskripsi default
        pieChart.centerText = "Pengeluaran"
        pieChart.setCenterTextSize(16f)
        pieChart.setUsePercentValues(true) // Tampilkan dalam persentase
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 45f
        pieChart.animateY(1000) // Animasi
    }
}
