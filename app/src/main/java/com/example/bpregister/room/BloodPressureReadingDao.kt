package com.example.bpregister.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bpregister.domain.BloodPressureReading
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
@Dao
interface BloodPressureReadingDao {

    @Insert
    suspend fun insertBloodPressureReading(reading: BloodPressureReading)

    @Delete
    suspend fun deleteBloodPressureReading(reading: BloodPressureReading)

    @Query("DELETE FROM bp_items WHERE id = :id")
    suspend fun deleteBloodPressureReadingById(id: Int)

    @Query("select * from bp_items where id = :id")
    fun getBloodPressureReadingById(id: Int): LiveData<BloodPressureReading>

    @Query("select * from bp_items order by  date, time desc")
    fun getAllBloodPressureReadings(): Flow<List<BloodPressureReading>>

    @Query("select * from bp_items where date >= :dateFrom and date <= :dateTo order by date, time desc")
    fun getBloodPressureReadingsByDateRange(dateFrom: LocalDateTime?, dateTo: LocalDateTime?): Flow<List<BloodPressureReading>>
}