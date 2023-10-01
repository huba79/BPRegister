package com.example.bpregister.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bpregister.domain.BPEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

@Database(entities = [BPEntity::class],version=1)
@TypeConverters(Converters::class)
abstract class BpDatabase: RoomDatabase() {
    abstract fun getBpItemDao(): BPItemDao
    //create a singleton instance
    companion object {
        private var INSTANCE:BpDatabase? = null
        fun getDatabase(context: Context, scope:CoroutineScope):BpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =  Room.databaseBuilder(context, BpDatabase::class.java, "bp_database")
                    .addCallback(BpDatabaseCallback(scope))
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }

    private class BpDatabaseCallback(private val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db:SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let{database->
                scope.launch {
                    //sample data insert
                    database.getBpItemDao().addItem(BPEntity(130,80,
                        date = LocalDateTime.of(2023,7,5,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(120,80,
                        date = LocalDateTime.of(2023,7,10,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(130,85,
                        date = LocalDateTime.of(2023,7,15,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(135,85,
                        date = LocalDateTime.of(2023,7,20,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(120,75,
                        date = LocalDateTime.of(2023,7,25,0,0),
                        LocalTime.of(10,0)))

                    database.getBpItemDao().addItem(BPEntity(120,80,
                        date = LocalDateTime.of(2023,8,5,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(125,80,
                        date = LocalDateTime.of(2023,8,10,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(130,85,
                        date = LocalDateTime.of(2023,8,15,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(125,85,
                        date = LocalDateTime.of(2023,8,20,0,0),
                        LocalTime.of(10,0)))
                    database.getBpItemDao().addItem(BPEntity(135,90,
                        date = LocalDateTime.of(2023,8,25,0,0),
                        LocalTime.of(10,0)))
                }

            }
        }
    }
}