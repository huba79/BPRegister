package com.example.bpregister.ui.activities

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import com.example.bpregister.R
import com.example.bpregister.databinding.ActivityMainBinding
import com.example.bpregister.databinding.CardFilterBinding
import com.example.bpregister.domain.BloodPressureReading
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BPApplication
import com.example.bpregister.ui.viewmodel.BpViewModel
import com.example.bpregister.ui.viewmodel.BpViewModel.Factory.provideFactory
import com.example.bpregister.utils.DateUtils
import com.example.bpregister.utils.ScreenProps
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var filterBinding: CardFilterBinding
    private lateinit var bpReading : BloodPressureReading
    private var searchCriteria = Criteria(null,null)
    lateinit var bpViewModel: BpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(mainBinding.root)

        bpViewModel = provideFactory((this.application as BPApplication).getRepo()).create(BpViewModel::class.java)

        val datePickerButton = mainBinding.datePickerButton
        val timePickerButton = mainBinding.timePickerButton

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        bpViewModel.setDateOfCurrent(LocalDate.now())
        bpViewModel.setTimeOfCurrent(LocalTime.now())

        datePickerButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(
                this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        datePickerButton.text = DateUtils.toDisplayableDate(pYear, pMonth + 1, pDay)
//                        selectedDate = LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)
                        bpViewModel.setDateOfCurrent(LocalDate.of(pYear, pMonth + 1, pDay))
                        Log.d("date", "date: ${LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)} was selected...")
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
                        bpViewModel.setTimeOfCurrent(LocalTime.of(pHour, pMinute))
                        Log.d("date", "time: ${bpViewModel.getCurrent()!!.time.toString()} was selected...")
                    }
                },
                hour,
                minute,
                true
            )
            mTimePicker.show()
        }

        mainBinding.saveButton.setOnClickListener {
            if (formDataIsValid()) {
                bpReading = BloodPressureReading(
                    Integer.parseInt(mainBinding.sistholicEdit.text.toString()),
                    Integer.parseInt(mainBinding.diastholicEdit.text.toString()),
                    bpViewModel.getCurrent()!!.date, bpViewModel.getCurrent()!!.time
                )
                bpViewModel.save(bpReading)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.blood_pressure_values_saved_successfully), Toast.LENGTH_SHORT
                ).show()
                resetInputBpData()
            }
            else {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter your blood pressure data!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        mainBinding.exitButton.setOnClickListener {
            finish()
        }

        mainBinding.searchButton.setOnClickListener {

            filterBinding = CardFilterBinding.inflate(LayoutInflater.from(this@MainActivity))

            val popupWidth = LayoutParams.MATCH_PARENT
            val popupHeight = LayoutParams.WRAP_CONTENT
            val filterPopup = PopupWindow(filterBinding.root.rootView, popupWidth, popupHeight).also {
                it.setBackgroundDrawable(ResourcesCompat.getDrawable(resources,
                    R.drawable.popup_borders,this@MainActivity.theme))
                it.showAtLocation(mainBinding.root, 1, 0, filterBinding.root.height / 2)
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
                        searchDateFromButton.text = DateUtils.toDisplayableDate(pYear, pMonth + 1, pDay)
                        bpViewModel.currentCriteria.dateFrom=LocalDate.of(pYear, pMonth + 1, pDay)
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
//                            searchCriteria.dateTo = LocalDateTime.of(pYear, pMonth + 1, pDay, 0, 0)
                            bpViewModel.currentCriteria.dateTo = LocalDate.of(pYear, pMonth + 1, pDay)
                        }
                    }, year, month, day)
                pickerDialog.show()
            }

            searchFilterButton.setOnClickListener {
                Log.d("criteria", "saved criteria: ${bpViewModel.currentCriteria}")
                bpViewModel.currentCriteria.normalize()
                Log.d("criteria", "normalized criteria: ${bpViewModel.currentCriteria}")

                val intent = Intent(this@MainActivity, ResultListActivity::class.java)
                intent.putExtra("criteria", searchCriteria as Serializable)

                filterPopup.dismiss()
                startActivity(intent)
            }

            searchCancelButton.setOnClickListener { filterPopup.dismiss() }

            filterPopup.showAtLocation(mainBinding.root, 1, 0, filterBinding.root.height / 2)
            filterPopup.dimBehind(filterBinding.root)
        }

    }

    @SuppressLint("SetTextI18n")
    public override fun onResume() {
        super.onResume()
        bpViewModel.currentCriteria.let {
            searchCriteria = bpViewModel.currentCriteria
        }
        bpViewModel.getCurrent()?.let {
            mainBinding.datePickerButton.text =
                DateUtils.toDisplayableDate(bpViewModel.getCurrent()!!.date.year, bpViewModel.getCurrent()!!.date.month.value, bpViewModel.getCurrent()!!.date.dayOfMonth)
            mainBinding.timePickerButton.text =
                DateUtils.toDisplayableTime(bpViewModel.getCurrent()!!.time.hour,bpViewModel.getCurrent()!!.time.minute)
            mainBinding.sistholicEdit.setText(bpViewModel.getCurrent()!!.sistholic.toString())
            mainBinding.diastholicEdit.setText(bpViewModel.getCurrent()!!.diastholic.toString())
        }

    }
    private fun formDataIsValid(): Boolean {
        return !(mainBinding.sistholicEdit.text.isBlank()
                ||mainBinding.diastholicEdit.text.isBlank()
                ||mainBinding.datePickerButton.text.isBlank()
                ||mainBinding.timePickerButton.text.isBlank())
    }
    private fun resetInputBpData(){
        val calendar = Calendar.getInstance()
        mainBinding.sistholicEdit.text.clear()
        mainBinding.diastholicEdit.text.clear()
        mainBinding.datePickerButton.text= getString(R.string.date_visual_formatter, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+ 1, calendar.get(Calendar.DAY_OF_MONTH)) //getText(R.string.date_picker_button_caption)
        mainBinding.timePickerButton.text=getString(R.string.time_visual_formatter,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE))
    }
    private fun PopupWindow.dimBehind(contentView:View) {

        val context = contentView.rootView.context
        val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
        val popupLayoutParams = contentView.rootView.layoutParams as WindowManager.LayoutParams

        popupLayoutParams.flags = popupLayoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        popupLayoutParams.dimAmount = 0.4f
        wm.updateViewLayout(contentView.rootView, popupLayoutParams)
    }

}