package com.example.bpregister.room

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String? {
        return date?.let{
            it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm")) }
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.let{
            it.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
}