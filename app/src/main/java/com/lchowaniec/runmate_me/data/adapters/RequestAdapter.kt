package com.lchowaniec.runmate_me.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import kotlinx.android.synthetic.main.friend_request_item.view.*
import kotlinx.android.synthetic.main.friend_search_item.view.*

class RequestAdapter(val context: Context, var onItemClickListener: onClickListener): RecyclerView.Adapter<RequestAdapter.RequestHolder>() {
    private var dataList = listOf<User>()
    fun setListData(data:List<User>){
        dataList = data
    }

    inner class RequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(user: User, onClickListener: onClickListener){
            Glide.with(context).load(user.profile_photo).into(itemView.friend_request_photo)
            itemView.friend_request_username.text = user.username
            itemView.friend_request_accept.setOnClickListener{
                onClickListener.onAddClick(user)
            }
            itemView.friend_request_decline.setOnClickListener{
                onClickListener.onDeleteClick(user)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_request_item,parent,false)
        return RequestHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user,onItemClickListener)
    }


    interface onClickListener{
        fun onAddClick(user: User)
        fun onDeleteClick(user:User)
    }
}