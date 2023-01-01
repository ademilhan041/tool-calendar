package com.ailhan.tool.calendar

import java.time.LocalDate

interface HolidayRepo {
    fun isoHoliday(date: LocalDate): Boolean

    fun islHoliday(date: LocalDate): Boolean
}
