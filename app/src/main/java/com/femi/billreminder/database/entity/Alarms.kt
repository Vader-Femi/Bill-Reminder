package com.femi.billreminder.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alarms")
data class Alarms(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "bill")
    val bill: Bill,

    @ColumnInfo(name = "time")
    val time: Long,

) : Serializable