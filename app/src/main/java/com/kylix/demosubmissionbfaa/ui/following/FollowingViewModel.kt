package com.kylix.demosubmissionbfaa.ui.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kylix.demosubmissionbfaa.data.Resource
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class FollowingViewModel : ViewModel() {

    private val retrofit = RetrofitService.create()

    fun getUserFollowing(username: String) = flow<Resource<List<User>>> {
        emit(Resource.Loading())
        try {
            val list = retrofit.getUserFollowing(username)
            if (list.isNullOrEmpty())
                emit(Resource.Error(null))
            else
                emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO).asLiveData()

}