package com.example.bpregister.utils

import com.example.bpregister.domain.BloodPressureReading
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException

object FileHelper {
    private val file: File = File.createTempFile("myBloodPressureReadings.csv", ".csv")
    private val fileWriter: FileWriter = FileWriter(file)
    fun createAttachment(data: List<BloodPressureReading>?): File? {
        try {


            if(file.exists()){
                if(data == null) {
                    fileWriter.use {
                        it.write("")
                    }
                } else {
                    fileWriter.use {
                        it.write("")
                    }
                    for(reading: BloodPressureReading in data!!) {
                        FileWriter(file).append("/n$reading")
                    }
                }
                fileWriter.close()
                return file
            } else throw FileNotFoundException("Something went Wrong.File not Found!")

        }
        catch (e: IOException){
            e.printStackTrace()
            fileWriter.close()
            return null
        }
        catch(e:FileNotFoundException){
            e.printStackTrace()
            fileWriter.close()
            return null
        }
        return null
    }

}


