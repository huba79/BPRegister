package com.example.bpregister.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpregister.databinding.ActivityResultListBinding
import com.example.bpregister.domain.BPComparator
import com.example.bpregister.domain.BPItem
import com.example.bpregister.domain.Criteria
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultListActivity : Activity() {
    private  var dateFrom:LocalDateTime?=null
    private  var dateTo:LocalDateTime? =null
    private lateinit var orderedResults:MutableList<BPItem>
    private lateinit var binding: ActivityResultListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.getSerializableExtra ("results") is ArrayList<*> ) {
            val results = intent.getSerializableExtra ("results") as ArrayList<BPItem>

            results.sortWith( BPComparator() )
            orderedResults=results

            Log.d("results","after sending...$results")

            val adapter = ResultsAdapter(this@ResultListActivity,results)
            binding.resultsRecyclerView.layoutManager=LinearLayoutManager(this@ResultListActivity)
            binding.resultsRecyclerView.adapter=adapter
        } else {
            Toast.makeText(this@ResultListActivity,"Incorrect parameter!",Toast.LENGTH_SHORT).show()
        }

        dateFrom = if (intent.getSerializableExtra("dateFrom") is LocalDateTime) {
            intent.getSerializableExtra("dateFrom") as LocalDateTime
        } else null


        dateTo = if (intent.getSerializableExtra("dateTo") is LocalDateTime) {
            intent.getSerializableExtra("dateTo") as LocalDateTime
        } else null

        binding.sendResultsInMailButton.setOnClickListener {
            val intent = Intent(this@ResultListActivity, MailerActivity::class.java)
            intent.putExtra("dateFrom", dateFrom)
            intent.putExtra("dateTo", dateTo)
            intent.putExtra("results",orderedResults)
//            Log.d("mail", "dateFrom: ${dateFrom!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}, ${dateTo!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
            startActivity(intent)
        }
//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}