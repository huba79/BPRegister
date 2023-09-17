package com.example.bpregister.utils

import android.content.Context
import com.example.bpregister.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MailHelper {
    private val recipient ="ibolyahanga@gmail.com"
    private val senderName = "Tánczos Huba"
    private val recipientName = "Doktornő"
    fun getBody(context: Context, dateFrom :LocalDateTime?, dateTo:LocalDateTime?):String{
        val from = if(dateFrom !=null){dateFrom!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"
        val to = if(dateTo !=null){dateTo!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"

        return context.resources.getString(R.string.default_message, recipientName,from,to, senderName)
    }

    fun getSubject(context: Context, dateFrom :LocalDateTime?, dateTo:LocalDateTime?):String{
        val from = if(dateFrom !=null){dateFrom!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"
        val to = if(dateTo !=null){dateTo!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} else "null"

        return context.resources.getString(R.string.default_subject,from,to, senderName)
    }

    fun getRecipient():String{
        return recipient
    }
}