package com.example.bpregister

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BPRepository{
//    lateinit var output: FileOutputStream
    lateinit var input: FileInputStream
    lateinit var bpdatabase:File

    fun writeToFile(context:Context,data:BPItem){
        val folder = context.filesDir
        bpdatabase=File(folder,"bpDatabase.txt")
        if(!bpdatabase.exists()) {
            bpdatabase.createNewFile()
        }
        bpdatabase.appendText(data.toString())
        bpdatabase.appendText("\n")
    }

    fun readFromFile():ArrayList<BPItem>  {
        input = bpdatabase.inputStream()
        val buffer = input.bufferedReader()
        try {
            val lines = buffer.readLines()
            var  bpItems:ArrayList<BPItem> = ArrayList()

            var readData:BPItem

            for (line in lines){
                val properties = line.split("/")
                readData = BPItem(Integer.parseInt(properties[0]),Integer.parseInt(properties[1]),
                    LocalDateTime.parse(properties[2], DateTimeFormatter.ofPattern("yyyy-MM-DD H-M-S")))
                Log.d("readFromFile",readData.toString())
                bpItems.add(readData)
            }
            return bpItems
        } catch (e: Exception) {
            TODO("Not yet implemented")
        } finally {
            input.close()
        }
    }

    fun filterResults(list:ArrayList<BPItem>, criteria:Criteria):ArrayList<BPItem>{

        if(criteria.dateFrom!=null&&criteria.dateTo!=null) {
            for(item in list){
                if(!(item.timestamp>=criteria.dateFrom&&item.timestamp<=criteria.dateTo)){
                    list.remove(item)
                }
            }
            return list
        }
        if(criteria.dateFrom!=null&&criteria.dateTo==null) {
            for(item in list){
                if(item.timestamp<criteria.dateFrom){
                    list.remove(item)
                }
            }
            return list
        }

        if(criteria.dateFrom==null&&criteria.dateTo!=null) {
            for(item in list){
                if(item.timestamp>criteria.dateTo){
                    list.remove(item)
                }
            }
            return list
        }

        return list

    }

}