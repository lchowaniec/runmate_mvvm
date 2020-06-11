package com.lchowaniec.runmate_me.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import kotlinx.android.synthetic.main.friend_search_item.view.*

class searchAdapter(private val context: Context, var itemClickListener:onItemClickListener): RecyclerView.Adapter<searchAdapter.MainViewHolder>() {

    private var dataList = mutableListOf<User>()
    fun setListData(data: MutableList<User>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_search_item,parent,false)
        return MainViewHolder(view)

    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user, itemClickListener)
    }







    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


        fun bindView(user: User, clickListener: onItemClickListener){
            Glide.with(context).load(user.profile_photo).into(itemView.friend_item_photo)
            itemView.friend_item_username.text = user.username
            itemView.friend_item_desc.text = user.description
            itemView.setOnClickListener{
                clickListener.onItemClick(user)
            }

        }




    }




    interface onItemClickListener{
        fun onItemClick(user: User)
    }

}