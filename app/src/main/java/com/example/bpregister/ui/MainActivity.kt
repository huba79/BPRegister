package com.example.bpregister.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.bpregister.BPRepository
import com.example.bpregister.R
import com.example.bpregister.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var blodPressureDataset : BPItem
    val repo = BPRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)
        val datePickerButton = binding.datePickerButton
        val timePickerButton = binding.timePickerButton

        val calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            var minute = calendar.get(Calendar.MINUTE)
            val timeformat24h = true
            lateinit var sDate:String
            lateinit var sTime:String


        val pickedDateView = binding.selectedDateView
        val pickedTimeView = binding.selectedTimeView
        pickedDateView.text="YYYY / MM / DD"
        pickedTimeView.text = "HH:MM"

        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { view, pYear, pMonth, pDay ->
                    run {
                        Log.i("picker", "The selected date is: $pYear/${pMonth+1}/$pDay")
                        pickedDateView.text = "$pYear/${pMonth+1}/$pDay"
                        sDate = "$pYear-${if(pMonth<9) "0${pMonth+1}" else "${pMonth+1}"}-${if(pDay<9) "0$pDay" else "$pDay"}"
                        pickedDateView.text = sDate
                    }
                }, year, month, day
            )
            pickerDialog.show()
        }
        timePickerButton.setOnClickListener{
        val timePickerDialog = TimePickerDialog(this@MainActivity,
            {view,pHour,pMinute-> run{
                pickedTimeView.text="$pHour:$pMinute"
                hour = pHour
                minute=pMinute
                sTime="${if(pHour<9) "0$pHour"  else "$pHour"}:${if(pMinute<9) "0$pMinute"  else "$pMinute"}"
                pickedTimeView.text=sTime
            }}
            ,hour, minute,timeformat24h)
            timePickerDialog.show()
        }

        val saveButton = binding.saveButton
        saveButton.setOnClickListener{
            if(formDataIsValid()) {
                blodPressureDataset = BPItem( Integer.parseInt(binding.sistolicEdit.text.toString()),
                    Integer.parseInt(binding.diastholicEdit.text.toString()),
                    LocalDateTime.parse("$sDate $sTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                try{
                    BPRepository.writeToFile(this@MainActivity, blodPressureDataset)
                    Toast
                        .makeText(this@MainActivity,
                            getString(R.string.blood_pressure_values_saved_successfully),Toast.LENGTH_SHORT)
                        .show()
                } catch(e:Exception) {
                    Toast
                        .makeText(this@MainActivity,
                            getString(R.string.error_saving_data),Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this@MainActivity,"Please enter your blood pressure data!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun formDataIsValid(): Boolean {
        return !(binding.sistolicEdit.text.isBlank()||binding.diastholicEdit.text.isBlank()||binding.selectedDateView.text.isBlank())
    }
}