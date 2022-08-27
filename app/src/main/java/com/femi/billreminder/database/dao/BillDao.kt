package com.femi.billreminder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.femi.billreminder.database.entity.Bill

@Dao
interface BillDao {
    @Query("SELECT * FROM bills where paid = 'false' ORDER BY date ASC")
    fun getUnpaidBills(): LiveData<List<Bill>>

    @Query("SELECT * FROM bills where paid = 'true' ORDER BY date ASC")
    fun getPaidBills(): LiveData<List<Bill>>

    @Query("SELECT * FROM bills ORDER BY date ASC")
    fun getBills(): LiveData<List<Bill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bill: Bill)

    @Update
    fun update(bill: Bill)

    @Delete
    fun deleteBill(bill: Bill)
}
