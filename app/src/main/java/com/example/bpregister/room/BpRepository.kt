package com.example.bpregister.room

import androidx.annotation.WorkerThread
import com.example.bpregister.domain.BPEntity
import kotlinx.coroutines.flow.Flow

class BpRepository(private val dao:BPItemDao) {
    private lateinit var allBpRecords: Flow<List<BPEntity>>
    @WorkerThread
    suspend fun getAll(){
        allBpRecords = dao.getAllBpItems()
    }
    @WorkerThread
    suspend fun getById(id:Int){
        dao.getBpItemById(id)
    }
    @WorkerThread
    suspend fun insert(item:BPEntity){
        dao.addItem(item)
    }
    @WorkerThread
    suspend fun delete(item:BPEntity){
        dao.deleteItem(item)
    }
}