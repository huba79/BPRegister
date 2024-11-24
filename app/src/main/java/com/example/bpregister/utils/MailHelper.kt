package com.example.bpregister.utils

import android.content.Context
import com.example.bpregister.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object MailHelper {
    private val recipient ="ibolyahanga@gmail.com"
    private val senderName = "Tánczos Huba"
    private val recipientName = "Doktornő"

    //TODO : apply strings according to the existence of date constraints
    fun getBody(context: Context, dateFrom :LocalDate?, dateTo:LocalDate?):String{
        val from = if(dateFrom !=null){
            dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"
        val to = if(dateTo !=null){
            dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"

        return context.resources.getString(R.string.message_default, recipientName,from,to, senderName)
    }

    fun getSubject(context: Context, dateFrom :LocalDate?, dateTo:LocalDate?):String{
        val from = if(dateFrom !=null){
            dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"
        val to = if(dateTo !=null){
            dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"

        return context.resources.getString(R.string.subject_default,from,to, senderName)
    }

    fun getRecipient():String{
        return recipient
    }
}