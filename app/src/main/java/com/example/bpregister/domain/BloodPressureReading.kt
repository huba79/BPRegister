package com.example.bpregister.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "bp_items")
data class BloodPressureReading (var sistholic:Int,
                                 var diastholic:Int,
                                 var date: LocalDate,
                                 var time:LocalTime):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    override fun toString():String{
        return "$sistholic/$diastholic/${date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}/${time.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    }


}
