package com.example.bpregister.domain

import android.util.Log
import java.io.Serializable
import java.time.LocalDate

data class Criteria(var dateFrom: LocalDate?, var dateTo:LocalDate?):Serializable {

    fun normalize():Criteria {
        if ((dateFrom != null && dateTo != null)) {
            if (dateFrom!!.isAfter(dateTo!!)) {
                Log.d("date", "normalizing criteria...")
                try {
                    if (dateFrom!!>dateTo!!){
                        val temp = this.dateFrom
                        this.dateFrom=this.dateTo
                        this.dateTo = temp
                    }
                } catch (e: Exception) {
                    return this
                }
            }
        }
        return this
    }

    fun clear():Criteria {
        dateFrom=null
        dateTo=null
        return this
    }
}