package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.tulsidistributors.tdemployee.databinding.FragmentUpdateProfileBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import com.tulsidistributors.tdemployee.utils.UploadImageRequestBody
import com.tulsidistributors.tdemployee.utils.getFileName
import com.tulsidistributors.tdemployee.utils.showToast
import de.hdodenhof.circleimageview.CircleImageView
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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileFragment : Fragment(), UploadImageRequestBody.UploadCallback {

    lateinit var bindinig: FragmentUpdateProfileBinding
    lateinit var profileImg: CircleImageView
    lateinit var updateNameEt: EditText
    lateinit var updateImgBtn: Button
    var selectedImage: Uri? = null
    lateinit var name:String
    lateinit var imageUrl:String

    private val args:UpdateProfileFragmentArgs by navArgs()

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindinig = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return bindinig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImg = bindinig.profileImg
        updateNameEt = bindinig.updateNameEt
        updateImgBtn = bindinig.updateImgBtn

        name = args.name
        imageUrl = args.imageUrl

        updateNameEt.setText(name)


        profileImg.setOnClickListener {
            openImageChooser()
        }

        updateImgBtn.setOnClickListener {

            name = updateNameEt.text.toString().trim()

            uploadProfileImage(name)


        }

    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // this it is ok it mean user selected the image
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                REQUEST_CODE_IMAGE_PICKER -> {
                    // here we will get selected image from data
                    selectedImage = data?.data

                    profileImg.setImageURI(selectedImage)
                }
            }
        }
    }


    private fun uploadProfileImage(name:String) {

        if (selectedImage == null) {
           showToast(requireContext(),"Image is not selected")
            return
        }

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            selectedImage!!, "r", null
        ) ?: return

        val file = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(selectedImage!!)
        )

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)


        val body = UploadImageRequestBody(file, "image", this)
        val fileName = file.name

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = profileUpdateApiCall(fileName = fileName,body=body,name=name)

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

    private suspend fun profileUpdateApiCall(
        fileName: String,
        body: UploadImageRequestBody,
        name: String
    ): Response<StatusMessageModel> {


        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.uploadImage(
                MultipartBody.Part.createFormData("profile_image", fileName, body),
                "8755420120".toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                ),
                name.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                )

            )

        }


    }


    override fun onProgressUpdate(percentage: Int) {
       
    }


}