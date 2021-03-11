package com.tulsidistributors.tdemployee.ui.auth.fragment

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
                photoFile = getPhotoFile(FILE_NAME)

                val fileProvider = FileProvider.getUriForFile(
                    requireContext(),
                        "com.tulsidistributors.fileProvider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                startActivityForResult(intent, CAMERA_REQUEST_CODE)


            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PREMISSION_CODE
                )
            }
        }


    }

    private fun getPhotoFile(fileName: String): File {

        //Use `getExternalFilesDir` on Context to access paackage-specific directories
        // this is the storageDir where we r going to save photo
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == CAMERA_REQUEST_CODE) {

            val image = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.selfieImage.setImageBitmap(image)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PREMISSION_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)

            } else {
                Toast.makeText(
                    context,
                    "Permission denied! Kindly grant permissions from settings",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    

}