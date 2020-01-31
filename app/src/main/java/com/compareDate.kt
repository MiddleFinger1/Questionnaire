package com

import java.util.Calendar
import java.util.Calendar.*


infix fun Calendar.compareDate(calendar: Calendar): Int {
    val compareInt = { one: Int, two: Int ->
        if (one > two)
            1
        else if (one < two) -1
        else 0
    }
    val array = arrayOf(SECOND, MINUTE, HOUR, DAY_OF_MONTH, MONTH, YEAR)
    var compare = 0
    for (i in array){
        compare = compareInt(this[i], calendar[i])
        if (compare == 0)
            continue
        else return compare
    }
    return compare
}