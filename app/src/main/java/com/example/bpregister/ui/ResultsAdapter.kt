package com.example.bpregister.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.bpregister.R
import com.example.bpregister.databinding.ItemCardBinding
import com.example.bpregister.domain.BPItem
import com.example.bpregister.utils.DateUtils

class ResultsAdapter(private val context: Context, private val bpDataList: List<BPItem>) : Adapter<ResultsAdapter.ViewHolder>() {
    class ViewHolder(var itemBinding: ItemCardBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("adapter","onCreate")
        var binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return bpDataList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter","onBind $position")
        val itemBinding = ItemCardBinding.inflate(LayoutInflater.from(context))
        val item = bpDataList[position]

        holder.itemBinding.sistholicListItemView.text= item.sistholic.toString()
        holder.itemBinding.diastholicListItemView.text=item.diastholic.toString()
        holder.itemBinding.dateListItemView.text=DateUtils.toDisplayableDate(item.localDate.year,item.localDate.monthValue,item.localDate.dayOfMonth)
        holder.itemBinding.timeListItemView.text=context.getString(R.string.time_visual_formatter,item.localTime.hour,item.localTime.minute)
    }
}