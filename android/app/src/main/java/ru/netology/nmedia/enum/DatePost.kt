package ru.netology.nmedia.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.formatDate(): String {
    return try {
        val instant = Instant.parse(this)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())

        zonedDateTime.format(
            DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm")
        )
    } catch (e: Exception) {
        this
    }
}