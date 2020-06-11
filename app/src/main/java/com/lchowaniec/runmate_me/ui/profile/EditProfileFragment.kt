package com.lchowaniec.runmate_me.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iceteck.silicompressorr.FileUtils
import com.iceteck.silicompressorr.SiliCompressor


import com.lchowaniec.runmate_me.R
import com.lchowaniec.runmate_me.data.models.User
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.File


class EditProfileFragment : BaseProfileFragment(),UploadListener {
    private lateinit var viewModel:ProfileViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.uploadListener = this
        setUser()
        edit_confirm_username.setOnClickListener{
                viewModel.checkUser(edit_username.text.toString().toLowerCase())
        }
        edit_confirm_description.setOnClickListener{
            viewModel.updateDescription(edit_description.text.toString())
        }
        edit_profile_change_photo.setOnClickListener{
            startImage()
        }
        edit_profile_photo.setOnClickListener{
            startImage()
        }
    }
    private fun startImage(){
        val permissionCheck =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 1)
        }
    }

    private fun setUser(){
        if(requireArguments() != null){
            user = requireArguments().getParcelable<User>("user")!!
            val mUsername = view?.findViewById<TextView>(R.id.edit_username)
            mUsername?.text = user.username
            val mDescription = view?.findViewById<TextView>(R.id.edit_description)
            mDescription?.text = user.description
            Glide.with(this).load(user.profile_photo).into(edit_profile_photo)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent, 1)            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val picked_uri = data?.data
            if (picked_uri != null) {
                val file = File(
                    SiliCompressor.with(requireContext())
                        .compress(
                            FileUtils.getPath(
                                requireContext(),
                                picked_uri
                            ), File(requireContext().cacheDir, "temp")
                        )
                )
                val uri = Uri.fromFile(file)
                viewModel.uploadPhoto(uri)
            }

        }
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)


    }

    override fun onSuccess() {
        Toast.makeText(requireContext(),"Image uploaded succesfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onStarted() {
        Toast.makeText(requireContext(),"Choose your new image", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(requireContext(),"Something gone wrong, try again", Toast.LENGTH_SHORT).show()
    }
}
