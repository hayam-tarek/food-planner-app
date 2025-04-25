package com.example.foodplanner.features.mainLayout.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodplanner.utils.NetworkUtil

class NetworkViewModel(application: Application) : AndroidViewModel(application) {
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

//    init {
//        checkInternetConnection()
//    }
    fun checkInternetConnection() {
        _isConnected.postValue(NetworkUtil.isInternetAvailable(getApplication()))
    }

}
