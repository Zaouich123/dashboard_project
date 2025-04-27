package com.example.myapplication

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.FileReader
import java.io.InputStreamReader

class CPUDataCollector {
    // Lecture des données CPU à partir de /proc/stat
    fun getCpuUsagePercentage(): Float {
        try {
            // Première lecture
            val statsBefore = readCpuStats()
            Log.d("CPUDataCollector", "Stats before: idle=${statsBefore.idle}, total=${statsBefore.total}")


            // Attendre un court instant
            Thread.sleep(500)

            // Deuxième lecture
            val statsAfter = readCpuStats()
            Log.d("CPUDataCollector", "Stats after: idle=${statsAfter.idle}, total=${statsAfter.total}")

            // Calcul de la différence
            val idleDiff  = statsAfter.idle - statsBefore.idle
            val totalDiff = statsAfter.total - statsBefore.total
            Log.d("CPUDataCollector", "Diffs: idle=$idleDiff, total=$totalDiff")
            // Calcul du pourcentage d'utilisation
            return 100f * (1f - idleDiff.toFloat() / totalDiff.toFloat())
        } catch (e: Exception) {
            e.printStackTrace()
            return 0f // Valeur par défaut en cas d'erreur
        }
    }

    private fun readCpuStats(): CpuStats {
        try {

            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)

            // Lire /proc/stat avec su (root)
            os.writeBytes("cat /proc/stat\n")
            os.writeBytes("exit\n")
            os.flush()

            // Lire la sortie de la commande
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            //val reader = BufferedReader(FileReader("/proc/stat"))
            val line   = reader.readLine() // Première ligne contient les stats CPU global
            reader.close()

            // Format typique: "cpu user nice system idle iowait irq softirq steal guest guest_nice"
            val values  = line.split("\\s+".toRegex()).drop(1) // Ignorer "cpu"

            var total   = 0L
            var idle    = values[3].toLong() // idle est la 4ème valeur

            // Somme de toutes les valeurs
            for (i in 0 until minOf(values.size, 10)) {
                total += values[i].toLong()
            }

            return CpuStats(idle, total)
        } catch (e: Exception) {
            e.printStackTrace()
            return CpuStats(0, 1) // Valeur par défaut en cas d'erreur
        }
    }

    data class CpuStats(val idle: Long, val total: Long)
}