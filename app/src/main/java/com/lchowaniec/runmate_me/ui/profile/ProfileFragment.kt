package com.lchowaniec.runmate_me.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.util.startAuthActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.snippet_profile_stats.*


class ProfileFragment : BaseProfileFragment() {
    private lateinit var viewModel:ProfileViewModel
    private var mUser: User? = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_edit.setOnClickListener{
            navEdit()
        }
        add_friend.setOnClickListener{
            navFriendsTab()
        }
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.getUser()?.observe(viewLifecycleOwner,object: Observer<User>{
            override fun onChanged(t: User?) {
                mUser = t
                bindUI(t)
            }

        })
        profile_logout.setOnClickListener{
            viewModel.logout()
            context?.startAuthActivity()
        }



    }
    private fun bindUI(user: User?){
        if(user!= null){
            show_activities.text =user.activities.toString()
            show_distance.text = user.distance.toString()
            show_friends.text = user.friends.toString()
            user_username.text = user.username
            Glide.with(this).load(user.profile_photo).into(profile_image)
        }



    }
    private fun navEdit(){
        if(findNavController().currentDestination?.id == R.id.profileFragment) {
            val bundle = Bundle()
            bundle.putParcelable("user", mUser)
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment,bundle)


        }
    }
    private fun navFriendsTab(){
        if(findNavController().currentDestination?.id == R.id.profileFragment) {
            findNavController().navigate(R.id.action_profileFragment_to_baseFriendFragment)
        }
    }




}
