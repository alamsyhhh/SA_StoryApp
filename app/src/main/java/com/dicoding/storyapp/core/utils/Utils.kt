package com.dicoding.storyapp.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun dataconverter(dateData: String): String {
    val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val outputFormat = DateTimeFormatter.ofPattern("HH:mm")
    val dateTime = ZonedDateTime.parse(dateData, inputFormat)
        .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))

    return outputFormat.format(dateTime)
}
