package com.example.myapplication

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

object ChartUtils {
    fun setupCpuChart(chart: LineChart) {
        setupChart(chart, "Utilisation CPU (%)")
    }

    fun setupRamChart(chart: LineChart) {
        setupChart(chart, "Utilisation RAM (%)")
    }

    private fun setupChart(chart: LineChart, label: String) {
        // Configuration générale du graphique
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.TRANSPARENT)

        // Configuration de l'axe X
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(5, true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Afficher le temps en secondes
                return "${value.toInt()}s"
            }
        }

        // Configuration de l'axe Y gauche
        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.axisMaximum = 100f // Pourcentage maximum
        leftAxis.axisMinimum = 0f  // Pourcentage minimum
        leftAxis.setDrawZeroLine(true)

        // Désactiver l'axe Y droit
        chart.axisRight.isEnabled = false

        // Configuration de la légende
        chart.legend.isEnabled = true
        chart.legend.form = Legend.LegendForm.LINE

        // Création du jeu de données initial vide
        val dataSet = LineDataSet(ArrayList<Entry>(), label)
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 3f
        dataSet.setDrawCircleHole(false)
        dataSet.valueTextSize = 9f
        dataSet.setDrawFilled(true)
        dataSet.formLineWidth = 1f
        dataSet.formSize = 15f
        dataSet.fillAlpha = 65
        dataSet.fillColor = Color.BLUE
        dataSet.setDrawValues(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        // Ajout du jeu de données au graphique
        val data = LineData(dataSet)
        chart.data = data
    }

    fun addEntryToChart(chart: LineChart, value: Float) {
        val data = chart.data

        if (data != null) {
            var dataSet = data.getDataSetByIndex(0) as LineDataSet?

            if (dataSet == null) {
                dataSet = LineDataSet(ArrayList<Entry>(), "Data")
                data.addDataSet(dataSet)
            }

            // Ajout d'une nouvelle entrée
            val x = (dataSet.entryCount).toFloat()
            data.addEntry(Entry(x, value), 0)

            // Limitation à 60 points (1 minute à raison de 1 point/seconde)
            if (dataSet.entryCount > 60) {
                dataSet.removeEntry(0)
                // Décalage des indices X
                for (i in 0 until dataSet.entryCount) {
                    dataSet.getEntryForIndex(i).x = i.toFloat()
                }
            }

            // Notification des changements
            data.notifyDataChanged()
            chart.notifyDataSetChanged()

            // Déplacement à la dernière entrée
            chart.moveViewToX(data.entryCount.toFloat())

            // Rafraîchissement
            chart.invalidate()
        }
    }
}