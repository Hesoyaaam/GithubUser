package com.example.githubusers.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.FragmentFollowBinding
import com.example.githubusers.ui.detail.DetailViewModel
import com.example.githubusers.ui.main.UserAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var position: Int = 0
    private var username: String? = null
    private lateinit var adapter: UserAdapter

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        adapter = UserAdapter()
        binding.rvFollowRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@FollowFragment.adapter
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1) {
            detailViewModel.getFollowers(username.toString())
            detailViewModel.listfollowers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        }
        else {
            detailViewModel.getFollowing(username.toString())
            detailViewModel.listfollowing.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)

            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}