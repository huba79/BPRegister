package com.example.bpregister.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpregister.databinding.ActivityResultListBinding
import com.example.bpregister.domain.BPComparator
import com.example.bpregister.domain.deprecated.BPItem
import com.example.bpregister.domain.deprecated.BPRepository
import com.example.bpregister.domain.Criteria
import com.example.bpregister.utils.MailHelper

class ResultListActivity : Activity() {

    private lateinit var orderedResults:MutableList<BPItem>
    private lateinit var binding: ActivityResultListBinding

    private lateinit var recipients:String
    private  lateinit var subject: String
    private lateinit var message: String
    val repo = BPRepository
    var criteria:Criteria = Criteria(null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)

            if (intent.getSerializableExtra("criteria") is Criteria) {
                criteria = intent.getSerializableExtra("criteria") as Criteria
                criteria = criteria.normalize()

                orderedResults =  repo.filterResults(repo.readAllFromFile(this@ResultListActivity),criteria)
                orderedResults.toMutableList().sortWith( BPComparator() )
                Log.d("orderedFilteredResults", orderedResults.toString())

                val adapter = ResultsAdapter(this@ResultListActivity,orderedResults)
                binding.resultsRecyclerView.layoutManager=LinearLayoutManager(this@ResultListActivity)
                binding.resultsRecyclerView.adapter=adapter

            }
            else {
                Toast.makeText(this@ResultListActivity,"Incorrect parameter!",Toast.LENGTH_SHORT).show()

                orderedResults = BPRepository.readAllFromFile(this@ResultListActivity)
                orderedResults.toMutableList().sortWith( BPComparator() )

                Log.d("orderedUnFilteredResults", orderedResults.toString())

                val adapter = ResultsAdapter(this@ResultListActivity,orderedResults)
                binding.resultsRecyclerView.layoutManager=LinearLayoutManager(this@ResultListActivity)
                binding.resultsRecyclerView.adapter=adapter
            }

        binding.sendResultsInMailButton.setOnClickListener {
            recipients= MailHelper.getRecipient().trim()
            subject = MailHelper.getSubject(this@ResultListActivity, criteria.dateFrom, criteria.dateTo).trim()
            message = MailHelper.getBody(this@ResultListActivity, criteria.dateFrom, criteria.dateTo).trim()
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