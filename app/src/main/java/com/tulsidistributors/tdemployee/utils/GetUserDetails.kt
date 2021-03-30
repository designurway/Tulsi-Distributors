package com.tulsidistributors.tdemployee.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences

class GetUserDetails(val userLoginPreferences: UserLoginPreferences,val viewLifecycleOwner:LifecycleOwner) {

    fun getEmpId():String{
        var empId=""
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner) {
            empId =  it.toString()
        }
        return empId
    }
}