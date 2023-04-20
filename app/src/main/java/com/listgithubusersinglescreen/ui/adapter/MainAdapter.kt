package com.listgithubusersinglescreen.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.listgithubusersinglescreen.data.local.entity.UserEntity
import com.listgithubusersinglescreen.databinding.ActivityMainAdapterBinding

class MainAdapter(
    private val isFav: Boolean ? = null,
    private val onClickLove: ((UserEntity) -> Unit)? = null,
    private val onClickItem: ((UserEntity) -> Unit)? = null
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val users = ArrayList<UserEntity>()

    fun setListUsers(users: List<UserEntity>) {
        val diffCallback = UserDiffCallback(this.users, users)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.users.clear()
        this.users.addAll(users)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: ActivityMainAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ActivityMainAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(users[position]) {
            holder.binding.userText.text = login
            Glide.with(holder.itemView)
                .load(avatarUrl)
                .into(holder.binding.photoUser)
        }

        if(isFav != null){
            holder.binding.loveBtn.visibility = View.VISIBLE
        }else{
            holder.binding.loveBtn.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClickItem?.let { userEntityUnit ->
                userEntityUnit(users[position])
            }
        }

        holder.binding.loveBtn.setOnClickListener {
            onClickLove?.let { userEntityUnit ->
                userEntityUnit(users[position])
            }
        }
    }
}