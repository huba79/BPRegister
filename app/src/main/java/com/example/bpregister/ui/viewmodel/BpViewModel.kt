package com.example.bpregister.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bpregister.domain.BPEntity
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

class BpViewModel(private val repo:BpRepository): ViewModel() {
    private var currentRecord: MutableLiveData<BPEntity?> = MutableLiveData<BPEntity?>(BPEntity(0,0,LocalDateTime.now(), LocalTime.of(10,0)))
    var currentCriteria: Criteria = Criteria(null, null)
    fun save(entity:BPEntity) {
        viewModelScope.launch(Dispatchers.IO){
            repo.insert(entity)
        }
        currentRecord.value = entity
    }

    fun setDateOfCurrent(date:LocalDateTime){
        currentRecord.value.let { currentRecord.value!!.date= date }
    }
    fun setTimeOfCurrent(time: LocalTime){
        currentRecord.value.let { currentRecord.value!!.time= time }
    }
    fun setSistholicOfOfCurrent(sis:Int){
        currentRecord.value.let { currentRecord.value!!.sistholic= sis }
    }
    fun setDiastholicOfOfCurrent(dia:Int){
        currentRecord.value.let { currentRecord.value!!.diastholic= dia }
    }
    fun getCurrent():BPEntity?{
         currentRecord.let{
             return currentRecord.value
        }
    }
    companion object Factory {
        fun provideFactory(
            myRepository: BpRepository
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
