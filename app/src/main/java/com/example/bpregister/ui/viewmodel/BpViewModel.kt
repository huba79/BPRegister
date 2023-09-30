package com.example.bpregister.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bpregister.domain.BPEntity
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BpRepository
import kotlinx.coroutines.launch

class BpViewModel(private val repo:BpRepository): ViewModel() {
    private lateinit var filteredBpRecords: LiveData<List<BPEntity>>
    private lateinit var currentRecord: MutableLiveData<BPEntity>

    fun getFiltered(criteria:Criteria) = viewModelScope.launch{
        filteredBpRecords =  repo.getFiltered(criteria.dateFrom, criteria.dateTo).asLiveData()
    }

    fun getAll(criteria:Criteria) = viewModelScope.launch{
        filteredBpRecords =  repo.getAll().asLiveData()
    }

    fun save(entity:BPEntity) = viewModelScope.launch{
        repo.insert(entity)
    }

    fun getById(id:Int) = viewModelScope.launch{
        currentRecord = repo.getById(id).asLiveData() as MutableLiveData<BPEntity>
    }





}