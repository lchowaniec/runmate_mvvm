package com.lchowaniec.runmate_me.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.util.startMainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(),AuthListener {

    private lateinit var  viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return  inflater.inflate(R.layout.fragment_login, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =  ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.authListener = this

        login_button.setOnClickListener{
            viewModel.email = input_email.text.toString()
            viewModel.password = input_password.text.toString()
            viewModel.login()

        }
    }

    override fun onSuccess() {
        activity?.startMainActivity()

    }

    override fun onStarted() {
        progress_bar_login.visibility = View.VISIBLE
    }

    override fun onFailure(message: String) {
        progress_bar_login.visibility = View.GONE
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


}




