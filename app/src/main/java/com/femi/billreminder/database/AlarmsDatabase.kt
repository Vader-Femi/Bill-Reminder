package com.femi.billreminder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.femi.billreminder.database.dao.AlarmsDAO
import com.femi.billreminder.database.dao.BillDao
import com.femi.billreminder.database.entity.Alarms
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.utils.RoomConverters

@Database(entities = [Alarms::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AlarmsDatabase : RoomDatabase() {

    abstract fun alarmsDao(): AlarmsDAO

    companion object {
        private const val databaseName = "billreminder.db.alarms"

        private var instance: AlarmsDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            AlarmsDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

}