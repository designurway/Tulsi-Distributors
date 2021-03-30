package com.tulsidistributors.tdemployee.ui.home.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.tulsidistributors.tdemployee.databinding.FragmentAssignedOrderBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderData
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderClicked
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class AssignedOrderFragment : Fragment(), AssignedOrderClicked {

    lateinit var binding: FragmentAssignedOrderBinding
    lateinit var assignedOrderRecyclerView: RecyclerView
    lateinit var assignAdapter: AssignedOrderAdapter
    lateinit var msettingsClient: SettingsClient
    private lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest
    var shopLat: Double = 12.263783007551348
    var shopLong: Double = 76.64457088158639
    var curLong: Double = 0.0
    var curLat: Double = 0.0
     lateinit var EmpId:String
    lateinit var userLoginPreferences: UserLoginPreferences

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAssignedOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getUserDetails()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        //initalize the LocationRequest
        locationRequest = LocationRequest.create()
        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        getCurrentLocation()


        assignedOrderRecyclerView = binding.assignedOrderRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        assignedOrderRecyclerView.layoutManager = layoutManager

    }



    private fun getAssignedOrder(empId:String){
//        showToast(requireContext(),"$EmpId")
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getAssignedOderApiCall(empId)
                if (response.isSuccessful) {
                    val data = response.body()
                    val assignedOrder: ArrayList<AssignedOrderData> = data!!.assigned_order
                    assignAdapter = AssignedOrderAdapter(assignedOrder, this@AssignedOrderFragment)
                    assignedOrderRecyclerView.adapter = assignAdapter

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code : ${response.code()} and Respone Message : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), "Exception Occured : ${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend private fun getAssignedOderApiCall(empId: String): Response<AssignedOrderModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getAssignedOrder(empId)
        }
    }

    override fun onItemClicked(
        postion: Int,
        shopName: String,
        dealer_id: String,
        shop_address: String
    ) {


        if (curLat!=0.0){
            val distance = calculateDistance()

            Toast.makeText(requireContext(), "Distance $distance", Toast.LENGTH_SHORT).show()

            if (distance < 100) {
                val action =
                    AssignedOrderFragmentDirections.actionAssignedOrderFragmentToAddProductListFragment2(
                        dealerId = dealer_id,
                        address = shop_address,
                        shopName = shopName
                    )
                requireView().findNavController().navigate(action)
            }
        }else{
            Toast.makeText(requireContext(), "Current Location Empty", Toast.LENGTH_SHORT).show()
        }


    }

    private fun calculateDistance(): Float {
       /* val currentLocation = Location("")
        currentLocation.latitude = curLat
        currentLocation.longitude = curLong

        val shopLocation = Location("")
        shopLocation.latitude = shopLat
        shopLocation.longitude = shopLong

        val distanceInMeter: Float = currentLocation.distanceTo(shopLocation)

        Log.d("distance","Latitude $currentLat Longtitude $currentLong")
*/

        val results = FloatArray(1)

        Location.distanceBetween(curLat,curLong,shopLat,shopLong,results)

        val distance:Float = results[0]

        return distance

    }



    private fun getCurrentLocation() {
        msettingsClient = LocationServices.getSettingsClient(requireContext())

        Toast.makeText(requireContext(), "Inside method ", Toast.LENGTH_SHORT).show()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...

                    curLat = location.latitude
                    curLong = location.longitude

                   /* Toast.makeText(requireContext(), "${location.latitude}", Toast.LENGTH_SHORT)
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

    private fun getUserDetails(){
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            EmpId = it.toString()

            getAssignedOrder(EmpId)
        })
    }


}