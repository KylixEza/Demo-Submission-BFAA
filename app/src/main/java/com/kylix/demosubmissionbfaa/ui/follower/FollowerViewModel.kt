package com.kylix.demosubmissionbfaa.ui.follower

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kylix.demosubmissionbfaa.data.Resource
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FollowerViewModel : ViewModel() {

    private val retrofit = RetrofitService.create()
    
    fun getUserFollowers(username: String) = flow<Resource<List<User>>> {
        emit(Resource.Loading())
        try {
            val list = retrofit.getUserFollowers(username)
            if (list.isNullOrEmpty())
                emit(Resource.Error(null))
            else
                emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO).asLiveData()
}