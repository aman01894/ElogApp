package com.example.elogapp.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.elogapp.R
import com.example.elogapp.activity.auth.AuthViewModel
import com.example.elogapp.activity.auth.AuthViewModelFactory
import com.example.elogapp.activity.auth.LoginActivity
import com.example.elogapp.databinding.ActivitySplashBinding
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.PreferenceHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.concurrent.timerTask


class SplashActivity : AppCompatActivity(), KodeinAware {

    private var REQUEST_LOCATION_CODE = 101

    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()
    private var TIME_SPLASH = 3*1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding : ActivitySplashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        val viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
//        val currentEvent = PreferenceHelper().getString(this,AppConstants.LAST_DRIVER_EVENT)
        val isLoggedIn = PreferenceHelper().getBoolean(this, AppConstants.PREF_IS_LOGGED_IN, false)



        Timer().schedule(
            timerTask()
            {
               AppUtils.logger("Splash Timer is running")

                if(!isLoggedIn){
                    TIME_SPLASH = 3*1000
                    AppUtils.logger("Step: User does not Exist Goto LoginActivity")
                    Intent(applicationContext, LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else
                if(isLoggedIn){
                    TIME_SPLASH = 1*1000
                    AppUtils.logger("Step: User Exist Goto Main")
                    Intent(applicationContext, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)}
                }/*else if(!isLoggedIn && currentEvent.isNullOrBlank() && currentEvent?.equals(AppConstants.OFF_DUTY_TITLE) == true ){
                    TIME_SPLASH = 2000

                    AppUtils.logger("Step: User does not Exist Goto LoginActivity")
                    Intent(applicationContext, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)}

                }else if(isLoggedIn && currentEvent.isNotBlank() && currentEvent?.equals(AppConstants.OFF_DUTY_TITLE) == false){
                    TIME_SPLASH = 0
                    AppUtils.logger("Step: User does not Exist Goto LoginActivity")
                    Intent(applicationContext, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)}
                }*/

            }, TIME_SPLASH)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.

//        if(featureEld) {
//            checkGPSLocation()
//        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
     /*   Handler(Looper.getMainLooper()).postDelayed(
            {
            viewModel.getLoggedInUser().observe(this, Observer{ user->
                if(user!=null){
                    val userid = user.id
                    /*lifecycleScope.launch {
                        userPreferences.saveUserId(userid)
                    }*/
                    AppUtils.logger("Step: User Exist Goto MainActivity")
                    Intent(this,MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else
                {
                    AppUtils.logger("Step: User does not Exist Goto LoginActivity")
                    Intent(this, LoginActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }

            })
        }, TIME_SPLASH) // 3000 is the delayed time in milliseconds.

*/
    }




    private fun checkGPSLocation() {
        if (!checkGPSEnabled()) {
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                //getLocation();
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            //getLocation();
        }
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_LOCATION_CODE
                        )
                    })
                    .create()
                    .show()

            } else ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }


        }
    }

}