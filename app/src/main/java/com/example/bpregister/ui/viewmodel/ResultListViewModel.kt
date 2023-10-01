package com.example.bpregister.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bpregister.domain.BPEntity
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultListViewModel(private val repo:BpRepository): ViewModel() {
    var results: LiveData<List<BPEntity>> = MutableLiveData()
    var currentCriteria: Criteria = Criteria(null, null)

    fun getFiltered() = viewModelScope.launch(Dispatchers.IO){
        results =  repo.getFiltered(currentCriteria.dateFrom, currentCriteria.dateTo).asLiveData()
        Log.d("ResultListViewModel","filtered results: ${logList(results.value)}")
    }
    fun getAll() = viewModelScope.launch(Dispatchers.IO){
        results =  repo.getAll().asLiveData()
        Log.d("ResultListViewModel","all results: ${logList(results.value)}")
    }
    companion object Factory {
        fun provideFactory( myRepository: BpRepository ): ViewModelProvider.AndroidViewModelFactory =
            object : ViewModelProvider.AndroidViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    return ResultListViewModel(myRepository) as T
                }
            }
    }
    fun logList(list:List<BPEntity>?):String{
        var out = ""
        if (list!=null) {
            for(item:BPEntity in list){
                out += item.toString()
            }
            return out
        }
        return out
    }
}
