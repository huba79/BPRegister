package com.example.bpregister.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bpregister.domain.BloodPressureReading
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BloodPressureReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultListViewModel(private val repo:BloodPressureReadingRepository): ViewModel() {

    private val _results: MutableLiveData<List<BloodPressureReading>> = MutableLiveData<List<BloodPressureReading>>()
    val results: LiveData<List<BloodPressureReading>> = _results

    fun queryFiltered(criteria : Criteria) {
        viewModelScope.launch {
            repo.getByCriteria(criteria.dateFrom, criteria.dateTo).collect { results ->
                _results.value = results
            }
        }
    }

    fun queryAll() = viewModelScope.launch(Dispatchers.IO){
        Log.d("ResultListViewModel","All results requested from viewmodel")
        viewModelScope.launch {
            repo.getUnfiltered().collect { results ->
                _results.value = results
            }
        }
    }


    companion object Factory {
        fun provideFactory( myRepository: BloodPressureReadingRepository ): ViewModelProvider.AndroidViewModelFactory =
            object : ViewModelProvider.AndroidViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    return ResultListViewModel(myRepository) as T
                }
            }
    }

}
