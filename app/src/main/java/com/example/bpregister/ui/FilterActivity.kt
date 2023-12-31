package com.example.bpregister.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import com.example.bpregister.R
import com.example.bpregister.databinding.ActivityFilterBinding
import com.example.bpregister.domain.deprecated.BPItem
import com.example.bpregister.domain.deprecated.BPRepository
import com.example.bpregister.domain.Criteria
import com.example.bpregister.utils.DateUtils
import com.example.bpregister.utils.ScreenProps
import java.time.LocalDateTime

class FilterActivity : Activity() {

    private lateinit var binding: ActivityFilterBinding
    private var fromDate:LocalDateTime? = null
    private var toDate:LocalDateTime? = null
    private  var results:ArrayList<BPItem> = ArrayList()
    val criteria = Criteria(null,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateFromButton =binding.selectDateFromButton
        val dateToButton = binding.selectDateToButton

        val calendar= Calendar.getInstance()

        val defaultFromYear = calendar.get(Calendar.YEAR)
        val defaultToYear = calendar.get(Calendar.YEAR)

        val defaultFromMonth = calendar.get(Calendar.MONTH)
        val defaultToMonth = calendar.get(Calendar.MONTH)

        val defaultFromDay = calendar.get(Calendar.DAY_OF_MONTH)
        val defaultToDay = calendar.get(Calendar.DAY_OF_MONTH)

        dateFromButton.text= getString(R.string.date_picker_from_caption)
        dateToButton.text= getString(R.string.date_picker_to_caption)

        Log.d("date","defaults set up")

        binding.selectDateFromButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        dateFromButton.text = DateUtils.toDisplayableDate(pYear,pMonth+1,pDay)
                        criteria.dateFrom=LocalDateTime.of(pYear,pMonth+1,pDay,0,0)
                    }
                }, defaultFromYear, defaultFromMonth, defaultFromDay
            )
            pickerDialog.show()
        }

        binding.selectDateToButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this, ScreenProps.getDialogThemeAdvice(resources),
                { _, pYear, pMonth, pDay ->
                    run {
                        dateToButton.text = DateUtils.toDisplayableDate(pYear,pMonth+1,pDay)
                        criteria.dateTo=LocalDateTime.of(pYear,pMonth+1,pDay,0,0)
                    }
                }, defaultToYear, defaultToMonth, defaultToDay
            )
            pickerDialog.show()
        }

        binding.doFilterButton.setOnClickListener {
            results = BPRepository.readAllFromFile(applicationContext)

            if((criteria.dateFrom!=null && criteria.dateTo!=null)) {
                if(criteria.dateFrom!!.isAfter(criteria.dateTo!!)   ) {
                    Log.d("date","normalizing criteria...")
                    criteria.normalize()
                }
            }

            val filteredResults = BPRepository.filterResults(results, criteria)
            Log.d("results","unfiltered: $results")
            Log.d("results","criteria: $criteria")
            Log.d("results","Filtered, before sending...$filteredResults")
            val intent = Intent(this@FilterActivity,ResultListActivity::class.java)
            intent.putExtra("dimens",filteredResults.size)
            intent.putExtra("results", filteredResults)
            intent.putExtra("dateFrom", criteria.dateFrom)
            intent.putExtra("dateTo", criteria.dateTo)
            startActivity(intent)
        }

//        setSupportActionBar(binding.toolbar)

    }

    override fun onStart() {
        super.onStart()
        binding.selectDateFromButton.text= getString(R.string.date_picker_from_caption)
        binding.selectDateToButton.text= getString(R.string.date_picker_to_caption)
        fromDate = null
        toDate= null
        criteria.clear()
    }
}