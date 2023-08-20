package com.example.bpregister.utils

import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun toDisplayableDate(year:Int,month:Int,day:Int):String{
        var sMonth =  "${month+ 1}"
        var sDay = "$day"

        if((month)<9) {sMonth = "0${month+1}" }
        if((day)<10) {sDay = "0${day}" }

        return "${year}-${sMonth}-${sDay}"
    }

    fun toLocalDateTime(year:Int, month:Int,day:Int): LocalDateTime {
        return  LocalDateTime.of(year, month, day, 0,0,0)
    }

    fun captionToDate(text:String):LocalDateTime?{
        return try{
            LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e:Exception){
            Log.d("dateConverter","$text should not be null:")
            e.printStackTrace()
            null
        }
    }

    fun captionToTime(text:String): LocalTime? {
        return try {
            LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"))
        } catch(e:Exception) {
            return null
        }
    }
}