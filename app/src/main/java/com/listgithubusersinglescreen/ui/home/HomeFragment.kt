package com.listgithubusersinglescreen.ui.home

import android.app.SearchManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.data.ResultStatus
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.FragmentHomeBinding
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
        setupMenu()
    }
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                searchView(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.love -> {
                        Toast.makeText(requireActivity(), "love list", Toast.LENGTH_SHORT).show()
                        return true
                    }
                    R.id.setting -> {
                        Toast.makeText(requireActivity(), "setting list", Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

    }

    private fun observeSearchUser(viewModel: HomeViewModel, query: String) {
        viewModel.getSearchUser(query).observe(this) { result ->
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
                            requireActivity(), "Terjadi kesalahan" + result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun searchView(menu: Menu) {
        val searchManager = requireActivity().getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager?.getSearchableInfo(requireActivity().componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    observeSearchUser(viewModel, query!!)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}