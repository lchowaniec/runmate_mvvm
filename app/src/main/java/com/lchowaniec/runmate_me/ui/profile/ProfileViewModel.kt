package com.lchowaniec.runmate_me.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.data.repository.profile.ProfileRepository
import com.lchowaniec.runmate_me.util.FriendDeleteListener
import io.reactivex.ObservableEmitter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class ProfileViewModel : ViewModel(){


    private val profileRepository: ProfileRepository? = ProfileRepository().getInstance()
    var uploadListener: UploadListener? = null
    var friendDeleteListener: FriendDeleteListener? = null

    // disposable to dispose the Completable
    private val disposables = CompositeDisposable()
    val user by lazy{
        profileRepository?.currentUser()
    }
    fun logout() = profileRepository?.logout()
    fun getRequests():LiveData<List<User>>?{
        return profileRepository?.getRequests()
    }
    fun getFriends(): LiveData<List<User>>?{
        return profileRepository?.getFriends()
    }


    fun getUser(): LiveData<User>? {
        Log.d("TAG","ProfileViewModel: getUser")
        return profileRepository?.getUserData()
    }
    fun checkUser(username: String){
         profileRepository?.checkUser(username)
    }
    fun updateDescription(description:String){
        profileRepository?.updateDescription(description)
    }
    fun getState(id:String?): LiveData<String>? {
        return profileRepository?.getState(id)
    }

    fun sendRequest(id:String?){
        if(id!=null){
            val disposable = profileRepository?.sendRequest(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    uploadListener?.onSuccess()
                },{
                    uploadListener?.onFailure(it.message!!)
                })
            if(disposable!=null){
                disposables.add(disposable)
            }
        }
    }
    fun deleteFriend(id:String?){
        if(id!=null){
            val disposable = profileRepository?.deleteFriend(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    friendDeleteListener?.onFriendDeleted()
                },{
                    friendDeleteListener?.onFailure(it.message!!)
                })
            if(disposable!=null){
                disposables.add(disposable)
            }
        }
    }
    fun addFriend(id:String?){
        if(id!=null){
            val disposable = profileRepository?.addFriend(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    uploadListener?.onSuccess()
                },{
                    uploadListener?.onFailure(it.message!!)
                })
            if(disposable!=null){
                disposables.add(disposable)
            }
        }
    }
    fun deleteRequest(id:String?){
        if(id!=null){
            val disposable = profileRepository?.deleteRequest(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    uploadListener?.onSuccess()
                },{
                    uploadListener?.onFailure(it.message!!)
                })
            if(disposable!=null){
                disposables.add(disposable)
            }
        }
    }



    fun uploadPhoto(uri: Uri?){
        if(uri!= null){
            val disposable = profileRepository?.uploadPhoto(uri)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    uploadListener?.onSuccess()

                },{
                    uploadListener?.onFailure(it.message!!)
                })
            if(disposable!= null){
                disposables.add(disposable)
            }

        }

    }
    fun searchFriend(query: String):LiveData<MutableList<User>>  {
        val mutableData = MutableLiveData<MutableList<User>>()
        profileRepository?.searchFriend(query)?.observeForever {
            mutableData.value = it
        }
        return mutableData

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }


}
