package com.listgithubusersinglescreen.ui.home

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.FragmentHomeBinding
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.ui.adapter.MainAdapter
import com.listgithubusersinglescreen.ui.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by sharedViewModel()
    private val menuProvider: MenuProvider by lazy {
        object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                val searchManager: SearchManager? = requireActivity().getSystemService()
                val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView
                searchConfig(searchManager!!, searchView)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.love -> {
                        findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
                        true
                    }

                    R.id.setting -> {
                        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }

                    else -> true
                }
            }
        }
    }

    override fun FragmentHomeBinding.initialize() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager

        // prevent get new data when configuration changed like rotate
        if (savedInstanceState === null) {
            viewModel.getFreshUser()
        }

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.srlMain.setOnRefreshListener {
            viewModel.getFreshUser() // sedang tidak work
        }

        binding.reloadBtn.setOnClickListener {
            viewModel.getFreshUser()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collect { result ->
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
    }

    private fun searchConfig(searchManager: SearchManager, searchView: SearchView) {
        searchView.apply {
            setQuery(viewModel.searchText.value, false)
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.clearFocus()
                    viewModel.setSearchText(query)
                    viewModel.getSearchUser(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.setSearchText(newText)
                    return true
                }
            })
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
}