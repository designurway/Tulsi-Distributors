package com.tulsidistributors.tdemployee.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class TimeDiffrence(val loginTime:String,val logoutTime:String) {

  fun getTimeDiffrence(): String {
      val timeFormat = java.text.SimpleDateFormat("hh:mm:ss")

      val logInTime = timeFormat.parse(loginTime)
      val logOutTime = timeFormat.parse(logoutTime)

      val timeDiffrence = logOutTime.time - logInTime.time

      val days = (timeDiffrence / (1000 * 60 * 60 * 24))
      val hours = ((timeDiffrence - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
      val min =
          (timeDiffrence - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60)

            return "$hours : $min"
  }




}