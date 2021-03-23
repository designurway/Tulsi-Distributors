package com.tulsidistributors.tdemployee.ui.home.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.databinding.FragmentAttendenceBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.attendance.AttendanceModel
import com.tulsidistributors.tdemployee.utils.TimeDiffrence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*


class AttendenceFragment : Fragment() {

    lateinit var datePickerDialog: DatePickerDialog

    lateinit var binding: FragmentAttendenceBinding
    lateinit var date_cv: CardView
    lateinit var date__tv: TextView
    lateinit var selectedDate: String;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAttendenceBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date_cv = binding.dateCv
        date__tv = binding.dateTv



        date_cv.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]



            datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, Year, Month, dayOfMonth ->

                    //Date format
                    selectedDate = "$dayOfMonth-${(Month + 1)}-$Year"

                    date__tv.setText(dayOfMonth.toString() + "/" + (Month + 1) + "/" + Year)

                    getAttandance("Emp", selectedDate)

                    Toast.makeText(requireContext(), "selected date $selectedDate", Toast.LENGTH_SHORT).show()

                }, year, month, day
            )
            datePickerDialog.show()


        }

        binding.totalTime.setOnClickListener {
            Toast.makeText(requireContext(), "Date : $selectedDate", Toast.LENGTH_SHORT).show()
        }



        getAttandance("Emp", "22-3-2021")


    }

    private fun getAttandance(emp_id: String, att_date: String) {
        CoroutineScope(Dispatchers.Main).launch {

//            Toast.makeText(requireContext(), "Date : $att_date", Toast.LENGTH_SHORT).show()
            try {

                val response = getAttandanceApiCall(emp_id, att_date)
                if (response.isSuccessful) {
                    val resposeData = response.body()

                    if (resposeData?.status.equals("1")) {

                        val loginTime = resposeData?.data?.login_time
                        val logoutTime = resposeData?.data?.logout_time

                        binding.attendName.text = resposeData?.data?.first_name
                        binding.attendEmail.text = resposeData?.data?.emp_id
                        binding.loginTime.text = loginTime
                        binding.logoutTime.text = logoutTime

                        Glide.with(requireView()).load(resposeData?.data?.profile)
                            .into(binding.atteImg)


                        val timeDiff = TimeDiffrence(loginTime.toString(), logoutTime.toString())

                        val totalTime = timeDiff.getTimeDiffrence()


                        binding.totalTime.text = totalTime

                        Toast.makeText(
                            requireContext(),
                            "Total Time Diffrence ${totalTime}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {

                        binding.loginTime.text = "00:00:00"
                        binding.logoutTime.text = "00:00:00"
                        binding.totalTime.text = "00:00"

                        Toast.makeText(
                            requireContext(),
                            "Message : ${resposeData?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "ResponseCode : ${response.code()} and ResponseMessage ${response.message()}",
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

    private suspend fun getAttandanceApiCall(
        empId: String,
        date: String
    ): Response<AttendanceModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getAttendance("TD002", date)
        }
    }


}