package com.ailhan.tool.calendar.util

import java.time.DayOfWeek
import java.time.LocalDate

data class DateRange(val start: LocalDate, val end: LocalDate) {
    private var range = if (start.isEqual(end)) start..end else start..(end.minusDays(1))

    fun between(date: LocalDate): Boolean {
        return date in range
    }

    fun insideOf(other: DateRange): Boolean {
        if (other.start.isEqual(other.end)) return between(other.start) || between(other.end)
        return between(other.start) || between(other.end.minusDays(1))
    }

    fun countGivenWeekday(weekDay: DayOfWeek): Int {
        var count = 0
        var tempDate = range.start
        while (!tempDate.isAfter(range.endInclusive)) {
            if (tempDate.dayOfWeek == weekDay) count++
            tempDate = tempDate.plusDays(1)
        }

        return count
    }

    fun forEach(callback: (date: LocalDate) -> Unit) {
        var tempDate = range.start
        while (!tempDate.isAfter(range.endInclusive)) {
            callback(tempDate)
            tempDate = tempDate.plusDays(1)
        }
    }

    fun asList(): List<LocalDate> {
        val dateList = mutableListOf<LocalDate>()
        var tempDate = range.start
        while (!tempDate.isAfter(range.endInclusive)) {
            dateList.add(tempDate)
            tempDate = tempDate.plusDays(1)
        }
        return dateList
    }

    fun isAscendingDate() = start.isBefore(end)

    fun isDescendingDate() = !isAscendingDate()

    fun isSameDate() = start == end
}
