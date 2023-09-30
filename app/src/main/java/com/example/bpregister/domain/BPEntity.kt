package com.example.bpregister.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@Entity(tableName = "bp_items")
data class BPEntity (var sistholic:Int, var diastholic:Int, var localDate: LocalDateTime, var localTime:LocalTime):Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    override fun toString():String{
        return "$sistholic/$diastholic/${localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}/${localTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    }


}