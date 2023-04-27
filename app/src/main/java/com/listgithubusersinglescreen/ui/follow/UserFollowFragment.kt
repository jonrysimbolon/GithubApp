package com.listgithubusersinglescreen.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.data.local.entity.FollowEntity
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.FragmentUserFollowBinding
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.ui.adapter.MainAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserFollowFragment : Fragment() {

    private var _binding: FragmentUserFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserFollowViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        val user = arguments?.getString(USER) ?: ""
        val argSectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0
        val nodeId = arguments?.getString(NODE_ID) ?: ""
        when (argSectionNumber) {
            0 -> followers(user, nodeId)
            1 -> followings(user, nodeId)
        }
    }

    private fun followers(user: String, nodeId: String) {
        viewModel.fetchFollowers(user, nodeId).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is ResultStatus.Loading -> {
                        showLoading(true)
                        showFailedComponent(false)
                    }
                    is ResultStatus.Success -> {
                        showLoading(false)
                        showFailedComponent(false)
                        val followersData = result.data
                        setFollowsData(followersData)
                    }
                    is ResultStatus.Error -> {
                        showLoading(false)
                        showFailedComponent(true)
                        Toast.makeText(
                            requireActivity(), getString(R.string.terjadi_kesalahan) + result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun followings(user: String, nodeId: String) {
        viewModel.fetchFollowings(user, nodeId).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when (result) {
                    is ResultStatus.Loading -> {
                        showLoading(true)
                        showFailedComponent(false)
                    }
                    is ResultStatus.Success -> {
                        showLoading(false)
                        showFailedComponent(false)
                        val followingsData = result.data
                        setFollowsData(followingsData)
                    }
                    is ResultStatus.Error -> {
                        showLoading(false)
                        showFailedComponent(true)
                        Toast.makeText(
                            requireActivity(), getString(R.string.terjadi_kesalahan) + result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setFollowsData(followers: List<FollowEntity>) {
        val filterFollowers = followers.map { follow ->
            UserEntity(
                nodeId = follow.nodeId,
                login = follow.login,
                followers = follow.followers,
                avatarUrl = follow.avatarUrl,
                following = follow.following,
                name = follow.name,
                url = follow.url,
                isLoved = false
            )
        }

        val adapter = MainAdapter { _, _ ->

        }.also {
            it.setListUsers(filterFollowers)
        }
        binding.rvUsers.adapter = adapter
    }

    private fun showFailedComponent(isFailure: Boolean) {
        binding.apply {
            errorImg.visibility = if (isFailure) View.VISIBLE else View.GONE
            reloadBtn.visibility = if (isFailure) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USER = "user"
        const val NODE_ID = "node_id"
    }
}