package com.kylix.demosubmissionbfaa.ui.detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kylix.demosubmissionbfaa.data.Resource
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception

class DetailViewModel: ViewModel() {

    private val retrofit = RetrofitService.create()

    fun getDetailUser(username: String?) = flow<Resource<User>> {
        try {
            val result = retrofit.getDetailUser(username.toString())
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO).asLiveData()
}