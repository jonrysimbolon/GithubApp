package com.listgithubusersinglescreen.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.listgithubusersinglescreen.ui.follow.UserFollowFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    var user: String,
    var nodeId: String,
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragment = UserFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserFollowFragment.ARG_SECTION_NUMBER, position)
            putString(UserFollowFragment.USER, user)
            putString(UserFollowFragment.NODE_ID, nodeId)
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}