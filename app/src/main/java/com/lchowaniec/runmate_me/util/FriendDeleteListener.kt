package com.lchowaniec.runmate_me.util

interface FriendDeleteListener {
    fun onFriendDeleted()
    fun onFailure(message:String)
}