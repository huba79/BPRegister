package com.example.bpregister.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bpregister.databinding.ActivityResultListBinding
import com.example.bpregister.domain.BPItem

class ResultListActivity : Activity() {

    private lateinit var binding: ActivityResultListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.getSerializableExtra ("results") is ArrayList<*> ) {
            val results = intent.getSerializableExtra ("results") as ArrayList<BPItem>

            Log.d("results","after sending...$results")

            val adapter = ResultsAdapter(this@ResultListActivity,results)
            binding.resultsRecyclerView.layoutManager=LinearLayoutManager(this@ResultListActivity)
            binding.resultsRecyclerView.adapter=adapter
        } else {
            Toast.makeText(this@ResultListActivity,"Incorrect parameter!",Toast.LENGTH_SHORT).show()
        }

//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}