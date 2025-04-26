package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.os.HandlerCompat.postDelayed
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // 5 secondes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        updateDeviceInfo()
        startPeriodicUpdates()
    }

    private fun setupNavigation() {
        binding.btnCpu.setOnClickListener {
            startActivity(Intent(this, CPUActivity::class.java))
        }
        binding.btnRam.setOnClickListener {
            startActivity(Intent(this, RAMActivity::class.java))
        }
        binding.btnBattery.setOnClickListener {
            startActivity(Intent(this, BatteryActivity::class.java))
        }
    }

    private fun startPeriodicUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateDeviceInfo()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    private fun updateDeviceInfo() {
        // Animation de rafraîchissement


        // Mise à jour des informations de l'appareil
        binding.tvDeviceModel.text = DeviceInfoUtil.getDeviceModel()
        binding.tvAndroidVersion.text = DeviceInfoUtil.getAndroidVersion()
        binding.tvTotalRam.text = DeviceInfoUtil.getTotalRAM(this)

        val (free, total) = DeviceInfoUtil.getStorageInfo(this)
        binding.tvStorage.text = "$free libre sur $total"

        binding.tvLocalIp.text = DeviceInfoUtil.getLocalIpAddress()
        binding.tvUptime.text = DeviceInfoUtil.getUptime()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}