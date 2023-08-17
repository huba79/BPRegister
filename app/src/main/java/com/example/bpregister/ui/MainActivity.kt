package com.example.bpregister

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.bpregister.databinding.ActivityMainBinding
import com.example.bpregister.ui.BPItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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

        var selectedDate = LocalDateTime.now()
        var selectedTime = LocalTime.now()

        datePickerButton.text = "$year/${month+1}/$day"

        timePickerButton.text = selectedDate.format(DateTimeFormatter.ofPattern("HH : mm"))

        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { view, pYear, pMonth, pDay ->
                    run {
                        Log.i("picker", "The selected date is: $pYear-${pMonth+1}-$pDay")
                        datePickerButton.text = "$pYear-${pMonth+1}-$pDay"

                        var sMonth =  "${pMonth+ 1}"
                        if((pMonth)<9) {sMonth = "0${pMonth+1}" }

                        var sDay = "${pDay}"
                        if((pDay)<9) {sDay = "0${pDay}" }

                        selectedDate = LocalDateTime.parse("$pYear-$sMonth-${sDay} 00:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        Log.i("picker", "The date to save is: $pYear-${sMonth}-$sDay")
                    }
                }, year, month, day
            )
            pickerDialog.show()
        }

        timePickerButton.setOnClickListener{
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            val mTimePicker = TimePickerDialog(this,
                { _,
                  pHour,
                  pMinute -> {
                    timePickerButton.text = getString(R.string.selected_time, pHour, pMinute)
                    selectedTime=LocalTime.of(pHour,pMinute)
                  }
                },
                hour,
                minute,
                true)
            mTimePicker.show()
        }

        val saveButton = binding.saveButton

        saveButton.setOnClickListener{
            if(formDataIsValid()) {
                blodPressureDataset = BPItem( Integer.parseInt(binding.sistolicEdit.text.toString()),
                    Integer.parseInt(binding.diastholicEdit.text.toString()),
                    selectedDate, selectedTime)
                repo.writeToFile(this@MainActivity,blodPressureDataset)
                Toast.makeText(this@MainActivity,
                    getString(R.string.blood_pressure_values_saved_successfully), Toast.LENGTH_SHORT).show()
                resetCardData()
            } else {
                Toast.makeText(this@MainActivity,"Please enter your blood pressure data!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun formDataIsValid(): Boolean {
        return !(binding.sistolicEdit.text.isBlank()
                ||binding.diastholicEdit.text.isBlank()
                ||binding.datePickerButton.text.isBlank()
                ||binding.timePickerButton.text.isBlank())
    }

    private fun resetCardData(){
        binding.sistolicEdit.text.clear()
        binding.diastholicEdit.text.clear()
        binding.datePickerButton.text=getText(R.string.date_picker_button_text)
        binding.timePickerButton.text=getText(R.string.time_picker_button)
    }
}