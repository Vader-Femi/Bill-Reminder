package com.femi.billreminder.utils

import androidx.room.TypeConverter
import com.femi.billreminder.database.entity.Bill
import com.google.gson.Gson
import java.math.BigDecimal
import java.sql.Date

object RoomConverters {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun bigDecimalToString(value: BigDecimal): String = value.toString()

    @TypeConverter
    @JvmStatic
    fun stringToBigDecimal(value: String): BigDecimal = value.toBigDecimal()

    @TypeConverter
    @JvmStatic
    fun longToDate(dateLong: Long?): Date? = dateLong?.let { Date(it) }

    @TypeConverter
    @JvmStatic
    fun dateToLong(date: Date?): Long? = date?.time

    @TypeConverter
    @JvmStatic
    fun longToString(timeLong: Long?): String? = timeLong?.toString()

    @TypeConverter
    @JvmStatic
    fun stringToLong(timeString: String?): Long? = timeString?.toLong()

    @TypeConverter
    @JvmStatic
    fun billToString(bill: Bill): String = gson.toJson(bill)

    @TypeConverter
    @JvmStatic
    fun stringToBill(bill: String): Bill? = gson.fromJson(bill, Bill::class.java)

}