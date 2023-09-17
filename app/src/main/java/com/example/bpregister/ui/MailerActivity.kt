package com.example.bpregister.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bpregister.R
import com.example.bpregister.databinding.ActivityMailerBinding
import com.example.bpregister.utils.MailHelper
import java.time.LocalDateTime

class MailerActivity : Activity() {
    private lateinit var binding:ActivityMailerBinding
    private  var dateFrom:LocalDateTime?=null
    private  var dateTo:LocalDateTime? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateFrom = if (intent.getSerializableExtra("dateFrom") is LocalDateTime) {
            intent.getSerializableExtra("dateFrom") as LocalDateTime
        } else null

        dateTo = if (intent.getSerializableExtra("dateTo") is LocalDateTime) {
            intent.getSerializableExtra("dateTo") as LocalDateTime
        } else null

        binding.recipientEt.setText(MailHelper.getRecipient().trim(), TextView.BufferType.EDITABLE)
        binding.subjectEt.setText(MailHelper.getSubject(this@MailerActivity,dateFrom, dateTo).trim(), TextView.BufferType.EDITABLE)
        binding.messageEt.setText(MailHelper.getBody(this@MailerActivity,dateFrom, dateTo).trim(), TextView.BufferType.EDITABLE)

        binding.sendEmailBtn.setOnClickListener {
            //get input from EditTexts and save in variables
            val recipient = binding.recipientEt.text.toString().trim()
            val subject = binding.subjectEt.text.toString().trim()
            val message = binding.messageEt.text.toString().trim()

            Log.d("mail","to: $recipient \nsubject: $subject \n message: \n $message")

            sendEmail(recipient, subject, message)
        }
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