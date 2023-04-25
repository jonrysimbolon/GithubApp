package com.listgithubusersinglescreen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.FragmentHomeBinding
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.ui.adapter.MainAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        observeFreshUser(viewModel)

        binding.srlMain.setOnRefreshListener {
            observeFreshUser(viewModel)
        }

        binding.reloadBtn.setOnClickListener {
            observeFreshUser(viewModel)
        }

        viewModel.searchText.observe(requireActivity()) { text ->
            viewModel.searchView.value?.apply {
                setQuery(text, false)
                setIconifiedByDefault(false)
                isIconified = false
            }
        }
    }

    private fun showFailedComponent(isFailure: Boolean) {
        binding.apply {
            errorImg.visibility = if (isFailure) View.VISIBLE else View.GONE
            reloadBtn.visibility = if (isFailure) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                if (srlMain.isRefreshing) {
                    srlMain.isRefreshing = !srlMain.isRefreshing
                } else {
                    progressBar.visibility = View.VISIBLE
                }
            } else {
                if (srlMain.isRefreshing) {
                    srlMain.isRefreshing = srlMain.isRefreshing
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showPersonNotFound(isEmpty: Boolean) {
        binding.notFoundImg.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setUsersData(users: List<UserEntity>) {
        showPersonNotFound(users.isEmpty())
        val usersData = ArrayList<UserEntity>()
        for (user in users) {
            usersData.add(user)
        }
        val adapter = MainAdapter { view, user ->
            val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            toDetailFragment.userNodeId = user.nodeId
            toDetailFragment.userLogin = user.login
            view.findNavController().navigate(toDetailFragment)
        }.also {
            it.setListUsers(usersData)
        }
        binding.rvUsers.adapter = adapter
    }

    private fun observeFreshUser(viewModel: HomeViewModel) {
        viewModel.getFreshUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultStatus.Loading -> {
                        showLoading(true)
                        showFailedComponent(false)
                    }

                    is ResultStatus.Success -> {
                        showLoading(false)
                        showFailedComponent(false)
                        val userData = result.data
                        setUsersData(userData)
                    }

                    is ResultStatus.Error -> {
                        showLoading(false)
                        showFailedComponent(true)
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.failed_desc) + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun observeSearchUser(viewModel: HomeViewModel) {
        viewModel.getSearchUser(viewModel.searchText.value ?: "")
            .observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultStatus.Loading -> {
                            showLoading(true)
                            showFailedComponent(false)
                        }

                        is ResultStatus.Success -> {
                            showLoading(false)
                            showFailedComponent(false)
                            val userData = result.data
                            setUsersData(userData)
                        }

                        is ResultStatus.Error -> {
                            showLoading(false)
                            showFailedComponent(true)
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.failed_desc) + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}