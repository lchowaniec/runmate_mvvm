package com.lchowaniec.runmate_me.ui.auth

import androidx.lifecycle.ViewModel
import com.lchowaniec.runmate_me.data.repository.auth.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel (): ViewModel()
{
    private val authRepository: AuthRepository? = AuthRepository().getInstance()

    //inputs
    var email: String? = null
    var username: String? = null
    var password: String? = null

    //authListener
    var authListener: AuthListener? = null
    //disposable to dispote the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        authRepository?.currentUser()
    }
    fun login(){
        if(email.isNullOrEmpty()|| password.isNullOrEmpty() ){
            authListener?.onFailure("Invalid email or password")
            return
        }
        authListener?.onStarted()

        val disposable = authRepository?.login(email!!,password!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    authListener?.onSuccess()
                },
                {
                    authListener?.onFailure(it.message!!)
                }
            )
        if(disposable!= null){
            disposables.add(disposable)
        }
    }
    fun register(){
        if(email.isNullOrEmpty()  || password.isNullOrEmpty() || username.isNullOrEmpty() ){
            authListener?.onFailure("invalid input")
            return

        }
        authListener?.onStarted()

        val disposable = authRepository?.register(email!!,password!!, username!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                authListener?.onSuccess()
            },{
                authListener?.onFailure(it.message!!)
            })
        if(disposable!= null){
            disposables.add(disposable)
        }
    }
    fun logout(){
        authRepository?.logout()
    }
    fun resetPassword(){
        if(email.isNullOrEmpty()){
            authListener?.onFailure("Wrong email, please try again")
            return
        }
        authListener?.onStarted()

        val disposable : Disposable?= authRepository?.resetPassword(email!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                authListener?.onSuccess()

            },{
                authListener?.onFailure(it.message!!)
            })
        if(disposable!= null){
            disposables.add(disposable)
        }

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}