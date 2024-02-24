package com.example.elogapp.activity

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.devicemanager.*
import com.example.devicemanager.TrackerService.TrackerBinder
import com.example.elogapp.R
import com.example.elogapp.activity.ui.home.HomeFragment
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.AppUtils.logger
import com.example.elogapp.util.PreferenceHelper
import com.example.elogapp.util.bg.MyServiceWorker
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.permissionx.guolindev.PermissionX
import com.pt.sdk.Uart
import io.sentry.Sentry.*
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


open class MainActivity : BleProfileServiceReadyActivity<TrackerBinder>(),
    TrackerUpdateProgressFragment.OnTrackerUpdateClosedListener
    ,FragmentManager.OnBackStackChangedListener /*AppCompatActivity()*/ {

    private var REQUEST_LOCATION_CODE = 101
    private lateinit var appBarConfiguration: AppBarConfiguration
    val TAG = AppModel.TAG

    private lateinit var dialog: MaterialDialog
//    var mPrivacyDlg: Dialog? = null
    private var mTrackerBinder: TrackerBinder? = null
    private lateinit var navController: NavController
    private var featureEld by Delegates.notNull<Boolean>()
    private var featureDispatch by Delegates.notNull<Boolean>()
//    private val exceptionHandler = CoroutineExceptionHandler {
//        _ , throwable -> Log.e(MainActivity::class.java.name,
//        "Error In: ${throwable.localizedMessage}")
//    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(savedInstanceState: Bundle?) {

        //captureMessage("Sentry SDK Setup ELOG")
        captureMessage("Sentry SDK Setup ELOG")
//        if (!BuildConfig.DEBUG) {
//            Fabric.with(this, new Crashlytics());
//        }

        setContentView(R.layout.activity_main)

        startServiceViaWorker()

        dialog = this?.let { MaterialDialog(it) }!!
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportFragmentManager.addOnBackStackChangedListener(this)
//        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp()
////        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
////        getSupportActionBar().setDisplayUseLogoEnabled(true);


        featureEld = PreferenceHelper().getBoolean(this, AppConstants.PREF_FEATURE_ELD, false)
        featureDispatch = PreferenceHelper().getBoolean(this,AppConstants.PREF_FEATURE_DISPATCH, false)

        //TODO Comment below Line
//        featureDispatch = true
//        featureEld = true

        // Initialize a list of required permissions to request runtime
        val permissionList = mutableListOf<String>()
        if(featureDispatch && featureEld){
            permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionList.add(Manifest.permission.CAMERA)
        }else if(featureDispatch && !featureEld){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionList.add(Manifest.permission.CAMERA)
        }else if(!featureDispatch && featureEld){
            permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        PermissionX.init(this)
            .permissions(permissionList)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based" +
                        " on these permissions", getString(R.string.ok), getString(R.string.cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //Toast.makeText(this, "All permissions are granted.", Toast.LENGTH_LONG).show()

                    try {
                        val bluetoothManager =
                            this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return@request
                        }
                        if (bluetoothManager?.adapter!!.disable()) {
                            AppUtils.logger("Bluetooth Enabling")
                            bluetoothManager.adapter.enable()
                            AppUtils.logger("Bluetooth enabled")
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                } else {

                    dialog.show {
                        title(text = getString(R.string.dialog_title_alert))
                        message(text = "All permissions are mandatory to run application" +
                                " without it you can't continue.\n\nDenied permissions: \n$deniedList")
                        positiveButton(text = getString(R.string.ok)){
                            //finishAffinity()
                        }
                    }
                    dialog.cancelOnTouchOutside(false)
                }
            }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        setActionBarTitle("Home")

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if(featureDispatch && !featureEld) {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController
            val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            navGraph.startDestination = R.id.nav_dispatch_home
            navController.graph = navGraph
            navView.setupWithNavController(navController)

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_dispatch_home,
                    R.id.nav_dispatch,
                    R.id.nav_daily_log,
                    R.id.nav_inspection,
                    R.id.nav_co_driver,
                    R.id.nav_pretrip,
                    R.id.nav_eqipment,
                    R.id.nav_shipping_doc,
                    R.id.nav_company_info,
                    //R.id.nav_setting,
                    R.id.nav_exception,
                    R.id.nav_logout,
                    R.id.versionTxt
                ), drawerLayout
            )

            hideItem(navView, R.id.nav_exception)
            hideItem(navView, R.id.nav_daily_log)
            hideItem(navView, R.id.nav_dispatch)
            hideItem(navView, R.id.nav_home)
            hideItem(navView, R.id.nav_eqipment)
            hideItem(navView, R.id.nav_co_driver)
            hideItem(navView, R.id.nav_inspection)
            hideItem(navView, R.id.content_privacy)
            showItem(navView, R.id.nav_dispatch_home)

            //Make Dispatcher As default Screen
            navController.navigate(R.id.nav_dispatch)

            val connectBtn : ExtendedFloatingActionButton = findViewById(connectionToggleResourceId)
            connectBtn.isVisible = false

        }else if(featureDispatch && featureEld){

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_daily_log,
                    R.id.nav_inspection,
                    R.id.nav_co_driver,
                    R.id.nav_pretrip,
                    R.id.nav_eqipment,
                    R.id.nav_shipping_doc,
                    R.id.nav_company_info,
                    //R.id.nav_setting,
                    R.id.nav_exception,
                    R.id.nav_unidentified_data,
                    R.id.nav_logout,
                    R.id.nav_dispatch,
                    R.id.versionTxt
                ), drawerLayout)

            val connectBtn : ExtendedFloatingActionButton = findViewById(connectionToggleResourceId)
            connectBtn.isVisible = true

        }else if(featureEld && !featureDispatch){

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_daily_log,
                    R.id.nav_inspection,
                    R.id.nav_co_driver,
                    R.id.nav_pretrip,
                    R.id.nav_eqipment,
                    R.id.nav_shipping_doc,
                    R.id.nav_company_info,
                    //R.id.nav_setting,
                    R.id.nav_exception,
                    R.id.nav_logout,
                    R.id.nav_dispatch,
                    R.id.versionTxt
                ), drawerLayout)

            hideItem(navView, R.id.nav_dispatch)


            val connectBtn : ExtendedFloatingActionButton = findViewById(connectionToggleResourceId)
            connectBtn.isVisible = true

        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        try {
            val pInfo: PackageInfo =
                applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
                val version: String = pInfo.versionName
                //val versionCode: Long = pInfo.longVersionCode

                val navMenu: Menu = navView.menu
                navMenu.findItem(R.id.versionTxt).title = "Version $version"

            val drawerSwitch: SwitchCompat =
                navMenu.findItem(R.id.switch_item).actionView as SwitchCompat
            drawerSwitch.setOnCheckedChangeListener { _, isChecked ->

                PreferenceHelper().save(applicationContext!!, AppConstants.PREF_IS_FOR_TESTING, isChecked)

            }


        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        if(featureEld) {
            checkGPSLocation()
        }


//        if (savedInstanceState == null) {
//            val fm = supportFragmentManager
//            val fragment = fm.findFragmentById(R.id.fragment_container)
//            val ft = fm.beginTransaction()
//            if (AppModel.MODE_USB) {
//                ft.add(
//                    R.id.fragment_container,
//                    DevicesFragment.newInstance(),
//                    DevicesFragment.FRAG_TAG
//                )
//            } else {
//                ft.add(R.id.fragment_container, DefaultFragment.newInstance())
//            }
//            ft.commit()
//        }


//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.home)
//
////                val fragment =
////                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//
//                if (fragment is HomeFragment) {
//                    //(fragment as HomeFragment)
//
//                    AppUtils.logger("===============>")
//                }else{
//
//                    AppUtils.logger("===111111111111============>")
//                }
//            }
//        })

    }


    private fun hideItem(navView: NavigationView, menuId: Int) {
        val navMenu: Menu = navView.menu
        navMenu.findItem(menuId).isVisible = false
    }

    private fun showItem(navView: NavigationView, menuId: Int) {
        val navMenu: Menu = navView.menu
        navMenu.findItem(menuId).isVisible = true
    }


    override fun onServiceBound(binder: TrackerBinder) {
        mTrackerBinder = binder
        Log.i(TAG, "A:Tracker bounded.")
    }

    override fun onServiceUnbound() {
        mTrackerBinder = null
        Log.i(TAG, "A:Tracker unbounded.")
        val fm = supportFragmentManager
        var listener: TrackerServiceListener? = fm.findFragmentByTag(TrackerViewFragment.FRAG_TAG) as TrackerServiceListener?
        listener?.onServiceUnbound()
        listener =
            fm.findFragmentByTag(TrackerConnectingFragment.FRAG_TAG) as TrackerServiceListener?
        listener?.onServiceUnbound()
    }

    override fun getServiceClass(): Class<out BleProfileService?>? {
        return TrackerService::class.java
    }

    override fun getConnectionToggleResourceId(): Int {
        return R.id.fab
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    /**
     * View to be shown before 'Select a device'
     */
    override fun setDefaultUI() {}


    override fun getFilterUUID(): UUID? {
        return UUID.fromString(Uart.RX_SERVICE_UUID.toString())
    }

    override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {
        showToast("BA: Failed to Connect :" + device.address + " rc = " + reason)
    }

    override fun onTrackerUpdateClosed() {
        // If update is in progress, cancel it
        if (mTrackerBinder != null) {
            //if (mTrackerBinder.getTracker().isUpdating()) {
            Log.i(TAG, "A: Tracker update canceled")
            mTrackerBinder!!.tracker.cancelUpdate()
            mTrackerBinder!!.cancelUpdateNotifications()
            //}
        }
    }

    override fun onLinkLossOccurred(device: BluetoothDevice?) {
        super.onLinkLossOccurred(device)
        Log.i(TrackerManagerActivity.TAG, "A: Tracker lost link ...")

        // Wait for device
        val fm = supportFragmentManager
        val fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            val ft = fm.beginTransaction()
            val tcf = TrackerConnectingFragment.newInstance().init(mTrackerBinder)
            tcf.onServiceBound(mTrackerBinder)
            ft.replace(R.id.fragment_container, tcf, TrackerConnectingFragment.FRAG_TAG)
            ft.commit()
        }
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(canGoBack)
        supportActionBar!!.setDisplayShowHomeEnabled(canGoBack)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.main, menu)
         val item: MenuItem = menu.findItem(R.id.action_bluetooth)

        when (item.itemId) {

            R.id.action_bluetooth ->   {
                //Log.d("===>", "====================>")
                //.icon.alpha = 10

//                if(featureDispatch){
//                    item.isVisible = false
//                }
//
//                if(featureEld){
//                    item.isVisible = true
//                }
            }
        }


         this.invalidateOptionsMenu()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

         when (item.itemId) {

            R.id.action_bluetooth ->   {
                AppUtils.logger("Bluetooth Icon Clicked")
                //item.isEnabled = false;
                //item.icon.alpha = 10
                invalidateOptionsMenu()
                true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
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

    override fun isBLEEnabled(): Boolean {
        logger("Bluetooth: isBLEEnabled method")
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        return adapter != null && adapter.isEnabled
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_CODE
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


    open fun startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called")
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker"
        val workManager = WorkManager.getInstance(this)

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        val request = PeriodicWorkRequest.Builder(
            MyServiceWorker::class.java,
            16,
            TimeUnit.MINUTES
        )
            .build()

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

//    override fun onBackPressed() {
//        val fragment: Fragment? =  supportFragmentManager.findFragmentById(R.id.nav_dispatch_home)
//        if (fragment is HomeFragment) {
//            super.onBackPressed()
//        }
//    }


}