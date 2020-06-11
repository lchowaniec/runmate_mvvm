package com.lchowaniec.runmate_me

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class runmate : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

}