package com.listgithubusersinglescreen.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    private var _searchMenuItem: MenuItem? = null
    private val searchMenuItem get() = _searchMenuItem

    private var _searchView: SearchView? = null
    private val searchView get() = _searchView!!

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

        setupMenu()

        observeFreshUser()

        binding.srlMain.setOnRefreshListener {
            observeFreshUser()
        }

        binding.reloadBtn.setOnClickListener {
            observeFreshUser()
        }

        viewModel.searchText.observe(viewLifecycleOwner) { text ->

            // submit
            // :. false -> save state but not submit auto
            // :. true -> save state and submit auto
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
        val adapter = MainAdapter{ user ->

        }.also {
            it.setListUsers(usersData)
        }
        binding.rvUsers.adapter = adapter
    }

    private fun observeFreshUser() {
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

    private fun observeSearchUser(text: String) {
        viewModel.getSearchUser(text).observe(viewLifecycleOwner) { result ->
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
        _searchView = null
        _searchMenuItem = null
    }

    private fun searchView(menu: Menu) {
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        _searchMenuItem = menu.findItem(R.id.search)
        _searchView = searchMenuItem?.actionView as SearchView
        searchView.apply {
            setIconifiedByDefault(false)
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.clearFocus()
                    observeSearchUser(query)
                    viewModel.searchText.value = query
                    viewModel.searchView.value = _searchView
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchText.value = newText
                    viewModel.searchView.value = _searchView
                    return true
                }
            })
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                searchView(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.love -> {
                        Toast.makeText(requireActivity(), "love list", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.setting -> {
                        findNavController()
                            .navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }

                    else -> true
                }
            }
        }, viewLifecycleOwner)
    }

}