package com.example.bpregister.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bpregister.domain.BloodPressureReading
import com.example.bpregister.domain.Criteria
import com.example.bpregister.room.BloodPressureReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class BpViewModel(private val repo:BloodPressureReadingRepository): ViewModel() {

    private var currentRecord: MutableLiveData<BloodPressureReading?> = MutableLiveData<BloodPressureReading?>(BloodPressureReading(0,0,LocalDate.now(), LocalTime.of(10,0)))
    var currentCriteria: Criteria = Criteria(null, null)

    fun save(entity:BloodPressureReading) {
        viewModelScope.launch(Dispatchers.IO){
            repo.insert(entity)
            Log.d("BpViewModel","Data saved to repository")
        }
        currentRecord.value = entity
    }

    fun setDateOfCurrent(date:LocalDate){
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
    fun getCurrent():BloodPressureReading?{
         currentRecord.let{
             return currentRecord.value
        }
    }
    companion object Factory {
        fun provideFactory(
            myRepository: BloodPressureReadingRepository
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
