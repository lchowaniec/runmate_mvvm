package com.lchowaniec.runmate_me.data.repository.auth

import com.lchowaniec.runmate_me.data.firebase.FirebaseSource

class AuthRepository
{
    private var instance: AuthRepository? = null
    private var firebase: FirebaseSource? = FirebaseSource()
        .getInstance()

    fun getInstance(): AuthRepository?{
        if(instance == null){
            instance = AuthRepository()
        }
        return instance
    }
    fun login(email: String, password: String) = firebase?.login(email,password)
    fun register(email: String,password: String, username: String) = firebase?.register(email,password,username)
    fun currentUser() = firebase?.currentUser()
    fun logout() = firebase?.logout()
    fun resetPassword(email: String) = firebase?.resetPassword(email)
}