package com.example.myapplication

import java.io.BufferedReader
import java.io.FileReader

class CPUDataCollector {
    // Lecture des données CPU à partir de /proc/stat
    fun getCpuUsagePercentage(): Float {
        try {
            // Première lecture
            val statsBefore = readCpuStats()

            // Attendre un court instant
            Thread.sleep(500)

            // Deuxième lecture
            val statsAfter = readCpuStats()

            // Calcul de la différence
            val idleDiff  = statsAfter.idle - statsBefore.idle
            val totalDiff = statsAfter.total - statsBefore.total

            // Calcul du pourcentage d'utilisation
            return 100f * (1f - idleDiff.toFloat() / totalDiff.toFloat())
        } catch (e: Exception) {
            e.printStackTrace()
            return 0f // Valeur par défaut en cas d'erreur
        }
    }

    private fun readCpuStats(): CpuStats {
        try {
            val reader = BufferedReader(FileReader("/proc/stat"))
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