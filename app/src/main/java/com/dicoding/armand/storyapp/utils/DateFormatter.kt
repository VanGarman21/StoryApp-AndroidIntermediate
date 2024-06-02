package com.dicoding.armand.storyapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(currentDateString: String, targetTimeZone: String): String {
        val instant = parseDate(currentDateString)
        val formatter = createFormatter(targetTimeZone)
        return formatter.format(instant)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(dateString: String): Instant {
        return Instant.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFormatter(timeZone: String): DateTimeFormatter {
        return DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(timeZone))
    }
}