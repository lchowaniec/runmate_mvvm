package com.lchowaniec.runmate_me.ui.profile.friends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.ui.profile.ProfileViewModel
import com.lchowaniec.runmate_me.ui.profile.UploadListener
import com.lchowaniec.runmate_me.util.FriendDeleteListener
import kotlinx.android.synthetic.main.fragment_friend_profile.*


class FriendProfileFragment : Fragment(),
    UploadListener, FriendDeleteListener {
    private val TAG = "FriendProfileFragment"
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mCurrentState: String
    private var mUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.uploadListener = this
        viewModel.friendDeleteListener = this
        getUser()

    }
    fun getUser(){
        if(arguments!=null){
            mUser = requireArguments().getParcelable<User>("user")
            Glide.with(this).load(mUser?.profile_photo).into(friend_profile_photo)
            friend_profile_username.text = mUser?.username
            friend_profile_activities.text = mUser?.activities.toString()
            friend_profile_friends.text = mUser?.friends.toString()
            friend_profile_distance.text = mUser?.distance.toString()
            viewModel.getState(mUser?.user_id)?.observe(viewLifecycleOwner, Observer {
                getButton(it)
            })

        }


    }

    private fun getButton(it: String?) {
        Log.d(TAG,"getButton: $it")
        if(mUser?.user_id != viewModel.user?.uid) {
            if (it == "NOT_FRIEND") {
                friend_profile_button_send.text = "Send friend request"
                friend_profile_button_send.visibility = View.VISIBLE
                friend_profile_button_delete.visibility = View.GONE
                friend_profile_button_send.setOnClickListener {
                    viewModel.sendRequest(mUser?.user_id)
                }
            } else if (it == "SENT") {
                friend_profile_button_send.text = "Request sent"
                friend_profile_button_send.visibility = View.VISIBLE
                friend_profile_button_delete.visibility = View.VISIBLE
                friend_profile_button_delete.setOnClickListener {
                    viewModel.deleteRequest(mUser?.user_id)
                }
            } else if (it == "RECEIVED"){
                friend_profile_button_send.text = "Accept request"
                friend_profile_button_delete.text = "Decline request"
                friend_profile_button_send.visibility = View.VISIBLE
                friend_profile_button_delete.visibility = View.VISIBLE
                friend_profile_button_send.setOnClickListener{
                    viewModel.addFriend(mUser?.user_id)

                }
                friend_profile_button_delete.setOnClickListener{
                    viewModel.deleteRequest(mUser?.user_id)
                }


            }else if (it =="FRIENDS"){
                friend_profile_button_send.visibility = View.GONE
                friend_profile_button_delete.visibility = View.VISIBLE
                friend_profile_button_delete.text = "Delete from friends"
                friend_profile_button_delete.setOnClickListener{
                    viewModel.deleteFriend(mUser?.user_id)
                }

            }
        }

    }

    override fun onSuccess() {
        Toast.makeText(requireContext(),"Request sent.", Toast.LENGTH_SHORT).show()

    }

    override fun onStarted() {
    }

    override fun onFriendDeleted() {
        Toast.makeText(requireContext(),"Friend deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Log.d(TAG,"onFailure: $message")
        Toast.makeText(requireContext(),"Try again", Toast.LENGTH_SHORT).show()
    }

}
