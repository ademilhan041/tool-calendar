package com.ailhan.tool.calendar

import com.ailhan.tool.calendar.util.DateRange
import java.time.LocalDate

interface BusinessCalendar {
    fun isHoliday(date: LocalDate): Boolean

    fun getBusinessDayOrAfter(date: LocalDate): LocalDate

    fun getBusinessDayOrBefore(date: LocalDate): LocalDate

    fun getNextBusinessDay(date: LocalDate): LocalDate

    fun getPreviousBusinessDay(date: LocalDate): LocalDate

    fun getBusinessDayCountBetween(range: DateRange): Int
}
