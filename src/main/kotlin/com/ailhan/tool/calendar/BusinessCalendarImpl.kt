package com.ailhan.tool.calendar

import com.ailhan.tool.calendar.util.DateRange
import com.ailhan.tool.calendar.util.isWeekend
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class BusinessCalendarImpl(private val repo: HolidayRepo) : BusinessCalendar {
    override fun isHoliday(date: LocalDate): Boolean {
        return repo.isoHoliday(date) || repo.islHoliday(date) || date.isWeekend()
    }

    override fun getBusinessDayOrAfter(date: LocalDate): LocalDate {
        if (!isHoliday(date)) return date

        var next = date
        while (isHoliday(next))
            next = next.plusDays(1)

        return next
    }

    override fun getBusinessDayOrBefore(date: LocalDate): LocalDate {
        if (!isHoliday(date)) return date

        var prev = date
        while (isHoliday(prev))
            prev = prev.minusDays(1)

        return prev
    }

    override fun getNextBusinessDay(date: LocalDate): LocalDate {
        var next = date.plusDays(1)
        while (isHoliday(next))
            next = next.plusDays(1)

        return next
    }

    override fun getPreviousBusinessDay(date: LocalDate): LocalDate {
        var prev = date.minusDays(1)
        while (isHoliday(prev))
            prev = prev.minusDays(1)

        return prev
    }

    override fun getBusinessDayCountBetween(range: DateRange): Int {
        val start = range.start
        val end = range.end
        if (start.isEqual(end)) return if (isHoliday(start)) 0 else 1

        val daysBetween = ChronoUnit.DAYS.between(start, end)
        return (0 until daysBetween).map { if (isHoliday(start.plusDays(it))) 0 else 1 }.sumOf { it }
    }
}
