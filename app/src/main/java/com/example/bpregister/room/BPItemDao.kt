package com.example.bpregister.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bpregister.domain.BPEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface BPItemDao {
    @Insert
    suspend fun addItem(entity: BPEntity)
//    @Delete
//    suspend fun deleteItem(entity: BPEntity)
    @Query("select * from bp_items where id = :id order by id asc")
    suspend fun getBpItemById(id:Int):Flow<BPEntity>
    @Query("select * from bp_items order by id asc")
    suspend fun getAllBpItems(): Flow<List<BPEntity>>
    @Query("select * from bp_items where localDate>= :dateFrom and localDate<= :dateTo order by localDate, localTime")
    suspend fun getBpItemsFilteredByDateTime(dateFrom:LocalDateTime?, dateTo:LocalDateTime?): Flow<List<BPEntity>>

}