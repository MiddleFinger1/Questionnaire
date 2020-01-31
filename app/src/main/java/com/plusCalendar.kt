package com

import java.util.*

infix operator fun Calendar.plus(calendar: Calendar): Calendar {
    this[Calendar.SECOND] += calendar[Calendar.SECOND]
    this[Calendar.MINUTE] += calendar[Calendar.MINUTE]
    this[Calendar.HOUR] += calendar[Calendar.HOUR]
    this[Calendar.DAY_OF_MONTH] += calendar[Calendar.DAY_OF_MONTH]
    this[Calendar.MONTH] += calendar[Calendar.MONTH]
    this[Calendar.YEAR] += calendar[Calendar.YEAR]
    return this
}