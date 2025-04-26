package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.google.android.material.animation.AnimationUtils

import com.example.myapplication.databinding.ActivityBatteryBinding

class BatteryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatteryBinding
    private lateinit var batteryReceiver: BatteryReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerBatteryReceiver()
    }

    private fun registerBatteryReceiver() {
        batteryReceiver = BatteryReceiver(binding)
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        registerReceiver(batteryReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    inner class BatteryReceiver(private val binding: ActivityBatteryBinding) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                // Animation au refresh


                // Température
                val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f
                binding.tvTemperature.text = String.format("%.1f°C", temp)

                // Voltage
                val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000f
                binding.tvVoltage.text = String.format("%.2f V", voltage)

                // Niveau de batterie
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                val batteryPct = level * 100 / scale
                binding.tvLevel.text = "$batteryPct%"

                // Santé de la batterie
                val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
                binding.tvHealth.text = when (health) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> "Bonne"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Surchauffe"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "Morte"
                    BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Surtension"
                    BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Défaillance"
                    BatteryManager.BATTERY_HEALTH_COLD -> "Froide"
                    else -> "Inconnue"
                }

                // Mode de charge
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN)
                binding.tvChargeMode.text = when (status) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> "En charge"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "En décharge"
                    BatteryManager.BATTERY_STATUS_FULL -> "Pleine"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Pas en charge"
                    else -> "Inconnu"
                }
            }
        }
    }
}