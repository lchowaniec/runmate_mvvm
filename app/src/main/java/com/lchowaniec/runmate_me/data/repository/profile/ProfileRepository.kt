package com.lchowaniec.runmate_me.data.repository.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lchowaniec.runmate_me.data.firebase.FirebaseSource
import com.lchowaniec.runmate_me.data.models.User

class ProfileRepository(

) {
    private var instance: ProfileRepository? = null
    private var firebase : FirebaseSource? = FirebaseSource().getInstance()

    fun getInstance(): ProfileRepository?{
        if(instance == null){
            instance = ProfileRepository()
        }
        return instance
    }

    fun getUserData(): LiveData<User>?{
        Log.d("TAG", " ProfileRepository: getUserData")
        return firebase?.setUser()
    }
    fun searchFriend(query: String): LiveData<MutableList<User>>? {
        return firebase?.searchFriend(query)
    }
    fun getFriends() = firebase?.getFriends()
    fun logout() = firebase?.logout()
    fun getRequests() = firebase?.getRequests()
    fun currentUser() = firebase?.currentUser()
    fun sendRequest(id:String?)= firebase?.sendRequest(id)
    fun addFriend(id:String?)= firebase?.addFriend(id)
    fun deleteRequest(id:String?) = firebase?.deleteRequest(id)
    fun uploadPhoto(uri: Uri?) = firebase?.uploadPhoto(uri)
    fun checkUser(username: String) = firebase?.checkUser(username)
    fun deleteFriend(id:String?) = firebase?.deleteFriend(id)
    fun updateDescription(description: String) = firebase?.updateDescription(description)
    fun getState(id: String? ): LiveData<String>? {
        return firebase?.getState(id)
    }


}