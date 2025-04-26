package com.example.myapplication

import android.app.ActivityManager
import android.content.Context

class RAMDataCollector(private val context: Context) {
    fun getRamUsagePercentage(): Float {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        // Calcul du pourcentage d'utilisation
        return (memoryInfo.totalMem - memoryInfo.availMem) * 100f / memoryInfo.totalMem
    }

    fun getRamDetails(): RamDetails {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        return RamDetails(
            totalMem = memoryInfo.totalMem,
            availMem = memoryInfo.availMem,
            usedMem = memoryInfo.totalMem - memoryInfo.availMem,
            percentUsed = (memoryInfo.totalMem - memoryInfo.availMem) * 100f / memoryInfo.totalMem
        )
    }

    data class RamDetails(
        val totalMem: Long,
        val availMem: Long,
        val usedMem: Long,
        val percentUsed: Float
    )
}