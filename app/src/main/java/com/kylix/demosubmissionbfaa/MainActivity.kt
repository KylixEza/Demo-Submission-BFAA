package com.kylix.demosubmissionbfaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.databinding.ActivityMainBinding
import com.kylix.demosubmissionbfaa.ui.adapter.UserAdapter
import com.kylix.demosubmissionbfaa.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ViewStateCallback {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var userQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val userAdapter = UserAdapter()
        mainBinding.includeMainSearch.rvListUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        mainBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    userQuery = query.toString()
                    clearFocus()
                    startSearching(userAdapter)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    private fun startSearching(userAdapter: UserAdapter) {
        val retrofit = RetrofitService.create()

        CoroutineScope(Dispatchers.Main).launch {
            onLoading()
            try {
                val list = retrofit.searchUsers(userQuery)
                if (list.items.isNullOrEmpty()) {
                    onFailed(null)
                } else {
                    userAdapter.setAllData(list.items)
                    onSuccess()
                }
            } catch (e: Exception) {
                onFailed(e.message)
            }
        }
    }

    override fun onSuccess() {
        mainBinding.includeMainSearch.apply {
            ivSearchIcon.visibility = invisible
            tvMessage.visibility = invisible
            mainProgressBar.visibility = invisible
            rvListUser.visibility = visible
        }
    }

    override fun onLoading() {
        mainBinding.includeMainSearch.apply {
            ivSearchIcon.visibility = invisible
            tvMessage.visibility = invisible
            mainProgressBar.visibility = visible
            rvListUser.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        mainBinding.includeMainSearch.apply {
            //User yang dicari tidak ditemukan
            if (message == null) {
                ivSearchIcon.apply {
                    setImageResource(R.drawable.ic_search_empty)
                    visibility = visible
                }
                tvMessage.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
                //Memunculkan error pada exception
            } else {
                ivSearchIcon.apply {
                    setImageResource(R.drawable.ic_search_failed)
                    visibility = visible
                }
                tvMessage.apply {
                    text = message
                    visibility = visible
                }
            }
            mainProgressBar.visibility = invisible
            rvListUser.visibility = invisible
        }
    }
}