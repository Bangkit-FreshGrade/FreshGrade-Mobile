package com.example.freshgrade.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.freshgrade.data.repo.UserRepository
import com.example.freshgrade.data.response.HistoryResponse
import com.example.freshgrade.data.response.HistoryResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _history = MutableLiveData<List<HistoryResponseItem>?>()
    val history: LiveData<List<HistoryResponseItem>?> = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    fun fetchHistory() {
//        _isLoading.value = true
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getHistory().enqueue(object : Callback<List<HistoryResponseItem>> {
//                override fun onResponse(
//                    call: Call<List<HistoryResponseItem>>,
//                    response: Response<List<HistoryResponseItem>>
//                ) {
//                    if (response.isSuccessful) {
//                        _history.postValue(response.body())
//                    } else {
//                        _history.postValue(null)
//                    }
//                    _isLoading.postValue(false)
//                }
//
//                override fun onFailure(call: Call<List<HistoryResponseItem>>, t: Throwable) {
//                    _history.postValue(null)
//                    _isLoading.postValue(false)
//                }
//            })
//        }
//    }

    fun fetchHistory() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHistory().enqueue(object : Callback<List<HistoryResponseItem>> {
                override fun onResponse(
                    call: Call<List<HistoryResponseItem>>,
                    response: Response<List<HistoryResponseItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _history.postValue(it)
                        }
                    } else {
                        _history.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<List<HistoryResponseItem>>, t: Throwable) {
                    _isLoading.value = false
                    _history.postValue(null)
                }
            })
        }
    }
}
