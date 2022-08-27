package com.femi.billreminder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.femi.billreminder.database.dao.BillDao
import com.femi.billreminder.database.entity.Bill
import com.femi.billreminder.utils.RoomConverters

@Database(entities = [Bill::class], version = 2, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class BillDatabase : RoomDatabase() {

    abstract fun billDao(): BillDao

    companion object {
        private const val databaseName = "billreminder.db"

        private var instance: BillDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            BillDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration()
            .build()
    }

}