package com.femi.billreminder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.femi.billreminder.database.entity.Alarms

@Dao
interface AlarmsDAO {

    @Query("SELECT * FROM alarms ORDER BY id ASC")
    fun getAlarms(): List<Alarms>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarms: Alarms)

    @Update
    fun update(alarms: Alarms)

    @Delete
    fun deleteAlarm(alarms: Alarms)
}
