package com.tulsidistributors.tdemployee.ui.home.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.databinding.FragmentAttendenceBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.attendance.AttendanceModel
import com.tulsidistributors.tdemployee.utils.Common
import com.tulsidistributors.tdemployee.utils.TimeDiffrence
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AttendenceFragment : Fragment() {

    lateinit var datePickerDialog: DatePickerDialog

    lateinit var binding: FragmentAttendenceBinding
    lateinit var date_cv: CardView
    lateinit var date__tv: TextView
    lateinit var selectedDate: String
    lateinit var name: String
    lateinit var imageUrl: String
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var empId: String
    lateinit var mContext: Context
    lateinit var logoutTime: String


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

        mContext = requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetails()

        date_cv = binding.dateCv
        date__tv = binding.dateTv

        date__tv.text = Common().getCurrentDate("dd/MM/yyyy")


        date_cv.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]




            datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, Year, Month, dayOfMonth ->

                    //Date format
                    if (Month < 10 && dayOfMonth < 10) {
                        selectedDate = "$Year-0${(Month + 1)}-0$dayOfMonth"
                    } else if (Month > 10 && dayOfMonth < 10) {
                        selectedDate = "$Year-${(Month + 1)}-0$dayOfMonth"
                    } else if (Month < 10 && dayOfMonth > 10) {
                        selectedDate = "$Year-0${(Month + 1)}-$dayOfMonth"
                    } else {
                        selectedDate = "$Year-${(Month + 1)}-$dayOfMonth"
                    }


                    date__tv.text = dayOfMonth.toString() + "/" + (Month + 1) + "/" + Year

                    getAttandance(empId, selectedDate)

                    showToast(mContext, "selected date $selectedDate")

                }, year, month, day
            )
            datePickerDialog.show()


        }

        binding.totalTime.setOnClickListener {


        }


    }


    private fun getAttandance(emp_id: String, att_date: String) {
        CoroutineScope(Dispatchers.Main).launch {

//            Toast.makeText(requireContext(), "Date : $att_date", Toast.LENGTH_SHORT).show()
            try {

                val response = getAttandanceApiCall(emp_id, att_date)
                if (response.isSuccessful) {
                    val resposeData = response.body()

                    if (resposeData?.status.equals("1")) {

                        val loginTime =
                            Common().getTime(resposeData?.data?.login_time.toString())
                        logoutTime =
                            Common().getTime(resposeData?.data?.logout_time.toString())

                        binding.attendName.text = resposeData?.data?.first_name
                        binding.attendEmail.text = resposeData?.data?.emp_id
                        binding.loginTime.text = loginTime
                        binding.logoutTime.text = logoutTime

                        Glide.with(requireView()).load(resposeData?.data?.profile)
                            .into(binding.atteImg)

                        if (!logoutTime.equals("0000-00-00 00:00:00") || !logoutTime.equals("00:00:00")) {
                            val calendar = Calendar.getInstance()
                            val mdformat = SimpleDateFormat("HH:mm:ss")
                            logoutTime = mdformat.format(calendar.time)

                        }

                        val timeDiff = TimeDiffrence(loginTime, logoutTime)

                        val totalTime = timeDiff.getTimeDiffrence()

                        binding.totalTime.text = totalTime

                        showToast(mContext, "Total Time Diffrence ${logoutTime}")


                    } else {

                        binding.loginTime.text = "00:00:00"
                        binding.logoutTime.text = "00:00:00"
                        binding.totalTime.text = "00:00"

                        showToast(mContext, "Message : ${resposeData?.message}")

                    }

                } else {

                    showToast(
                        mContext,
                        "ResponseCode : ${response.code()} and ResponseMessage ${response.message()}"
                    )


                }
            } catch (e: Exception) {
                showToast(mContext, "Exception Occured ${e.message}")
            }
        }
    }

    private suspend fun getAttandanceApiCall(
        empId: String,
        date: String
    ): Response<AttendanceModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getAttendance(empId, date)
        }
    }


    private fun getLoginDetails() {

        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner, { EmpId ->
            empId = EmpId.toString()

            getAttandance(EmpId.toString(), Common().getCurrentDate("yyyy-MM-dd"))

            showToast(mContext, "Today Date : ${Common().getCurrentDate("yyyy-MM-dd")}")

        })


    }


}