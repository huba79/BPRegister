package com.example.bpregister

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.bpregister.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var blodPressureDataset :BPItem
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

        val pickedDateView = binding.selectedDateView
        pickedDateView.text = "$year/${month+1}/$day"

        val pickedTimeView = binding.selectedTimeView
        pickedTimeView.text = selectedDate.format(DateTimeFormatter.ofPattern("HH-mm"))

        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { view, pYear, pMonth, pDay ->
                    run {
                        Log.i("picker", "The selected date is: $pYear/${pMonth+1}/$pDay")
                        pickedDateView.text = "$pYear/${pMonth+1}/$pDay"

                        var sMonth =  "${pMonth+ 1}"
                        if((pMonth)<9) {sMonth = "0${pMonth+1}" }

                        var sDay = "${pDay}"
                        if((pDay)<9) {sDay = "0${pDay}" }

                        selectedDate = LocalDateTime.parse("$pYear/$sMonth/${sDay} 00:00",DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))

                        BPItem( Integer.parseInt(binding.sistolicEdit.text.toString()),
                            Integer.parseInt(binding.diastholicEdit.text.toString()),
                            selectedDate
                        ).also { blodPressureDataset = it }
                        Log.i("picker", "The date to save is: $pYear/${sMonth}/$sDay")
                    }
                }, year, month, day
            )
            pickerDialog.show()
        }
        timePickerButton.setOnClickListener{
            //TODO implementr timePicker
        }
        val saveButton = binding.saveButton

        saveButton.setOnClickListener{
            if(formDataIsValid()) {
                blodPressureDataset = BPItem( Integer.parseInt(binding.sistolicEdit.text.toString()),
                    Integer.parseInt(binding.diastholicEdit.text.toString()),
                    selectedDate)
                //TODO: save the registered data
                repo.writeToFile(this@MainActivity,blodPressureDataset)
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