package com.example.githubusers.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.data.response.ItemsItem
import com.example.githubusers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        binding.recyclerView.adapter = adapter

        mainViewModel.listUsers.observe(this) { listUsers ->
            setUsersData(listUsers)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        searchData()
    }

    private fun searchData() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val name = searchView.text.toString()
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    mainViewModel.getsearchUser(name)
                    false
                }
        }
    }

    private fun setUsersData(items: List<ItemsItem>) {
        adapter.submitList(items)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}