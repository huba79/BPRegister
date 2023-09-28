package com.example.bpregister

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.example.bpregister.databinding.ActivityMainBinding
import com.example.bpregister.databinding.CardFilterBinding
import com.example.bpregister.domain.BPItem
import com.example.bpregister.domain.BPRepository
import com.example.bpregister.domain.Criteria
import com.example.bpregister.ui.ResultListActivity
import com.example.bpregister.utils.DateUtils
import com.example.bpregister.utils.ScreenProps
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : Activity() {
    private lateinit var layoutBinding : ActivityMainBinding
    private lateinit var filterBinding: CardFilterBinding
    private lateinit var blodPressureDataset : BPItem
    private val repo = BPRepository
    private  var results:ArrayList<BPItem> = ArrayList()
    val criteria = Criteria(null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        layoutBinding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(layoutBinding.root)

        val datePickerButton = layoutBinding.datePickerButton
        val timePickerButton = layoutBinding.timePickerButton

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var selectedDate = LocalDateTime.now()
        var selectedTime = LocalTime.now()

        datePickerButton.text = getString(R.string.date_visual_formatter, year, month + 1, day)
        timePickerButton.text = selectedDate.format(DateTimeFormatter.ofPattern("HH:mm"))

        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(
                this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        datePickerButton.text = DateUtils.toDisplayableDate(pYear, pMonth + 1, pDay)
                        selectedDate = LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)
                        Log.d("date", "date: $selectedDate was selected...")
                    }
                }, year, month, day
            )
            pickerDialog.show()
        }
        timePickerButton.setOnClickListener {

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val mTimePicker = TimePickerDialog(
                this, ScreenProps.getDialogThemeAdvice(resources),
                { _,
                  pHour,
                  pMinute ->
                    run {
                        timePickerButton.text =
                            getString(R.string.time_visual_formatter, pHour, pMinute)
                        selectedTime = LocalTime.of(pHour, pMinute)
                        Log.d("date", "time: ${selectedTime.toString()} was selected...")
                    }
                },
                hour,
                minute,
                true
            )
            mTimePicker.show()
        }

        layoutBinding.saveButton.setOnClickListener {
            if (formDataIsValid()) {
                blodPressureDataset = BPItem(
                    Integer.parseInt(layoutBinding.sistholicEdit.text.toString()),
                    Integer.parseInt(layoutBinding.diastholicEdit.text.toString()),
                    selectedDate, selectedTime
                )
                repo.writeToFile(this@MainActivity, blodPressureDataset)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.blood_pressure_values_saved_successfully), Toast.LENGTH_SHORT
                ).show()
                resetInputBpData()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter your blood pressure data!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        layoutBinding.exitButton.setOnClickListener {
            finish()
        }

        layoutBinding.searchButton.setOnClickListener {

            filterBinding = CardFilterBinding.inflate(LayoutInflater.from(this@MainActivity))

            val searchCriteria = Criteria(null, null)

            val popupWidth = LayoutParams.MATCH_PARENT
            val popupHeight = LayoutParams.WRAP_CONTENT
            val filterPopup = PopupWindow(filterBinding.root.rootView, popupWidth, popupHeight).also {
                it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.popup_borders,this@MainActivity.theme))
                it.showAtLocation(layoutBinding.root, 1, 0, filterBinding.root.height / 2)
                it.dimBehind(filterBinding.root)
            }

            val searchDateFromButton = filterBinding.selectDateFromButton
            val searchDateToButton = filterBinding.selectDateToButton
            val searchFilterButton = filterBinding.doFilterButton
            val searchCancelButton = filterBinding.searchCancelButton

            searchDateFromButton.text = resources.getString(R.string.label_date_from)
            searchDateToButton.text = resources.getString(R.string.label_date_to)
            searchDateFromButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(
                this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        searchDateFromButton.text =
                            DateUtils.toDisplayableDate(pYear, pMonth + 1, pDay)
                        searchCriteria.dateFrom = LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)
                    }
                }, year, month, day)
            pickerDialog.show()
        }

            searchDateToButton.setOnClickListener {
                val pickerDialog = DatePickerDialog(
                    this, ScreenProps.getDialogThemeAdvice(resources),
                    { _, pYear, pMonth, pDay ->
                        run {
                            searchDateToButton.text = DateUtils.toDisplayableDate(pYear, pMonth + 1, pDay)
                            searchCriteria.dateTo = LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)
                        }
                    }, year, month, day)
                pickerDialog.show()
            }

            searchFilterButton.setOnClickListener {

                criteria.normalize()
                val filteredResults = BPRepository.filterResults(BPRepository.readFromFile(applicationContext), criteria)
                Log.d("results", "unfiltered: $results")
                Log.d("results", "criteria: $criteria")
                Log.d("results", "Filtered, before sending...$filteredResults")

                val intent = Intent(this@MainActivity, ResultListActivity::class.java)
                intent.putExtra("dimens", filteredResults.size)
                intent.putExtra("results", filteredResults)
                intent.putExtra("dateFrom", criteria.dateFrom)
                intent.putExtra("dateTo", criteria.dateTo)

                filterPopup.dismiss()
                startActivity(intent)
            }

            searchCancelButton.setOnClickListener { filterPopup.dismiss() }

            filterPopup.showAtLocation(layoutBinding.root, 1, 0, filterBinding.root.height / 2)
            filterPopup.dimBehind(filterBinding.root)
        }

    }

    private fun formDataIsValid(): Boolean {
        return !(layoutBinding.sistholicEdit.text.isBlank()
                ||layoutBinding.diastholicEdit.text.isBlank()
                ||layoutBinding.datePickerButton.text.isBlank()
                ||layoutBinding.timePickerButton.text.isBlank())
    }
    private fun resetInputBpData(){
        val calendar = Calendar.getInstance()
        layoutBinding.sistholicEdit.text.clear()
        layoutBinding.diastholicEdit.text.clear()
        layoutBinding.datePickerButton.text= getString(R.string.date_visual_formatter, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+ 1, calendar.get(Calendar.DAY_OF_MONTH)) //getText(R.string.date_picker_button_caption)
        layoutBinding.timePickerButton.text=getString(R.string.time_visual_formatter,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE))
    }
    private fun PopupWindow.dimBehind(contentView:View) {

        val context = contentView.rootView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val popupLayoutParams = contentView.rootView.layoutParams as WindowManager.LayoutParams

        popupLayoutParams.flags = popupLayoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        popupLayoutParams.dimAmount = 0.4f
        wm.updateViewLayout(contentView.rootView, popupLayoutParams)
    }

}