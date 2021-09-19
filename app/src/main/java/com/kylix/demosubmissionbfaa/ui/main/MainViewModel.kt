package com.kylix.demosubmissionbfaa.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kylix.demosubmissionbfaa.data.Resource
import com.kylix.demosubmissionbfaa.data.remote.ApiService
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainViewModel: ViewModel() {

    private val retrofit: ApiService = RetrofitService.create()

    fun searchUser(query: String) = flow<Resource<List<User>>> {
        emit(Resource.Loading())
        try {
            val list = retrofit.searchUsers(query).items
            if (list.isNullOrEmpty())
                emit(Resource.Error(null))
            else
                emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO).asLiveData()
}