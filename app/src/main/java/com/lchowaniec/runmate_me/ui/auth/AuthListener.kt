package com.lchowaniec.runmate_me.ui.auth

interface AuthListener {
    fun onSuccess()
    fun onStarted()
    fun onFailure(message:String)
}