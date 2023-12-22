package com.example.callloggerapp

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Long
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        ) {
               getCallDetails()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG), 1)
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can access call logs now
                    getCallDetails()
                } else {
                    // Permission denied
                    // Handle the situation where the user denies permission
                }
                return
            }
            // Add other permission request cases if needed
        }
    }

    private fun getCallDetails() {
        val sb = StringBuilder()
        val managedCursor: Cursor? = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
        val number = managedCursor?.getColumnIndex(CallLog.Calls.NUMBER)
        val type = managedCursor?.getColumnIndex(CallLog.Calls.TYPE)
        val date = managedCursor?.getColumnIndex(CallLog.Calls.DATE)
        val duration = managedCursor?.getColumnIndex(CallLog.Calls.DURATION)
        val name = managedCursor?.getColumnIndex(CallLog.Calls.CACHED_NAME)
        sb.append("Call Details :")
        while (managedCursor?.moveToNext() == true) {
            val phNumber = managedCursor.getString(number!!)
            val callerName = managedCursor.getString(name!!)
            val callType = managedCursor.getString(type!!)
            val callDate = managedCursor.getString(date!!)
            val callDayTime = Date(Long.valueOf(callDate))
            val callDuration = managedCursor.getString(duration!!)
            var dir: String? = null
            val dircode = Integer.parseInt(callType)
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
//            val callLog = CallLogsModel(phNumber,callDayTime,dir!!,callDuration)
            sb.append("\nPhone Number:--- $phNumber \nCaller Name:--$callerName \nCall Type:--- $dir \nCall Date:--- $callDayTime \nCall duration in sec :--- $callDuration")
            sb.append("\n----------------------------------")
        }
        managedCursor?.close()
        val call = findViewById<TextView>(R.id.call)
        call.text = sb.toString()
    }
}