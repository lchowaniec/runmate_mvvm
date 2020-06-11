package com.lchowaniec.runmate_me.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lchowaniec.runmate_me.data.models.FriendRequest
import com.lchowaniec.runmate_me.data.models.User
import com.lchowaniec.runmate_me.data.models.friend_status
import io.reactivex.Completable
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class FirebaseSource {
    private val TAG = "FirebaseSource"
    private var instance: FirebaseSource? = null
    private var mUser: MutableLiveData<User> = MutableLiveData()
    private var mFriends= mutableListOf<User>()

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val currentID by lazy {
        firebaseAuth.currentUser?.uid
    }

    private val mFirebaseDatabase: DatabaseReference by lazy {
       FirebaseDatabase.getInstance().reference
    }
    private val mFirebaseStorage: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }
    fun getInstance(): FirebaseSource? {
        if(instance == null){
            instance = FirebaseSource()
        }
        return instance
    }
    fun login(email:String, password: String) = Completable.create{ emitter ->

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(!emitter.isDisposed){
                if(it.isSuccessful){
                        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                            mFirebaseDatabase.child("user").child(firebaseAuth.currentUser!!.uid).child("token").setValue(it.token)
                                .addOnSuccessListener {
                                    emitter.onComplete()
                                }.addOnFailureListener{
                                    emitter.onError(it)
                                }
                        }
                    }
                else
                    emitter.onError(it.exception!!)
            }
        }
    }
    fun getFriends(): MutableLiveData<List<User>>?{
        var friends: MutableLiveData<List<User>>? = MutableLiveData()
        var ref = mFirebaseDatabase
            .child("friends")
            .child(currentID.toString())
        ref.keepSynced(true)
        ref.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = ArrayList<String>()
                    var userList = mutableListOf<User>()
                    if(!p0.exists()){
                        friends?.value = emptyList()
                    }
                    for(friend in p0.children){
                        list.add(friend.key.toString())
                    }

                    for(friend in list){
                        val ref2 = mFirebaseDatabase
                            .child("user")
                            .orderByChild("user_id")
                            .equalTo(friend)
                        ref2.keepSynced(true)

                        ref2.addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for(each in p0.children) {
                                        userList.add(each.getValue(User::class.java)!!)
                                    }
                                    friends?.value = userList

                                }
                            })
                    }
                }
            })
        return friends
    }
    fun getRequests(): MutableLiveData<List<User>>? {
        var requests: MutableLiveData<List<User>>? = MutableLiveData()
        mFirebaseDatabase
            .child("friends_req")
            .child(currentID.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var list = ArrayList<String>()
                    var userList = mutableListOf<User>()
                    if(!p0.exists()){
                        requests?.value = emptyList()
                    }
                    for(each in p0.children){
                        if(each.getValue(FriendRequest::class.java)?.status == "RECEIVED") {
                            list.add(each.getValue(FriendRequest::class.java)?.id.toString())
                        }
                    }
                    for(id in list){
                        mFirebaseDatabase
                            .child("user")
                            .orderByChild("user_id")
                            .equalTo(id)
                            .addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for(each in p0.children){
                                        userList.add(each.getValue(User::class.java)!!)
                                    }
                                    requests?.value = userList
                                }
                            })
                    }
                }

            })

        return requests

    }


    fun register(email:String,password: String, username: String) = Completable.create{ emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if(!emitter.isDisposed){
                if(it.isSuccessful) {
                    emitter.onComplete()
                }
                else
                    emitter.onError(it.exception!!)
            }
        }.addOnSuccessListener {
            val mUserId: String = firebaseAuth.currentUser?.uid.toString()
            val mUser = User(mUserId,email,username.toLowerCase())
            mFirebaseDatabase
                .child("user")
                .child(mUserId)
                .setValue(mUser)
                .addOnSuccessListener {
                    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                        mFirebaseDatabase.child("user").child(firebaseAuth.currentUser!!.uid).child("token").setValue(it.token)
                            .addOnSuccessListener {
                                emitter.onComplete()
                            }.addOnFailureListener{
                                emitter.onError(it)
                            }
                    }
                }
        }

    }
    fun resetPassword(email: String)= Completable.create{ emitter ->
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{
            if(!emitter.isDisposed){
                if(it.isSuccessful){
                    emitter.onComplete()
                }
                else
                    emitter.onError(it.exception!!)
            }
        }
    }
    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun getState(id: String?): LiveData<String>{
        val string = MutableLiveData<String>()
        if(currentID!=null) {
            mFirebaseDatabase.child("friends_req")
                .child(currentID.toString())
                .child(id.toString())
                .addValueEventListener(object: ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(!p0.exists()){
                            mFirebaseDatabase.child("friends")
                                .child(currentID.toString())
                                .child(id.toString())
                                .addValueEventListener(object: ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if(!p0.exists()){
                                            string.value = "NOT_FRIEND"
                                        }else{
                                            string.value = "FRIENDS"
                                        }
                                    }

                                })

                        }else{
                            string.value = p0.getValue(FriendRequest::class.java)?.status
                        }
                    }
                })
        }
        return string

    }

    fun searchFriend(query: String?): LiveData<MutableList<User>> {
        val mutableData = MutableLiveData<MutableList<User>>()
        mFirebaseDatabase.child("user")
            .orderByChild("username").startAt(query).endAt(query + "\uf8ff")
            .addValueEventListener(object: ValueEventListener{
                val listData = mutableListOf<User>()
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(each in p0.children){
                    listData.add(each.getValue(User::class.java)!!)
                }
                mutableData.value = listData
            }
        })
        return mutableData
    }


    fun setUser(): LiveData<User> {
        Log.d(TAG,"setUserd: inside ")
        val mUserId = firebaseAuth.currentUser?.uid.toString()
        val ref = mFirebaseDatabase
            .child("user")
            .child(mUserId)
        ref.keepSynced(true)
            ref.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d(TAG,"onCancelled: "+ p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    Log.d(TAG,"onDataChange: "+ p0)
                    mUser.value = p0.getValue(User::class.java)
                }
            })

        return mUser
    }
    fun uploadUserPhoto(uri:String?){
        Log.d(TAG," uploadUserPhoto: " + uri)
        mFirebaseDatabase.child("user")
            .child(firebaseAuth.currentUser!!.uid)
            .child("profile_photo")
            .setValue(uri)
    }
    fun checkUser(username:String){


            val ref = mFirebaseDatabase.child("user").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            Log.d(TAG,"updateUser: onDataChange : USER EXISTS + $username")
                        }else{
                            updateUsername(username)
                        }
                    }
                })


    }
    fun updateUsername(username:String){
        if(currentID != null) {
            mFirebaseDatabase.child("user")
                .child(currentID!!)
                .child("username")
                .setValue(username.toLowerCase())

        }

    }
    fun updateDescription(description:String){

        if(currentID != null) {
            mFirebaseDatabase.child("user")
                .child(currentID!!)
                .child("description")
                .setValue(description)
        }
    }
    fun uploadPhoto(uri: Uri?) = Completable.create{ emitter ->
        if(uri != null){
            val reference = mFirebaseStorage.child(currentID.toString()).child("profile_photo").child("photo.png")
            reference.putFile(uri).addOnSuccessListener {
                val result = it.storage.downloadUrl.addOnSuccessListener {
                    uploadUserPhoto(it.toString())
                    if(!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }.addOnFailureListener{
                    emitter.onError(it)
                }
            }
        }
    }
    fun sendRequest(id:String?) = Completable.create{emitter->
        if(id!=null){
            val requestSend = FriendRequest()
            requestSend.id = id
            requestSend.status = "SENT"
            val requestReceived = FriendRequest()
            requestReceived.id = id
            requestReceived.status = "RECEIVED"

            mFirebaseDatabase
                .child("friends_req")
                .child(currentID.toString())
                .child(id)
                .setValue(requestSend)
                .addOnSuccessListener {
                    mFirebaseDatabase
                        .child("friends_req")
                        .child(id)
                        .child(currentID.toString())
                        .setValue(requestReceived)
                        .addOnSuccessListener {
                            val notificationData = HashMap<String,String>()
                            notificationData.put("from", currentID.toString())
                            notificationData.put("type","request")
                            mFirebaseDatabase.child("notification").child(id).push().setValue(notificationData)
                                .addOnSuccessListener {
                                    if(!emitter.isDisposed){
                                        emitter.onComplete()
                                    }
                                }

                        }
                        .addOnFailureListener{
                            emitter.onError(it)
                        }

                }.addOnFailureListener{
                    emitter.onError(it)
                }
        }
    }
    fun deleteFriend(id:String?) = Completable.create{emitter->
        if(id!=null){
            mFirebaseDatabase
                .child("friends")
                .child(currentID.toString())
                .child(id)
                .removeValue()
                .addOnSuccessListener {
                    mFirebaseDatabase
                        .child("friends")
                        .child(id)
                        .child(currentID.toString())
                        .removeValue()
                        .addOnSuccessListener {
                            if(!emitter.isDisposed){
                                emitter.onComplete()
                            }
                        }.addOnFailureListener{
                            emitter.onError(it)
                        }
                }
        }
    }
    fun addFriend(id:String?) = Completable.create{emitter->
        val currentDate = DateFormat.getDateTimeInstance().format(Date())
        if(id!=null){
            mFirebaseDatabase
                .child("friends")
                .child(currentID.toString())
                .child(id)
                .setValue(currentDate)
                .addOnSuccessListener {
                    mFirebaseDatabase
                        .child("friends")
                        .child(id)
                        .child(currentID.toString())
                        .setValue(currentDate)
                        .addOnSuccessListener {
                            if(!emitter.isDisposed){
                                mFirebaseDatabase
                                    .child("friends_req")
                                    .child(currentID.toString())
                                    .child(id)
                                    .removeValue()
                                    .addOnSuccessListener {
                                        mFirebaseDatabase
                                            .child("friends_req")
                                            .child(id)
                                            .child(currentID.toString())
                                            .removeValue()
                                            .addOnSuccessListener {
                                                if(!emitter.isDisposed){
                                                    emitter.onComplete()
                                                }
                                            }
                                            .addOnFailureListener{
                                                emitter.onError(it)
                                            }

                                    }
                                    .addOnFailureListener{
                                        emitter.onError(it)
                                    }
                            }
                        }
                        .addOnFailureListener{
                            emitter.onError(it)
                        }

                }.addOnFailureListener{
                    emitter.onError(it)
                }
        }
    }
    fun deleteRequest(id:String?) = Completable.create{emitter ->
        if(id!=null){
            mFirebaseDatabase
                .child("friends_req")
                .child(currentID.toString())
                .child(id)
                .removeValue()
                .addOnSuccessListener {
                    mFirebaseDatabase
                        .child("friends_req")
                        .child(id)
                        .child(currentID.toString())
                        .removeValue()
                        .addOnSuccessListener {
                            if(!emitter.isDisposed){
                                emitter.onComplete()
                            }
                        }
                        .addOnFailureListener{
                            emitter.onError(it)
                        }

                }
                .addOnFailureListener{
                    emitter.onError(it)
                }
        }
    }
}