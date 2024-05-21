package com.example.githubusers.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.FragmentFollowBinding
import com.example.githubusers.ui.main.UserAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()
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

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1) {
            followViewModel.getFollowers(username.toString())
            followViewModel.listfollowers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        }
        else {
            followViewModel.getFollowing(username.toString())
            followViewModel.listfollowing.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)

            }
        }
    }

    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}