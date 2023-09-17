package com.example.bpregister.domain

import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class BPItem (var sistholic:Int, var diastholic:Int, var localDate: LocalDateTime, var localTime:LocalTime):Serializable{
    override fun toString():String{
        return "$sistholic/$diastholic/${localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}/${localTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    }

}