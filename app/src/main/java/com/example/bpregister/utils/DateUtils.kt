package com.example.bpregister.utils

object DateUtils {
    fun toDisplayableDate(year:Int,month:Int,day:Int):String{
        var sMonth =  "$month"
        var sDay = "$day"

        if((month)<9) {sMonth = "0$month" }
        if((day)<10) {sDay = "0$day" }

        return "${year}-${sMonth}-${sDay}"
    }
    fun toDisplayableTime(hour:Int,minute:Int):String{
        var sHour =  "$hour"
        var sMinute = "$minute"
        if( hour <9) { sHour = "0$hour"}
        if( minute <9 ) { sMinute = "0$minute"}

        return "$sHour : $sMinute"
    }
//    fun toLocalDateTime(year:Int, month:Int,day:Int): LocalDateTime {
//        return  LocalDateTime.of(year, month, day, 0,0,0)
//    }

//    fun captionToDate(text:String):LocalDateTime?{
//        return try{
//            LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        } catch (e:Exception){
//            Log.d("dateConverter","$text should not be null:")
//            e.printStackTrace()
//            null
//        }
//    }

//    fun captionToTime(text:String): LocalTime? {
//        return try {
//            LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm"))
//        } catch(e:Exception) {
//            return null
//        }
//    }
}