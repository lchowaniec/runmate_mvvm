package com.lchowaniec.runmate_me.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import kotlinx.android.synthetic.main.friend_search_item.view.*

class FriendsAdapter(val context: Context, var onItemClickListener: onClickListener): RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {

    private var dataList = listOf<User>()
    fun setListData(data:List<User>){
        dataList = data
    }

    inner class FriendHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(user: User, onClickListener: onClickListener){
            Glide.with(context).load(user.profile_photo).into(itemView.friend_item_photo)
            itemView.friend_item_username.text = user.username
            itemView.setOnClickListener{
                onClickListener.onFriendClick(user)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_search_item,parent,false)
        return FriendHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user,onItemClickListener)
    }


    interface onClickListener{
        fun onFriendClick(user: User)
    }
}