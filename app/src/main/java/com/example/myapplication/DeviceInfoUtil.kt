package com.example.myapplication

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import java.net.Inet4Address
import java.net.NetworkInterface

object DeviceInfoUtil {
    fun getDeviceModel   ()   : String = Build.MODEL

    fun getAndroidVersion()   : String = Build.VERSION.RELEASE

    fun getTotalRAM(context: Context): String {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo      = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return formatSize(memoryInfo.totalMem)
    }

    fun getStorageInfo(context: Context): Pair<String, String> {
        val stat             = StatFs(Environment.getExternalStorageDirectory().path)
        val totalBytes       = stat.totalBytes
        val availableBytes   = stat.availableBytes
        return Pair(formatSize(availableBytes), formatSize(totalBytes))
    }

    fun getLocalIpAddress(): String {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf       = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress ?: "Non disponible"
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Non disponible"
    }

    fun getUptime(): String {
        val uptimeMillis = SystemClock.elapsedRealtime()
        val hours        = (uptimeMillis / (1000 * 60 * 60)).toInt()
        val minutes      = (uptimeMillis / (1000 * 60) % 60).toInt()
        return "$hours h $minutes min"
    }

    private fun formatSize(size: Long): String {
        val kb = 1024L
        val mb = kb * 1024L
        val gb = mb * 1024L

        return when {
            size >= gb -> String.format("%.2f GB", size.toFloat() / gb)
            size >= mb -> String.format("%.2f MB", size.toFloat() / mb)
            size >= kb -> String.format("%.2f KB", size.toFloat() / kb)
            else -> "$size bytes"
        }
    }
}