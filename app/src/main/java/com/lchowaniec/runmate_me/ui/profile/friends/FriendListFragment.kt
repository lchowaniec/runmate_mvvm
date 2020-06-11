package com.lchowaniec.runmate_me.ui.profile.friends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.adapters.FriendsAdapter
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_friend_list.*

/**
 * A simple [Fragment] subclass.
 */
class FriendListFragment : Fragment(), FriendsAdapter.onClickListener {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initRecycler()
        initObservers()
    }
    private fun initRecycler(){
        adapter = FriendsAdapter(requireContext(),this)
        friend_all_recycler.layoutManager = LinearLayoutManager(requireContext())
        friend_all_recycler.adapter = adapter
    }
    private fun initObservers(){
        viewModel.getFriends()?.observe(viewLifecycleOwner, Observer {
            Log.d("TAG","initObservers: Observer: userList: $it")
            for(each in it){
                Log.d("TAG","initObservers: Observer: userList: ${each.username}")

            }
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onFriendClick(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user",user)
        findNavController().navigate(R.id.action_baseFriendFragment_to_friendProfileFragment,bundle)
    }

}
