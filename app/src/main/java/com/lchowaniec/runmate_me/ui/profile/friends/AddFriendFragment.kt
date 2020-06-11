package com.lchowaniec.runmate_me.ui.profile.friends


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.adapters.searchAdapter
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_add_friend.*



class AddFriendFragment : Fragment(), searchAdapter.onItemClickListener {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var adapter: searchAdapter

    override fun onItemClick(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user",user)
        findNavController().navigate(R.id.action_baseFriendFragment_to_friendProfileFragment,bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_friend, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setupRecyclerView()
        initSearch()

    }
    fun setupRecyclerView() {
        adapter = searchAdapter(requireContext(),this)
        friends_list.layoutManager = LinearLayoutManager(requireContext())
        friends_list.adapter = adapter

    }


    private fun initSearch(){
        search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length!! > 1){
                    viewModel.searchFriend(newText).observe(viewLifecycleOwner, Observer {
                        adapter.setListData(it)
                        adapter.notifyDataSetChanged()

                    })
                }
                return false
            }

        })

    }




}
