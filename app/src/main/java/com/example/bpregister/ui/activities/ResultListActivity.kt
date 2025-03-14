package com.example.bpregister.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpregister.databinding.ActivityResultListBinding
import com.example.bpregister.domain.BloodPressureReading
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BPApplication
import com.example.bpregister.ui.viewmodel.ResultListViewModel
import com.example.bpregister.utils.MailHelper
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class ResultListActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityResultListBinding
    private lateinit var resultsViewModel: ResultListViewModel

    private lateinit var recipients: String
    private lateinit var subject: String
    private lateinit var message: String

    private val adapter = ResultsAdapter(this@ResultListActivity)

    private lateinit var searchCriteria: Criteria // = Criteria(null, null)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val applicationContext = this.applicationContext as BPApplication
        resultsViewModel = ResultListViewModel
            .provideFactory(applicationContext.getRepo())
            .create(ResultListViewModel::class.java)
        val lifeCycleOwner = this

        resultsViewModel.results.observe(lifeCycleOwner) { list ->
            Log.d("ResultListActivity", "Observer triggered")
            if (list != null && list.isNotEmpty()) {
                Log.d("ResultListActivity", "List is not empty: ${logList(list)}")
                adapter.setContents(list)
            } else {
                Log.d("ResultListActivity", "List is not empty: ${logList(list)}")
                Toast.makeText(this@ResultListActivity, "No results found!", Toast.LENGTH_SHORT)
                    .show()
                //TODO list seems to be empty!!!!!!!!!!
            }
        }

        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(this@ResultListActivity)
        adapter.setContents(
            resultsViewModel.results.value ?: listOf(
                BloodPressureReading(
                    0, 0,
                    LocalDate.now(), LocalTime.now()
                )
            )
        )
        binding.resultsRecyclerView.adapter = adapter

        if (intent.getSerializableExtra("criteria") is Criteria) {
            searchCriteria = intent.getSerializableExtra("criteria") as Criteria
            Log.d("ResultListActivity: ", "Received criteria: $searchCriteria")
            Log.i(
                "ResultListActivity",
                "Fetching results from: ${searchCriteria.dateFrom}, to: ${searchCriteria.dateTo}"
            )
            resultsViewModel.queryFiltered(searchCriteria)
        } else {
            Toast.makeText(
                this@ResultListActivity,
                "Incorrect date filter parameter!",
                Toast.LENGTH_SHORT
            ).show()
            resultsViewModel.queryAll()
        }

        binding.sendResultsInMailButton.setOnClickListener {
            recipients = MailHelper.getRecipient().trim()
            subject = MailHelper.getSubject(
                this@ResultListActivity,
                searchCriteria.dateFrom,
                searchCriteria.dateTo
            ).trim()
            message = MailHelper.getBody(
                this@ResultListActivity,
                searchCriteria.dateFrom,
                searchCriteria.dateTo
            ).trim()
            val file = resultsViewModel.getResultListAsFile()
            if (file != null) {
                sendEmailWithAttachment(
                    context = this@ResultListActivity,
                    recipients,
                    subject,
                    message,
                    file
                )
            } else {
                Toast.makeText(this@ResultListActivity, "No results found!", Toast.LENGTH_SHORT)
                    .show()

            }

            /*
            setSupportActionBar(findViewById(R.id.toolbar))
            binding.toolbarLayout.title = title
            binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
            }
            */
        }

    }

    private fun sendEmailWithAttachment(
        context: Context,
        recipient: String,
        subject: String,
        body: String,
        file: File ) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "vnd.android.cursor.dir/email" // MIME type for email
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)

        // Get the file URI using FileProvider
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Use the same authority as in AndroidManifest.xml
            file
        )

        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission

        // Check if there's an app to handle the intent
        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } else {
            // Handle the case where no email app is found
            // You might want to show a message to the user
            //   TODO check Snackbar workings         Snackbar.make(context)
            Toast.makeText(context, "No mailer app found", Toast.LENGTH_SHORT).show()
        }
    }

    fun logList(list: List<BloodPressureReading>?): String {
        return list?.joinToString { it.toString() } ?: ""
    }
}