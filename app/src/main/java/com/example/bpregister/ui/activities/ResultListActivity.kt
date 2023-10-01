package com.example.bpregister.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpregister.databinding.ActivityResultListBinding
import com.example.bpregister.domain.BPEntity
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BPApplication
import com.example.bpregister.ui.viewmodel.ResultListViewModel
import com.example.bpregister.utils.MailHelper

class ResultListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultListBinding
    private lateinit var resultsViewModel: ResultListViewModel
    private lateinit var recipients:String
    private  lateinit var subject: String
    private lateinit var message: String

    private val adapter = ResultsAdapter(this@ResultListActivity)
    private var criteria:Criteria = Criteria(null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultsViewModel = ResultListViewModel.provideFactory((this.application as BPApplication).repo).create(ResultListViewModel::class.java)

        resultsViewModel.results.observe(this@ResultListActivity) {
            Log.d("On Results changed","Actual criteria: ${criteria}")
            Log.d("On Results changed","Updated results: ${logList(it)}")
            adapter.setResults(it)
        }
        if (intent.getSerializableExtra("criteria") is Criteria) {
            criteria = intent.getSerializableExtra("criteria") as Criteria
            Log.d("criteria",criteria.toString())
            resultsViewModel.currentCriteria = criteria
            Log.i("Fetching results with","Criteria from: ${criteria.dateFrom}, to: ${criteria.dateTo}")
            resultsViewModel.getFiltered()
        }
        else {
            Toast.makeText(this@ResultListActivity,"Incorrect parameter!",Toast.LENGTH_SHORT).show()
            resultsViewModel.getAll()
        }

        binding.resultsRecyclerView.layoutManager=LinearLayoutManager(this@ResultListActivity)
        binding.resultsRecyclerView.adapter=adapter

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
    fun logList(list:List<BPEntity>?):String{
        var out = ""
        if (list!=null) {
            for(item: BPEntity in list){
                out += item.toString()
            }
            return out
        }
        return out
    }
}