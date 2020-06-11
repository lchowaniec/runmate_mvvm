package com.lchowaniec.runmate_me.ui.main

import androidx.lifecycle.ViewModel
import com.lchowaniec.runmate_me.data.repository.auth.AuthRepository

class MainViewModel() : ViewModel()
{
    private val repository: AuthRepository? = AuthRepository().getInstance()

    val user by lazy{
        repository?.currentUser()
    }
    fun logout(){
        repository?.logout()
    }
}