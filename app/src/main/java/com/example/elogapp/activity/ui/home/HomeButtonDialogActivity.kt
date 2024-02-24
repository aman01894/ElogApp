package com.example.elogapp.activity.ui.home

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.BundleCompat.getBinder
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.util.StringUtil
import com.example.devicemanager.AppModel
import com.example.devicemanager.AppModel.TAG
import com.example.devicemanager.TrackerViewFragment
import com.example.elogapp.R
import com.example.elogapp.gps.GPSTracker
import com.example.elogapp.gps.RetrofitClient
import com.example.elogapp.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode

class HomeButtonDialogActivity : AppCompatActivity() {

    lateinit var currLocation : EditText
    lateinit var custLocation : EditText
    lateinit var currOdometer : EditText
    lateinit var notes : EditText
    private lateinit var split_7_3 : RadioButton
    private lateinit var split_8_2 : RadioButton
    var title : String = ""
    private var sleepMode : Int = AppConstants.NO_SPLIT
//    private var gpsTracker: GPSTracker? = null

    val tmIf = IntentFilter()
    var tmRefresh: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateTelemetryInfo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_button_dialog)


        var closeBtn = findViewById<ImageView>(R.id.close_btn);
        var submitBtn = findViewById<Button>(R.id.submit_btn);
        var titleTV = findViewById<TextView>(R.id.title);
        var splitLay = findViewById<LinearLayout>(R.id.split_lay);
        split_7_3 = findViewById(R.id.split_7_3);
        split_8_2 = findViewById(R.id.split_8_2);

        currLocation = findViewById(R.id.et_current_loc);
        custLocation = findViewById(R.id.et_custom_location);
        currOdometer = findViewById(R.id.et_current_odometer);
        notes = findViewById(R.id.et_notes);

        title = intent.extras?.get(AppConstants.TITLE).toString()
        titleTV.text = title
        var id = intent.extras?.get(AppConstants.EVENT_TYPE)

        if (myAddress.isNotBlank()) {
            currLocation.setText(myAddress)
            currLocation.isFocusable = false
        }else{
            updateTelemetryInfo()
        }

        //Toast.makeText(this,title,Toast.LENGTH_LONG).show()


        // End Stored Events tile
        tmIf.addAction("REFRESH")

        if(title == AppConstants.SLEEP_TITLE){

            splitLay.visibility = View.VISIBLE

            if(HomeFragment.isSplitEnable()){

                split_7_3.isEnabled = true
                split_8_2.isEnabled = true
                split_7_3.isChecked = false
                split_8_2.isChecked = false

            }else{

                when(HomeFragment.getSleepType()){

                    AppConstants.SPLIT_8_2 ->{
                        split_8_2.isChecked = true
                        split_8_2.isEnabled = false
                        split_7_3.isEnabled = false
                    }

                    AppConstants.SPLIT_7_3 ->{
                        split_7_3.isChecked = true
                        split_7_3.isEnabled = false
                        split_8_2.isEnabled = false
                    }

                }

            }
        }


        titleTV.text = title;
        closeBtn.setOnClickListener {
            finish()
        }

        submitBtn.setOnClickListener {

            if (checkValidation()) {

                val intent = Intent().apply {
                    putExtra("cust_loc", currLocation.text)
                    putExtra("curr_loc", custLocation.text)
                    putExtra("odometer", currOdometer.text)
                    putExtra("notes", notes.text)
                    putExtra("event_id", id.toString())
                    putExtra("split_sleep", sleepMode.toString())

                    when (id) {
                        AppConstants.START_DUTY -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.START_DUTY_TITLE
                        )
                        AppConstants.DRIVE -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.DRIVE_TITLE
                        )
                        AppConstants.SLEEP -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.SLEEP_TITLE
                        )
                        AppConstants.OFF_DUTY -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.OFF_DUTY_TITLE
                        )
                        AppConstants.PERSONAL_USE -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.PERSONAL_USE_TITLE
                        )
                        AppConstants.YARD_MOVE -> putExtra(
                            AppConstants.EVENT_TYPE,
                            AppConstants.YARD_MOVE_TITLE
                        )
                    }

                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }




    override fun onResume() {
        super.onResume()
        Log.v(TrackerViewFragment.TAG, "TVF: onResume:$this")
        LocalBroadcastManager.getInstance(this).registerReceiver(tmRefresh, tmIf)

    }

    override fun onPause() {
        super.onPause()
        Log.v(TrackerViewFragment.TAG, "TVF: onPause:$this")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tmRefresh)
    }


    private fun checkValidation() : Boolean{

        when{

            currLocation.text.isEmpty()->{
                Toast.makeText(applicationContext, "Enter Current Location", Toast.LENGTH_LONG).show()
                return false
            }
//            custLocation.text.isEmpty()->{
//                Toast.makeText(applicationContext, "Enter Custom Location", Toast.LENGTH_LONG).show()
//                return false
//            }
            currOdometer.text.isEmpty()->{
                Toast.makeText(applicationContext, "Enter Odometer value", Toast.LENGTH_LONG).show()
                return false
            }

            split_7_3.isChecked -> {
                sleepMode = AppConstants.SPLIT_7_3
            }

            split_8_2.isChecked -> {
                sleepMode = AppConstants.SPLIT_8_2
            }

        }

        return true
    }


    fun updateTelemetryInfo() {
        if (AppModel.getInstance().mLastEvent != null) {
            try {
                val te = AppModel.getInstance().mLastEvent
                //currOdometer?.setText(te.mOdometer)

                val miles = kilometerToMiles(te.mOdometer)
                currOdometer?.setText(miles)

                CoroutineScope(Dispatchers.IO).launch {
                    getmAddress(te.mGeoloc.latitude.toDouble(), te.mGeoloc.longitude.toDouble())
                }

            } catch (e: java.lang.Exception) {
                Log.e(TrackerViewFragment.TAG, e.fillInStackTrace().toString())
            }
        }
    }

    private fun kilometerToMiles(valInKm: String?): String? {

        val miles = (valInKm?.toDouble()?.div(1.609))

        return miles?.toString()
    }



    private fun getmAddress(latitude: Double, longitude: Double) {

        val call =  RetrofitClient.getInstance().myApi.getAddress(latitude.toString(), longitude.toString())
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                myAddress = response.body().toString()
                if (myAddress.isNotBlank()) {

                    CoroutineScope(Dispatchers.Main).launch{
                        currLocation.setText(myAddress)
                    }

                    currLocation.isFocusable = false
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                //Toast.makeText(requireContext, "An error has occured", Toast.LENGTH_LONG).show();
            }

        })
    }

}