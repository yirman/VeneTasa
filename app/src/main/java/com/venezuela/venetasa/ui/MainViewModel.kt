package com.venezuela.venetasa.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venezuela.venetasa.model.Rate
import com.venezuela.venetasa.repository.RateRepository
import com.venezuela.venetasa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: RateRepository
) : ViewModel() {

    fun queryRates(): LiveData<List<Rate>> =  repository.queryAllRates()

    fun getNetworkCallStatus(): LiveData<Resource.Status> = repository.networkCallStatus

    fun fetchRates(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRates()
        }
    }

}