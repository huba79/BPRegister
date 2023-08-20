package com.example.bpregister.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bpregister.R
import com.example.bpregister.databinding.ActivityFilterBinding
import com.example.bpregister.domain.BPItem
import com.example.bpregister.domain.BPRepository
import com.example.bpregister.domain.Criteria
import com.example.bpregister.utils.DateUtils
import java.io.Serializable
import java.time.LocalDateTime

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var fromDate:LocalDateTime
    private lateinit var toDate:LocalDateTime
    private  var results:ArrayList<BPItem> = ArrayList()
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

        fromDate=LocalDateTime.of(defaultFromYear, defaultFromMonth,defaultFromDay,0,0,0)
        toDate=LocalDateTime.of(defaultToYear, defaultToMonth,defaultToDay,0,0)

        val criteria = Criteria(null,null)

        dateFromButton.text= getString(R.string.date_picker_from_caption)
        dateToButton.text= getString(R.string.date_picker_to_caption)

        Log.d("date","defaults set up")

        binding.selectDateFromButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { _, pYear, pMonth, pDay ->
                    run {
                        dateFromButton.text = DateUtils.toDisplayableDate(pYear,pMonth,pDay)
                        criteria.dateFrom=LocalDateTime.of(pYear,pMonth,pDay,0,0)
                    }
                }, defaultFromYear, defaultFromMonth, defaultFromDay
            )
            pickerDialog.show()
        }

        binding.selectDateToButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { _, pYear, pMonth, pDay ->
                    run {
                        dateToButton.text = DateUtils.toDisplayableDate(pYear,pMonth,pDay)
                        criteria.dateTo=LocalDateTime.of(pYear,pMonth,pDay,0,0)
                    }
                }, defaultToYear, defaultToMonth, defaultToDay
            )
            pickerDialog.show()
        }

        binding.doFilterButton.setOnClickListener {
            results = BPRepository.readFromFile(applicationContext)
            if(criteria.dateFrom!=null && criteria.dateTo!=null) {
                if(criteria.dateFrom!!.isAfter(criteria.dateTo!!)   ) {
                    Log.d("date","normalizing criteria...")
                    criteria.normalize()
                }

            }
            val filteredResults = BPRepository.filterResults(results, criteria)
            val intent = Intent(this@FilterActivity,ResultListActivity::class.java)
            intent.putExtra("results", filteredResults as Serializable?)
        }

//        setSupportActionBar(binding.toolbar)

    }


}