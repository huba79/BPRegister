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
import com.example.bpregister.utils.MailHelper
import java.time.LocalDateTime

class ResultListActivity : Activity() {
    private  var dateFrom:LocalDateTime?=null
    private  var dateTo:LocalDateTime? =null
    private lateinit var orderedResults:MutableList<BPItem>
    private lateinit var binding: ActivityResultListBinding

    private lateinit var recipients:String
    private  lateinit var subject: String
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.getSerializableExtra ("results") is ArrayList<*> ) {

            val results = intent.getSerializableExtra("results") as List<BPItem>

            orderedResults=results.toMutableList()
            orderedResults.sortWith( BPComparator() )

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

        orderedResults = if(intent.getSerializableExtra("results") is List<*>) {
            intent.getSerializableExtra("results") as MutableList<BPItem>
        } else listOf<BPItem>() as MutableList<BPItem>

        binding.sendResultsInMailButton.setOnClickListener {

            recipients= MailHelper.getRecipient().trim()
            subject = MailHelper.getSubject(this@ResultListActivity, dateFrom, dateTo).trim()
            message = MailHelper.getBody(this@ResultListActivity,dateFrom,dateTo).trim()
            sendEmail(recipients,subject,message)

        }

//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

    }
    private fun sendEmail(recipient: String, subject: String, message: String) {
        val mailSenderIntent = Intent(Intent.ACTION_SEND)

        mailSenderIntent.type="text/plain"

        mailSenderIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mailSenderIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mailSenderIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(mailSenderIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }
}