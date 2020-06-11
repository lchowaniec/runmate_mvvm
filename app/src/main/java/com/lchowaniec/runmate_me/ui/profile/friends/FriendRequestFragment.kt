package com.lchowaniec.runmate_me.ui.profile.friends

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.adapters.RequestAdapter
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_friend_request.*
import kotlinx.android.synthetic.main.friend_request_item.*


class FriendRequestFragment : Fragment(),RequestAdapter.onClickListener {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: RequestAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        Log.d("TAG","onViewCreated ${findNavController().currentDestination}" )
        initRecycler()
        viewModel.getRequests()?.observe(viewLifecycleOwner, Observer {
            if(it == null){
                adapter.notifyDataSetChanged()
            }
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })


    }
    private fun initRecycler(){
        adapter = RequestAdapter(requireContext(),this)
        friend_requests_recycler.layoutManager = LinearLayoutManager(requireContext())
        friend_requests_recycler.adapter = adapter

    }

    override fun onAddClick(user: User) {
        viewModel.addFriend(user.user_id)
    }

    override fun onDeleteClick(user: User) {
        viewModel.deleteRequest(user.user_id)
    }


}
