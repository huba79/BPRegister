package com.example.bpregister.ui.viewmodel

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

class BpViewModel(private val repo:BpRepository): ViewModel() {
    private lateinit var filteredBpRecords: LiveData<List<BPEntity>>
    private lateinit var currentRecord: MutableLiveData<BPEntity>

    fun getFiltered(criteria:Criteria) = viewModelScope.launch(Dispatchers.IO){
        filteredBpRecords =  repo.getFiltered(criteria.dateFrom, criteria.dateTo).asLiveData()
    }

    fun getAll(criteria:Criteria) = viewModelScope.launch(Dispatchers.IO){
        filteredBpRecords =  repo.getAll().asLiveData()
    }

    fun save(entity:BPEntity) = viewModelScope.launch(Dispatchers.IO){
        currentRecord.value = entity
        repo.insert(entity)
    }

    fun getById(id:Int) = viewModelScope.launch(Dispatchers.IO){
        currentRecord = repo.getById(id).asLiveData() as MutableLiveData<BPEntity>
    }

    companion object {
        fun provideFactory(
            myRepository: BpRepository,
        ): ViewModelProvider.AndroidViewModelFactory =
            object : ViewModelProvider.AndroidViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    return BpViewModel(myRepository) as T
                }
            }
    }

}
