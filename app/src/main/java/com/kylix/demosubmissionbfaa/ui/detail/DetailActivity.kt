package com.kylix.demosubmissionbfaa.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.kylix.demosubmissionbfaa.data.remote.RetrofitService
import com.kylix.demosubmissionbfaa.databinding.ActivityDetailBinding
import com.kylix.demosubmissionbfaa.model.User
import com.kylix.demosubmissionbfaa.ui.adapter.FollowPagerAdapter
import com.kylix.demosubmissionbfaa.util.Constanta
import com.kylix.demosubmissionbfaa.util.Constanta.EXTRA_USER
import com.kylix.demosubmissionbfaa.util.Constanta.TAB_TITLES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val username = intent.getStringExtra(EXTRA_USER)

        val retrofit = RetrofitService.create()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = retrofit.getDetailUser(username.toString())
                setView(user)
            } catch (e: Exception) {
                Log.e(this.toString(), e.message.toString())
            }
        }

        val pageAdapter = FollowPagerAdapter(this, username.toString())

        detailBinding.apply {
            viewPager.adapter = pageAdapter
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setView(user: User?) {

        detailBinding.apply {
            tvDetailNumberOfRepos.text = user?.repository.toString()
            tvDetailNumberOfFollowers.text = user?.follower.toString()
            tvDetailNumberOfFollowing.text = user?.following.toString()
            tvDetailName.text = user?.name
            tvDetailCompany.text = user?.company
            tvDetailLocation.text = user?.location

            Glide.with(this@DetailActivity)
                .load(user?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivDetailAvatar)

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = user?.username
                elevation = 0f
            }
        }
    }
}