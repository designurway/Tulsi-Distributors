package com.tulsidistributors.tdemployee.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.FragmentNotificationBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.ui.adapter.NotificationAdapter
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationDataModel
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationModel
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class NotificationFragment : Fragment() {
    lateinit var binding: FragmentNotificationBinding
    lateinit var notificationRv: RecyclerView
    lateinit var notificationAdapter: NotificationAdapter
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var EmpId: String
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        notificationRv = binding.notificationRv

        getLoginDetail()


    }

    fun getNotification(empId: String) {
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getNotificationApiCall(empId)

                if (response.isSuccessful) {

                    val data = response.body()
                    if (data?.status.equals("1")) {

                        val notificationDetails: ArrayList<NotificationModel> = data!!.notifications


                        notificationAdapter = NotificationAdapter(notificationDetails)
                        val linearLayout = LinearLayoutManager(activity)
                        notificationRv.setLayoutManager(linearLayout)
                        notificationRv.setAdapter(notificationAdapter)
                    } else {
                        showToast(mContext, data?.message!!)

                    }
                } else {
                    showToast(mContext,"${response.code()} and Response Message ${response.message()}")


                }

            } catch (e: Exception) {
                showToast(mContext, "Exeption Occured ${e.message}")


            }
        }
    }

    suspend private fun getNotificationApiCall(empId: String): Response<NotificationDataModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getNotification(empId)
        }
    }

    private fun getLoginDetail() {
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner, {
            EmpId = it.toString()
            showToast(mContext, it.toString())
            getNotification(it.toString())
        })
    }

}