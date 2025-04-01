package com.example.dashboard_hardware

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.RandomAccessFile
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HardwareDashboardApp(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardwareDashboardApp(context: Context) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hardware Dashboard") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val cpuUsage by remember { mutableStateOf(getCpuUsage()) }
            val ramUsage by remember { mutableStateOf(getRamUsage(context)) }
            val storageInfo by remember { mutableStateOf(getStorageInfo()) }

            DashboardItem("CPU Usage", cpuUsage)
            DashboardItem("RAM Usage", ramUsage)
            DashboardItem("Storage Info", storageInfo)
        }
    }
}

@Composable
fun DashboardItem(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 20.sp, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 18.sp)
        }
    }
}

fun getCpuUsage(): String {
    return try {
        val reader = RandomAccessFile("/proc/stat", "r")
        val load = reader.readLine().split(" ")
        println("Raw CPU Data: ${load.joinToString(", ")}")

        val user = load[2].toLong()
        val nice = load[3].toLong()
        val system = load[4].toLong()
        val idle = load[5].toLong()
        reader.close()

        val total = user + nice + system + idle
        "${(100 * (total - idle) / total.toDouble())}%"
    } catch (e: Exception) {
        "N/A"
    }
}

fun getRamUsage(context: Context): String {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)

    val totalRam = memoryInfo.totalMem / (1024 * 1024)
    val availableRam = memoryInfo.availMem / (1024 * 1024)
    val usedRam = totalRam - availableRam

    return "$usedRam MB / $totalRam MB"
}

fun getStorageInfo(): String {
    val stat = StatFs(Environment.getDataDirectory().path)
    val totalBytes = stat.totalBytes
    val availableBytes = stat.availableBytes
    val usedBytes = totalBytes - availableBytes

    val totalGB = totalBytes / (1024 * 1024 * 1024)
    val usedGB = usedBytes / (1024 * 1024 * 1024)

    return "$usedGB GB / $totalGB GB"
}
