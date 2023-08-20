package com.example.bpregister.domain

import java.time.LocalDateTime

data class Criteria(var dateFrom: LocalDateTime?, var dateTo:LocalDateTime?) {
    fun normalize():Criteria {
        try {
            if (dateFrom!!>dateTo!!){
                val temp = this.dateFrom
                this.dateFrom=this.dateTo
                this.dateTo = temp
            }
        } catch (e: Exception) {
            return this
        }
        return this
    }
}