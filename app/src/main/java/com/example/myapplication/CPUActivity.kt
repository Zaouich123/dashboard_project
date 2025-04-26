package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCpuBinding
import com.github.mikephil.charting.charts.LineChart

class CPUActivity : AppCompatActivity() {
    private lateinit var binding  : ActivityCpuBinding
    private lateinit var cpuChart : LineChart
    private val handler           = Handler(Looper.getMainLooper())
    private val updateInterval    = 1000L // mise à jour chaque seconde
    private val cpuDataCollector  = CPUDataCollector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCpuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
        startDataCollection()
    }

    private fun setupChart() {
        cpuChart = binding.cpuChart
        ChartUtils.setupCpuChart(cpuChart)
    }

    private fun startDataCollection() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateCpuData()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun updateCpuData() {
        val cpuUsage = cpuDataCollector.getCpuUsagePercentage()
        ChartUtils.addEntryToChart(cpuChart, cpuUsage)

        // Animation au refresh

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}