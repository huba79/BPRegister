package com.example.bpregister.ui

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BPItem (var sistholic:Int, var diastholic:Int,var timestamp: LocalDateTime){
    override fun toString():String{
        return "$sistholic/$diastholic/${timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm"))}"
    }
}