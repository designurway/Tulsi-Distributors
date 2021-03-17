package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.FragmentNotificationBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.ui.adapter.NotificationAdapter
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationDataModel
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class NotificationFragment : Fragment() {
    lateinit var binding: FragmentNotificationBinding
    lateinit var notificationRv: RecyclerView
    lateinit var notificationAdapter: NotificationAdapter
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


        notificationRv = binding.notificationRv



        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getNotification("TD001")

                if(response.isSuccessful){

                    val data = response.body()
                    if (data?.status.equals("1")){

                        val notificationDetails: ArrayList<NotificationModel> =data!!.notifications


                        notificationAdapter = NotificationAdapter(notificationDetails)
                        val linearLayout = LinearLayoutManager(activity)
                        notificationRv.setLayoutManager(linearLayout)
                        notificationRv.setAdapter(notificationAdapter)
                    } else {
                        Toast.makeText(requireContext(), data?.message, Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(requireContext(), "Response Code ${response.code()} and Response Message ${response.message()}", Toast.LENGTH_SHORT).show()

                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Exeption Occured ${e.message}", Toast.LENGTH_SHORT).show()

            }
        }



    }

    suspend private fun getNotification(empId: String): Response<NotificationDataModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getNotification(empId)
        }
    }

}