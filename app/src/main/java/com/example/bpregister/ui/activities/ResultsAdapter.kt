package com.example.bpregister.ui.activities

import android.annotation.SuppressLint
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
import com.example.bpregister.domain.BloodPressureReading
import com.example.bpregister.utils.DateUtils

class ResultsAdapter(private val context: Context) : Adapter<ResultsAdapter.ViewHolder>() {
    private var results:List<BloodPressureReading> = ArrayList()
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    @SuppressLint("NotifyDataSetChanged")
    fun setContents(pResults: List<BloodPressureReading>) {
        results = pResults
        Log.d("ResultListActivity", "Updated results(adapter level): $results")
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) {
            TYPE_HEADER
        } else TYPE_ITEM
    }
    open class ViewHolder(var binding:ViewBinding): RecyclerView.ViewHolder(binding.root)

    private fun isPositionHeader(position:Int):Boolean{
        return position==0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ResultsAdapter","onCreate")
        Log.d("ResultsAdapter","results:${results} ")
        return if (viewType==TYPE_ITEM){
            ViewHolder(CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ViewHolder(CardHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
    override fun getItemCount(): Int {
        return results.size+1
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
            val item = results[position-1]

            itemCardBinding.sistholicListItemView.text= item.sistholic.toString()
            itemCardBinding.diastholicListItemView.text=item.diastholic.toString()
            itemCardBinding.readOnListItemView.text=context.resources.getText(R.string.read_on)
            itemCardBinding.dateListItemView.text=DateUtils.toDisplayableDate(item.date.year,item.date.monthValue,item.date.dayOfMonth)
            itemCardBinding.timeListItemView.text=DateUtils.toDisplayableTime(item.time.hour, item.time.minute)
        }
    }
    fun logList(list:List<BloodPressureReading>?):String{
        var out = "Empty list"
        if (list!=null) {
            for(item:BloodPressureReading in list){
                out += item.toString()
            }
            Log.d("ResultListViewModel","Result list is: $out")
            return out

        } else Log.d("ResultListViewModel","Result list is empty!")
        return out
    }
}