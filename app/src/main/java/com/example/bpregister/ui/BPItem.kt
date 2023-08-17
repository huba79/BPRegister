package com.example.bpregister.ui

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class BPItem (var sistholic:Int, var diastholic:Int, var localDate: LocalDateTime, var time:LocalTime){
    override fun toString():String{
        return "$sistholic/$diastholic/${localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}/${time.format(DateTimeFormatter.ofPattern("HH : mm"))}"
    }
}