package com.lchowaniec.runmate_me.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.lchowaniec.runmate_me.R
import kotlinx.android.synthetic.main.fragment_password.*


class PasswordFragment : Fragment(), AuthListener {
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.authListener = this
        reset_button.setOnClickListener{
            viewModel.email = input_email.text.toString()
            viewModel.resetPassword()
        }

    }

    override fun onSuccess() {
        progress_bar_reset.visibility = View.GONE
        Toast.makeText(requireContext(),"Email sent. Check your inbox", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_passwordFragment_to_startFragment)

    }

    override fun onStarted() {
        progress_bar_reset.visibility = View.VISIBLE

    }

    override fun onFailure(message: String) {
        Toast.makeText(requireContext(),"Something went wrong, try again.", Toast.LENGTH_LONG).show()
        progress_bar_reset.visibility = View.GONE
    }


}
