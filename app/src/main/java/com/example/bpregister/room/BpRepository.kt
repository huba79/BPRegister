package com.example.bpregister.room

import androidx.annotation.WorkerThread
import com.example.bpregister.domain.BPEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class BpRepository(private val dao:BPItemDao) {

//    lateinit var filteredRecords: Flow<List<BPEntity>>

    @WorkerThread
    suspend fun getFiltered(dateFrom:LocalDateTime?, dateTo:LocalDateTime?):Flow<List<BPEntity>> {
        //TODO solve repo filtering by passing Criteria to DAO level (null param handling!)
        return dao.getBpItemsFilteredByDateTime(dateFrom,dateTo)
    }

    @WorkerThread
    suspend fun getAll():Flow<List<BPEntity>>{
        return dao.getAllBpItems()
    }

    @WorkerThread
    suspend fun getById(id:Int):Flow<BPEntity> {
        return dao.getBpItemById(id)
    }
    @WorkerThread
    suspend fun insert(item:BPEntity){
        dao.addItem(item)
    }
//    @WorkerThread
//    suspend fun delete(item:BPEntity){
//        dao.deleteItem(item)
//    }
}