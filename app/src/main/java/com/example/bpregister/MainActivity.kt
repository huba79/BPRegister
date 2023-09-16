package com.example.bpregister

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.bpregister.databinding.ActivityMainBinding
import com.example.bpregister.domain.BPItem
import com.example.bpregister.domain.BPRepository
import com.example.bpregister.ui.FilterActivity
import com.example.bpregister.utils.DateUtils
import com.example.bpregister.utils.ScreenProps
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : Activity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var blodPressureDataset : BPItem
    private val repo = BPRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)
        val datePickerButton = binding.datePickerButton
        val timePickerButton = binding.timePickerButton

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)



        var selectedDate = LocalDateTime.now()
        var selectedTime = LocalTime.now()

        datePickerButton.text = getString(R.string.date_visual_formatter, year, month + 1, day)
        timePickerButton.text = selectedDate.format(DateTimeFormatter.ofPattern("HH:mm"))
        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        datePickerButton.text = DateUtils.toDisplayableDate(pYear,pMonth+1,pDay)
                        selectedDate =LocalDateTime.of(pYear,pMonth+1,pDay,0,0)
                        Log.d("date","date: $selectedDate was selected...")
                    }
                }, year, month, day
            )
            pickerDialog.show()
        }

        timePickerButton.setOnClickListener{

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val mTimePicker = TimePickerDialog(this, ScreenProps.getDialogThemeAdvice(resources),
                { _,
                  pHour,
                  pMinute ->
                    run {
                        timePickerButton.text =
                            getString(R.string.time_visual_formatter, pHour, pMinute)
                        selectedTime = LocalTime.of(pHour, pMinute)
                        Log.d("date","time: ${selectedTime.toString()} was selected...")
                    }
                },
                hour,
                minute,
                true)
            mTimePicker.show()
        }

        binding.saveButton.setOnClickListener{
            if(formDataIsValid()) {
                blodPressureDataset = BPItem( Integer.parseInt(binding.sistholicEdit.text.toString()),
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

        binding.searchButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FilterActivity::class.java)
            startActivity(intent)
        }

        binding.exitButton.setOnClickListener{
            finish()
        }

    }

    private fun formDataIsValid(): Boolean {
        return !(binding.sistholicEdit.text.isBlank()
                ||binding.diastholicEdit.text.isBlank()
                ||binding.datePickerButton.text.isBlank()
                ||binding.timePickerButton.text.isBlank())
    }

    private fun resetCardData(){
        val calendar = Calendar.getInstance()
        binding.sistholicEdit.text.clear()
        binding.diastholicEdit.text.clear()
        binding.datePickerButton.text= getString(R.string.date_visual_formatter, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+ 1, calendar.get(Calendar.DAY_OF_MONTH)) //getText(R.string.date_picker_button_caption)
        binding.timePickerButton.text=getString(R.string.time_visual_formatter,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE))
    }

}