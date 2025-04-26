package com.example.myapplication

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet

object ChartStyler {
    fun applyDarkModeToChart(chart: LineChart, isDarkMode: Boolean) {
        if (isDarkMode) {
            // Appliquer les styles dark mode
            chart.setBackgroundColor(Color.parseColor("#1E1E1E"))
            chart.description.textColor = Color.WHITE
            chart.legend.textColor = Color.WHITE
            chart.xAxis.textColor = Color.WHITE
            chart.axisLeft.textColor = Color.WHITE
            chart.axisRight.textColor = Color.WHITE

            // Changer les couleurs des lignes pour le dark mode
            val dataSet = chart.data.getDataSetByIndex(0) as LineDataSet
            dataSet.color = Color.parseColor("#4CAF50") // Vert pour dark mode
            dataSet.setCircleColor(Color.parseColor("#4CAF50"))
            dataSet.fillColor = Color.parseColor("#4CAF50")
        } else {
            // Appliquer les styles light mode
            chart.setBackgroundColor(Color.WHITE)
            chart.description.textColor = Color.BLACK
            chart.legend.textColor = Color.BLACK
            chart.xAxis.textColor = Color.BLACK
            chart.axisLeft.textColor = Color.BLACK
            chart.axisRight.textColor = Color.BLACK

            // Changer les couleurs des lignes pour le light mode
            val dataSet = chart.data.getDataSetByIndex(0) as LineDataSet
            dataSet.color = Color.parseColor("#2196F3") // Bleu pour light mode
            dataSet.setCircleColor(Color.parseColor("#2196F3"))
            dataSet.fillColor = Color.parseColor("#2196F3")
        }

        chart.invalidate() // Rafraîchir le graphique
    }
}