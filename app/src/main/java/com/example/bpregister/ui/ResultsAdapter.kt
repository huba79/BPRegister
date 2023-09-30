package com.example.bpregister.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import com.example.bpregister.R
import com.example.bpregister.databinding.CardItemBinding
import com.example.bpregister.databinding.CardHeaderBinding
import com.example.bpregister.domain.deprecated.BPItem
import com.example.bpregister.utils.DateUtils

class ResultsAdapter(private val context: Context, private val bpDataList: List<BPItem>) : Adapter<ResultsAdapter.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
        override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return TYPE_HEADER
        } else return TYPE_ITEM
    }
    open class ViewHolder(var binding:ViewBinding): RecyclerView.ViewHolder(binding.root) {
    }

    private fun isPositionHeader(position:Int):Boolean{
        return position==0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("adapter","onCreate")

        if (viewType==TYPE_ITEM){
            return ViewHolder(CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return ViewHolder(CardHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }
    override fun getItemCount(): Int {
        return bpDataList.size+1
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter","onBind $position")
        if ( position ==0) {             //then it has to be a HeaderCardHolder with HeaderCardBinding
            val headerCardBinding = holder.binding as CardHeaderBinding
            headerCardBinding.sistholicListItemView.text = context.resources.getText(R.string.label_sistholic_short)
            headerCardBinding.diastholicListItemView.text = context.resources.getText(R.string.label_diastholic_short)
            headerCardBinding.dateListItemView.text = context.resources.getText(R.string.label_date)
            headerCardBinding.timeListItemView.text = context.resources.getText(R.string.label_time)
            headerCardBinding.readOnListItemView.text=context.resources.getText(R.string.read_on)
        } else { // it has to be an ItemCardHolder with ItemCardBinding
            val itemCardBinding  = holder.binding as CardItemBinding
            val item = bpDataList[position-1]

            itemCardBinding.sistholicListItemView.text= item.sistholic.toString()
            itemCardBinding.diastholicListItemView.text=item.diastholic.toString()
            itemCardBinding.readOnListItemView.text=context.resources.getText(R.string.read_on)
            itemCardBinding.dateListItemView.text=DateUtils.toDisplayableDate(item.localDate.year,item.localDate.monthValue,item.localDate.dayOfMonth)
            itemCardBinding.timeListItemView.text=DateUtils.toDisplayableTime(item.localTime.hour, item.localTime.minute)
        }


    }
}