package com.kylix.demosubmissionbfaa.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.kylix.demosubmissionbfaa.R
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.databinding.FollowerFragmentBinding
import com.kylix.demosubmissionbfaa.ui.adapter.UserAdapter
import com.kylix.demosubmissionbfaa.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowerFragment(private val username: String) : Fragment(), ViewStateCallback {

    private val followerBinding: FollowerFragmentBinding by viewBinding()

    //private lateinit var viewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.follower_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(FollowerViewModel::class.java)

        val userAdapter = UserAdapter()
        followerBinding.rvListUserFollower.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        val retrofit = RetrofitService.create()

        CoroutineScope(Dispatchers.Main).launch {
            onLoading()
            try {
                val list = retrofit.getUserFollowers(username)
                if (list.isNullOrEmpty()) {
                    onFailed(null)
                } else {
                    userAdapter.setAllData(list)
                    onSuccess()
                }
            } catch (e: Exception) {
                onFailed(e.message)
            }
        }
    }

    override fun onSuccess() {
        followerBinding.apply {
            tvMessage.visibility = invisible
            followProgressBar.visibility = invisible
            rvListUserFollower.visibility = visible
        }
    }

    override fun onLoading() {
        followerBinding.apply {
            tvMessage.visibility = invisible
            followProgressBar.visibility = visible
            rvListUserFollower.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        followerBinding.apply {
            if (message == null) {
                tvMessage.text = resources.getString(R.string.followers_not_found, username)
                tvMessage.visibility = visible
            } else {
                tvMessage.text = message
                tvMessage.visibility = visible
            }
            followProgressBar.visibility = invisible
            rvListUserFollower.visibility = invisible
        }
    }
}