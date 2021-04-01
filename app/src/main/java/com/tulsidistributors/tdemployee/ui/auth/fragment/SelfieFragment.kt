package com.tulsidistributors.tdemployee.ui.auth.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import com.leo.simplearcloader.SimpleArcLoader
import com.tulsidistributors.tdemployee.databinding.FragmentSelfieBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import com.tulsidistributors.tdemployee.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SelfieFragment : Fragment(), UploadImageRequestBody.UploadCallback {

    lateinit var selfieImage: ImageView
    lateinit var nextBtn: ImageView
    var FILE_NAME = "tdselfie.jpg"
    lateinit var photoFile: File
    lateinit var binding: FragmentSelfieBinding
    val REQUEST_CODE: Int = 10
    lateinit var currentPhotoPath: String
    var uri: Uri? = null
    var latitude: Double? = null
    var longtitude: Double? = null
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var msettingsClient: SettingsClient
    private lateinit var locationCallback: LocationCallback
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var empId: String
    lateinit var token:String
    lateinit var currentDate:String

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


        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getUserDetail()

         currentDate = Common().getCurrentDate("yyyy-MM-dd")

        showToast(requireContext(),"Current Date : $currentDate")

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        //initalize the LocationRequest
        locationRequest = LocationRequest.create()
        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        getLocation()



        binding.selfieImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                takeSelife()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CODE
                )
            }
        }

        binding.nextBtn.setOnClickListener {
            /* val intent = Intent(requireContext(), HomePageActivity::class.java)
             startActivity(intent)*/

            if (uri != null && !uri!!.equals(Uri.EMPTY)) {
                generateToken()
                uploadSelfie()
            } else {
//                Toast.makeText(requireContext(), "Select Image", Toast.LENGTH_SHORT).show()
                showToast(requireContext(),"Current Date : $currentDate")
//                Toast.makeText(requireContext(), "Next Button Clicked $latitude $longtitude", Toast.LENGTH_SHORT).show()

            }
        }


    }

    private fun generateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
              showToast(requireContext(),"Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
             token = task.result


            showToast(requireContext(),token)

            uploadNotifationToken(token)
        })

    }



    private fun uploadNotifationToken(token: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resposne = uploadNotificationTokenApiCall(token)

                if (resposne.isSuccessful) {
                    val responseData = resposne.body()
                    showToast(requireContext(), responseData!!.message)
                } else {
                    showToast(requireContext(), "Response Message ${resposne.message()}")
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun uploadNotificationTokenApiCall(token:String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.uploadNotifationToken(emp_id = empId, token = token)
        }
    }



    private fun uploadSelfie() {

        SimpleArcLoader(requireContext(), "Uploading..", false)

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            uri!!, "r", null
        ) ?: return


        val file =
            File(requireContext().cacheDir, requireContext().contentResolver.getFileName(uri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        Toast.makeText(requireContext(), "Uri : ${file}", Toast.LENGTH_SHORT).show()

        val body = UploadImageRequestBody(file, "image", this)
        val fileName = file.name

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = uploadSelfieApiCall(fileName, body)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    Toast.makeText(
                        requireContext(),
                        "Sucess  ${responseData!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(requireContext(), HomePageActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun uploadSelfieApiCall(
        fileName: String,
        body: UploadImageRequestBody
    ): Response<StatusMessageModel> {

     /*   Toast.makeText(requireContext(), "Upload Selife $latitude $longtitude", Toast.LENGTH_SHORT)
            .show()*/

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.uploadSelfie(
                MultipartBody.Part.createFormData("profileImg", fileName, body),
                empId.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                ),
                currentDate.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                ),
                latitude.toString().toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                ),
                longtitude.toString().toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                )

            )

        }


    }


    private fun takeSelife() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: Exception) {
                    // Error occurred while creating the File
                    Toast.makeText(requireContext(), "null", Toast.LENGTH_LONG)
                        .show()
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also { file ->
                    val photoUri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.tulsidistributors.tdemployee",
                        file
                    )
                    uri = photoUri

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    Toast.makeText(requireContext(), photoUri.toString(), Toast.LENGTH_LONG)
                        .show()

                    startActivityForResult(takePictureIntent, REQUEST_CODE)

                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            binding.selfieImage.setImageURI(uri)
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "SELFI_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // if premission granted then we need camera functionally
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                Toast.makeText(requireContext(), "Premssion Dined", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {

    }

    private fun getUserDetail() {
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner) {
            empId = it.toString()
            Toast.makeText(requireContext(), "EmpId : $empId", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocation() {
        msettingsClient = LocationServices.getSettingsClient(requireContext())

        Toast.makeText(requireContext(), "Inside method ", Toast.LENGTH_SHORT).show()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...

                    latitude = location.latitude
                    longtitude = location.longitude

                    /*    Toast.makeText(requireContext(), "${location.latitude}", Toast.LENGTH_SHORT)
                            .show()*/
                }
            }


        }


    }

    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


}


