package com.example.githubusers.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.example.githubusers.R
import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.database.FavoriteUser
import com.example.githubusers.databinding.ActivityDetailBinding
import com.example.githubusers.helper.ViewModelFactory
import com.example.githubusers.ui.fragment.SectionPagerAdapater
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_NAME = "username"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.text1,
            R.string.text2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionPagerAdapater(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.hide()
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        val username = intent.getStringExtra(EXTRA_NAME)
        sectionsPagerAdapter.username = username.toString()

        detailViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        if (username != null) {
            detailViewModel.getDetailUser(username)
        }

        var isFavorite = false
        detailViewModel.checkFavorite(username).observe(this) { favoriteUser ->
            if (favoriteUser == null) {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                isFavorite = false
            } else {
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
                isFavorite = true
            }
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                username?.let {
                    detailViewModel.addFavorite(it, avatarUrl)
                }
            } else {
                username?.let {
                    detailViewModel.deleteFavorite(FavoriteUser(it, it))
                }
            }
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun setDetailUser(user: DetailUserResponse) {
        binding.tvName.text = user.login
        binding.tvDetailUsername.text = user.name
        binding.ivPhoto.load(user.avatarUrl) {
            transformations(CircleCropTransformation())
        }
        binding.tvFollowers.text = "${user.followers} Followers"
        binding.tvFollowing.text = "${user.following} Following"
    }
}