package com.teds.warungku.pos.core.common

import java.time.LocalDate
import java.time.ZoneId

data class DayRange(
    val startOfDay: Long,
    val startOfNextDay: Long
)

fun getTodayRange(): DayRange {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)

    val startOfDay = today
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    val startOfNextDay = today
        .plusDays(1)
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    return DayRange(
        startOfDay = startOfDay,
        startOfNextDay = startOfNextDay
    )
}