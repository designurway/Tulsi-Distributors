package com.tulsidistributors.tdemployee.ui.home.fragment.assigned_orders

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.*
import com.tulsidistributors.tdemployee.databinding.FragmentAssignedOrderBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderData
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderClicked
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showLog
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    var shopLat: Double = 0.0
    var shopLong: Double = 0.0
    var curLong: Double = 0.0
    var curLat: Double = 0.0
    lateinit var userLoginPreferences: UserLoginPreferences
    var EmpId = ""
    var EmaiId=""
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mContext: Context
    lateinit var shimmerLayout: ShimmerFrameLayout

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

        mContext = requireContext()
        shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)



        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        //initalize the LocationRequest
        locationRequest = LocationRequest.create()
        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        getCurrentLocation()

        getUserDetails()


        assignedOrderRecyclerView = binding.assignedOrderRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        assignedOrderRecyclerView.layoutManager = layoutManager



    }



    private fun getAssignedOrder(empId:String){
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getAssignedOderApiCall(empId)
                if (response.isSuccessful) {
                    val data = response.body()
                    val assignedOrder: ArrayList<AssignedOrderData> = data!!.assigned_order
                    assignAdapter = AssignedOrderAdapter(assignedOrder, this@AssignedOrderFragment)
                    assignedOrderRecyclerView.adapter = assignAdapter

                    shimmerLayout.stopShimmer()
                    noDataFound(assignedOrderRecyclerView,shimmerLayout)

                } else {

                    showToast(mContext,"Response Code : ${response.code()} and Respone Message : ${response.message()}")

                }
            } catch (e: Exception) {

                showToast(mContext,"Exception Occured : ${e.message}")

            }
        }
    }

    suspend private fun getAssignedOderApiCall(empId: String): Response<AssignedOrderModel> {
        return withContext(Dispatchers.IO) {
            showLog("empId","EmpId: $empId")
            BaseClient.getInstance.getAssignedOrder(empId)
        }
    }

    override fun onItemClicked(
        postion: Int,
        shopName: String,
        dealer_id: String,
        shop_address: String,
        latitude: String,
        longitude: String,
        routingId: String
    ) {

        shopLat = latitude.toDouble()
        shopLong = longitude.toDouble()

        if (curLat!=0.0){
            val distance = calculateDistance()

            showToast(mContext,"Distance $distance")


            if (distance < 100) {
                val action =
                    AssignedOrderFragmentDirections.actionAssignedOrderFragmentToAddProductListFragment2(
                        dealerId = dealer_id,
                        address = shop_address,
                        shopName = shopName,
                        routingId = routingId
                    )
                requireView().findNavController().navigate(action)
            }
        }else{
            showToast(mContext,"Current Location Empty")
        }


    }

    private fun calculateDistance(): Float {
       /* val currentLocation = Location("")
        currentLocation.latitude = curLat
        currentLocation.longitude = curLong

        val shopLocation = Location("")
        shopLocation.latitude = shopLat+
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

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...

                    curLat = location.latitude
                    curLong = location.longitude

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

//        val emp = GetUserDetails(userLoginPreferences,viewLifecycleOwner).getEmpId()

        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            EmaiId = it.toString()
            getAssignedOrder(it.toString())
            showToast(mContext,EmpId)
        })


    }


}