package com.lchowaniec.runmate_me.ui.profile

interface UploadListener {
    fun onSuccess()
    fun onStarted()
    fun onFailure(message:String)
}