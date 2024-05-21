package com.example.githubusers.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.response.ItemsItem
import com.example.githubusers.databinding.ActivityFavoriteBinding
import com.example.githubusers.helper.ViewModelFactory
import com.example.githubusers.ui.main.UserAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite"

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewFav.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        binding.recyclerViewFav.adapter = adapter


        favoriteViewModel.getAllFav().observe(this) { favUserList ->
            val items = arrayListOf<ItemsItem>()
            favUserList.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            adapter.submitList(items)
        }
    }
}

