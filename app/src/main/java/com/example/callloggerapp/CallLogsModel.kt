package com.example.callloggerapp

import java.util.Date

data class CallLogsModel(
    val phoneNo:String,
    val callDayTime:Date,
    val dir:String,
    val callDuration:String
)
