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
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
                        dateFromButton.text = toDisplayableDate(pYear,pMonth,pDay)
                        criteria.dateFrom=captionToDate(toDisplayableDate(pYear,pMonth,pDay))
                    }
                }, defaultFromYear, defaultFromMonth, defaultFromDay
            )
            pickerDialog.show()
        }

        binding.selectDateToButton.setOnClickListener {
            val pickerDialog = DatePickerDialog(this,
                { _, pYear, pMonth, pDay ->
                    run {
                        dateToButton.text = toDisplayableDate(pYear,pMonth,pDay)
                        criteria.dateTo=captionToDate(toDisplayableDate(pYear,pMonth,pDay))
                    }
                }, defaultToYear, defaultToMonth, defaultToDay
            )
            pickerDialog.show()
        }

        binding.doFilterButton.setOnClickListener {
            results = BPRepository.readFromFile(applicationContext)
            if(criteria.dateFrom!=null && criteria.dateTo!=null
                && criteria.dateFrom as LocalDateTime >criteria.dateTo as LocalDateTime) {
                invertCriteria(criteria)
            }
            val filteredResults = BPRepository.filterResults(results, criteria)
            val intent = Intent(this@FilterActivity,ResultList::class.java)
            intent.putExtra("results", filteredResults as Serializable?)
        }

//        setSupportActionBar(binding.toolbar)

    }

    private fun captionToDate(text:String):LocalDateTime?{
        return try{
            LocalDateTime.parse(text,DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e:Exception){
            null
        }
    }

    private fun invertCriteria(criteria:Criteria):Criteria {
        val temp = criteria.dateFrom
        criteria.dateFrom=criteria.dateTo
        criteria.dateTo = temp
        return criteria
    }
    private fun toDisplayableDate(year:Int,month:Int,day:Int):String{
        var sMonth =  "${month+ 1}"
        var sDay = "$day"

        if((month)<9) {sMonth = "0${month+1}" }
        if((day)<10) {sDay = "0${day}" }

        return "${year}-${sMonth}-${sDay}"
    }

    private fun toLocalDateTime(year:Int, month:Int,day:Int):LocalDateTime{
        return  LocalDateTime.of(year, month, day, 0,0,0)
    }
}