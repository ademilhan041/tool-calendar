package com.ailhan.tool.calendar.holiday.calendarify

data class CalendarifyDto(var response: CalendarifyResponseDto)

data class CalendarifyResponseDto(var holidays: List<CalendarifyHolidayDto>)

data class CalendarifyHolidayDto(
    var date: CalendarifyDateDto,
    var name: String,
    var type: List<String>?
)

data class CalendarifyDateDto(
    var datetime: CalendarifyDateTimeDto,
    var iso: String
)

data class CalendarifyDateTimeDto(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int,
    var second: Int
)
