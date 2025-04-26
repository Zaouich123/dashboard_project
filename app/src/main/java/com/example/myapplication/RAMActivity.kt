package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.example.myapplication.databinding.ActivityRamBinding
import com.github.mikephil.charting.charts.LineChart

class RAMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRamBinding
    private lateinit var ramChart: LineChart
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L // mise à jour chaque seconde
    private lateinit var ramDataCollector: RAMDataCollector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ramDataCollector = RAMDataCollector(this)
        setupChart()
        startDataCollection()
    }

    private fun setupChart() {
        ramChart = binding.ramChart
        ChartUtils.setupRamChart(ramChart)
    }

    private fun startDataCollection() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateRamData()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun updateRamData() {
        val ramUsage = ramDataCollector.getRamUsagePercentage()
        ChartUtils.addEntryToChart(ramChart, ramUsage)

        // Animation au refresh

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}