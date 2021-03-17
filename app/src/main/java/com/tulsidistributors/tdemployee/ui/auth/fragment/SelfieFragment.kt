package com.tulsidistributors.tdemployee.ui.auth.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentSelfieBinding
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import java.io.File
import java.security.Permission
import java.security.Permissions


class SelfieFragment : Fragment() {

    lateinit var selfieImage: ImageView
    lateinit var nextBtn:ImageView
    var FILE_NAME = "tdselfie.jpg"
    lateinit var photoFile: File
    lateinit var binding: FragmentSelfieBinding

    companion object {
        private const val CAMERA_PREMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelfieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextBtn.setOnClickListener {
            val intent = Intent(context,HomePageActivity:: class.java)
            startActivity(intent)
        }

        binding.selfieImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                try {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }catch (e:Exception){
                    Toast.makeText(requireContext(), "Exception $e", Toast.LENGTH_SHORT).show()
                }

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PREMISSION_CODE
                )
            }
        }


    }


    

}