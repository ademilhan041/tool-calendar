package com.ailhan.tool.calendar.util

import java.io.InputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.isWeekend() = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY

fun Properties.loadStream(stream: InputStream): Properties {
    val props = Properties()
    props.load(stream)
    return props
}

fun String.toLocalDate() = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)