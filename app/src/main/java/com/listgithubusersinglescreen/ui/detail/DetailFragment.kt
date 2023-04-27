package com.listgithubusersinglescreen.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.listgithubusersinglescreen.R
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.FragmentDetailBinding
import com.listgithubusersinglescreen.helper.FollowType
import com.listgithubusersinglescreen.helper.ResultStatus
import com.listgithubusersinglescreen.ui.adapter.SectionsPagerAdapter
import com.listgithubusersinglescreen.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : Fragment() {

    private var userNodeId: String = ""
    private var userLogin: String = ""

    private var urlString: String? = null
    private var isLoved: Boolean = false
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = DetailFragmentArgs.fromBundle(arguments as Bundle)
        userNodeId = bundle.userNodeId
        userLogin = bundle.userLogin

        observeLoved(viewModel, userNodeId)

        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        (requireActivity() as MainActivity).onBack()
                        true
                    }
                    R.id.share -> {
                        startActivity(
                            Intent.createChooser(
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, urlString)
                                    type = getString(R.string.type_share)
                                }, getString(R.string.share_to)
                            )
                        )
                        true
                    }
                    R.id.setting -> {
                        findNavController()
                            .navigate(R.id.action_detailFragment_to_settingsFragment)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner)

        viewModel.getUser(userLogin, userNodeId).observe(viewLifecycleOwner) { result ->
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

        binding.favBtn.setOnClickListener {
            viewModel.setLovedUser(userNodeId, !isLoved)
        }

    }

    private fun observeLoved(viewModel: DetailViewModel, userNodeId: String) {
        viewModel.isLovedUser(userNodeId).observe(viewLifecycleOwner) {
            Log.e("DetailActivity", "observe it :$it")
            isLoved = it
            if (it) {
                binding.favBtn.setImageResource(R.drawable.ic_love_active_white_24)
            } else {
                binding.favBtn.setImageResource(R.drawable.ic_love_nonactive_white_24)
            }
        }
    }

    private fun setUsersData(user: UserEntity) {
        binding.apply {
            urlString = user.url
            detUserText.text = user.name
            Glide.with(requireActivity())
                .load(user.avatarUrl)
                .into(detPhotoUser)

            val sectionsPagerAdapter = SectionsPagerAdapter(
                (requireActivity() as AppCompatActivity),
                userLogin,
                userNodeId
            )
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text =
                            FollowType.Follower.name + " (" + user.followers + ")"
                    }
                    1 -> {
                        tab.text =
                            FollowType.Following.name + " (" + user.following + ")"
                    }
                }
            }.attach()
        }
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
}