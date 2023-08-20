package com.example.bpregister.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bpregister.databinding.ActivityBpListBinding
import com.example.bpregister.domain.BPItem

class ResultListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBpListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBpListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val results:ArrayList<BPItem> = savedInstanceState!!.getSerializable("results") as ArrayList<BPItem>

//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbarLayout.title = title
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}