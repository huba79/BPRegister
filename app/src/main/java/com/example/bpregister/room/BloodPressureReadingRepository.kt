package com.example.bpregister.room

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.bpregister.domain.BloodPressureReading
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class BloodPressureReadingRepository(private val dao: BloodPressureReadingDao) {

//    lateinit var filteredRecords: Flow<List<BPEntity>>

    @WorkerThread
    fun getFiltered(dateFrom:LocalDate?, dateTo:LocalDate?): Flow<List<BloodPressureReading>> {
        Log.d("BloodPressureReadingRepository","Accessing repo with \"criteria\" params: date from: $dateFrom, date to: $dateTo " )
        //TODO solve repo filtering by passing Criteria to DAO level (null param handling!)
        return dao.getBloodPressureReadingsByDateRange(dateFrom,dateTo)
    }

    @WorkerThread
    fun getAll(): Flow<List<BloodPressureReading>>{
        Log.d("BloodPressureReadingRepository","Accessing repo get with \"all\" params " )
        return dao.getAllBloodPressureReadings()
    }

    @WorkerThread
    fun getById(id:Int):LiveData<BloodPressureReading> {
        Log.d("BloodPressureReadingRepository","Accessing repo get with \"id\" param: $id " )
        return dao.getBloodPressureReadingById(id)
    }
    @WorkerThread
    suspend fun insert(item:BloodPressureReading){
        Log.d("BloodPressureReadingRepository","Inserting into repo with \"item\" param: $item " )
        dao.insertBloodPressureReading(item)
    }
//    @WorkerThread
//    suspend fun delete(item:BPEntity){
//        dao.deleteItem(item)
//    }
}