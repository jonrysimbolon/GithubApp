package com.listgithubusersinglescreen.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.listgithubusersinglescreen.data.local.entity.UserEntity

class UserDiffCallback(
    private val mOldUserList: List<UserEntity>,
    private val mNewUserList: List<UserEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldUserList.size

    override fun getNewListSize(): Int = mNewUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldUserList[oldItemPosition].login == mNewUserList[newItemPosition].login

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUserList[oldItemPosition]
        val newUser = mNewUserList[newItemPosition]

        return oldUser.login == newUser.login && oldUser.name == newUser.name
    }

}