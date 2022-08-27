package com.femi.billreminder.utils

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.sql.Date

object RoomConverters {

    @TypeConverter
    @JvmStatic
    fun bigDecimalToString(value: BigDecimal): String = value.toString()

    @TypeConverter
    @JvmStatic
    fun stringToBigDecimal(value: String): BigDecimal = value.toBigDecimal()

    @TypeConverter
    @JvmStatic
    fun longToDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }
}