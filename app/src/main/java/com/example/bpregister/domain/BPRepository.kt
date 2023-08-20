package com.example.bpregister.domain

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object BPRepository{
//    lateinit var output: FileOutputStream
    lateinit var input: FileInputStream
    lateinit var bpdatabase:File

    fun writeToFile(context:Context,data: BPItem){
        val folder = context.filesDir
        bpdatabase =File(folder,"bpDatabase.txt")
        if(!bpdatabase.exists()) {
            bpdatabase.createNewFile()
        }
        bpdatabase.appendText(data.toString())
        bpdatabase.appendText("\n")
    }

    fun readFromFile(context:Context):ArrayList<BPItem>  {
        val folder = context.filesDir
        bpdatabase =File(folder,"bpDatabase.txt")
        if(!bpdatabase.exists()) {
            bpdatabase.createNewFile()
        }
        Log.d("readFromFile","reading...")
        input = bpdatabase.inputStream()
        val buffer = input.bufferedReader()
        try {
            val lines = buffer.readLines()
            var  bpItems:ArrayList<BPItem> = ArrayList()

            var convertedData: BPItem

            for (line in lines){
                Log.d("line","$line....")
                if(line=="\n") {break}
                val properties = line.split("/")
                val sistholic = Integer.parseInt(properties[0])
                val diastholic = Integer.parseInt(properties[1])
                val date = LocalDate.parse(properties[2],DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val time = LocalTime.parse(properties[3],DateTimeFormatter.ofPattern("HH:mm"))

                convertedData = BPItem(sistholic,diastholic,LocalDateTime.of(date.year,date.month,date.dayOfMonth,0,0),time )
                Log.d("readFromFile",convertedData.toString())
                bpItems.add(convertedData)
            }
            return bpItems
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            input.close()
        }
        return ArrayList<BPItem>()
    }

    fun filterResults(list:ArrayList<BPItem>, criteria: Criteria):ArrayList<BPItem>{

        if(criteria.dateFrom!=null&&criteria.dateTo!=null) {
            for(item in list){
                if(!(item.localDate>=criteria.dateFrom && item.localDate<=criteria.dateTo)){
                    list.remove(item)
                }
            }
            return list
        }
        if(criteria.dateFrom!=null&&criteria.dateTo==null) {
            for(item in list){
                if(item.localDate<criteria.dateFrom){
                    list.remove(item)
                }
            }
            return list
        }

        if(criteria.dateFrom==null&&criteria.dateTo!=null) {
            for(item in list){
                if(item.localDate>criteria.dateTo){
                    list.remove(item)
                }
            }
            return list
        }

        return list

    }

}