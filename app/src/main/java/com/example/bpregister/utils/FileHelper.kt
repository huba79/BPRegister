package com.example.bpregister.utils

import android.util.Log
import com.example.bpregister.domain.BloodPressureReading
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException

object FileHelper {
    private val file: File = File.createTempFile("myBloodPressureReadings.csv", ".csv")
    private val fileWriter: FileWriter = FileWriter(file)
    fun createFileFromList(data: List<BloodPressureReading>?): File? {
        try {
            if(file.exists()){
                Log.d("FileHelper","File exists")
                if(data == null) {
                    Log.d("FileHelper","Input List is null")
                    fileWriter.use {
                        it.write("")
                    }
                } else {
                    Log.d("FileHelper","Input List not null")
//                    fileWriter.use {
//                        it.write("")
//                        Log.d("FileHelper","File cleared")
//                    }
                    var counter = 0
                    Log.d("FileHelper","List size: ${data.size}")

                    for(reading: BloodPressureReading in data) {
                        if (counter++==0 ) {
                            fileWriter.append(reading.toFormattedString())
                        } else{
                            fileWriter.append(",\n${reading.toFormattedString()}")
                        }
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
    }

}


