package com.example.elogapp.activity.ui.dvir

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.ActivityNewDvirBinding
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.repository.responses.dvir.NewDvirResponse
import com.example.elogapp.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewDvir : AppCompatActivity(), KodeinAware, DvirListener {

    override val kodein by kodein()
    private val factory: NewDvirViewModelFactory by instance()
    private lateinit var userPreferences: UserPreference

    private lateinit var newViewModel: NewDvirViewModel
    private lateinit var binding: ActivityNewDvirBinding
    private lateinit var dialog: MaterialDialog
    private val defectList: ArrayList<String> = ArrayList()
    private val defectIdList: ArrayList<Int> = ArrayList()


    private var driverId: Int = 0
    private var clientId: Int = 0
    private var trailerId: Int = 0
    private var vehicleId: Int = 0

//    var defectsIdsVehicle: String? = null
//    var defectsIdsTruck: String? = null
    var defectsIdArrVehicle  : ArrayList<Int> = ArrayList()
    var defectsIdArrTruck  : ArrayList<Int> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_dvir)
        newViewModel = ViewModelProviders.of(this, factory).get(NewDvirViewModel::class.java)
        dialog = MaterialDialog(this)

        userPreferences = UserPreference(this)
        binding.newDvirViewModel = newViewModel
        newViewModel.dvirListener = this

        binding.timeTv.text = AppUtils.getCurrentTime()
        binding.dateTv.text = AppUtils.getCurrentDate()


        getDefectList()

        newViewModel.backButtonLiveData.observe(this) {
            finish()
        }

        binding.truckLayout.setOnCheckedChangeListener { group, checkedId ->

            val radio: RadioButton = group.findViewById(checkedId)
            Log.e("selectedtext-->", radio.text.toString())

            if (radio.text.toString() == getString(R.string.has_defects)) {
                openDefectWindow(defectList, binding.defectsTv, true)
            } else {
                binding.defectsTv.text = ""
            }

        }

        binding.trailerLayout.setOnCheckedChangeListener { group, checkedId ->

            val radio: RadioButton = group.findViewById(checkedId)
            Log.e("selectedtext-->", radio.text.toString())

            if (radio.text.toString() == getString(R.string.has_defects)) {
                openDefectWindow(defectList, binding.defectTrailerTv, false)
            } else {
                binding.defectTrailerTv.text = ""
            }

        }

        userPreferences.driverId.asLiveData().observe(this) {
            if (it != null) {
                driverId = it
                Log.d("===DriverId=====>", "$driverId")
            }
        }

        userPreferences.driverName.asLiveData().observe(this) {
            if (it != null) {
                binding.driverName.text = it
                Log.d("===DriverName=====>", "$driverId")
            }
        }



        userPreferences.clientId.asLiveData().observe(this) {
            if (it != null) {
                clientId = it
                Log.d("===ClientId=====>", "$clientId")
            }
        }



        userPreferences.trailerId.asLiveData().observe(this) {
            if (it != null) {
                trailerId = it
                Log.d("===TrailerId=====>", "$trailerId")
            }
        }

        userPreferences.truckId.asLiveData().observe(this) {
            if (it != null) {
                vehicleId = it
                Log.d("===VehicleId=====>", "$vehicleId")
            }
        }


        binding.btnSubmit.setOnClickListener {

            checkValidation()

        }

    }

    private fun getDefectList() {

        binding.progressBar.show()
        newViewModel.getDefectList(AppConstants.DEFECTS).observe(this) { defectList ->

            binding.progressBar.hide()
            val defectListArr = defectList
            for (defect in defectListArr) {
                this.defectList.add(defect.name)
                this.defectIdList.add(defect.code)
            }

        }

    }


    private fun openDefectWindow(defectList: ArrayList<String>, view: TextView, tuckRGroup: Boolean) {

        val builder = AlertDialog.Builder(this).setCancelable(false)

        val defaultList = booleanArrayOf(
            false,            false,            false,            false,
            false,            false,            false,            false,
            false,            false,            false,            false,
            false,            false,            false,            false,
            false,            false,            false,            false,
            false,            false,            false,            false
        )

        for (x in 0..defectList.size) {

            defaultList[x] = false
            Log.d(AppConstants.TAG, "$x")
        }

        val mDefectList = arrayOfNulls<String>(defectList.size)
        defectList.toArray(mDefectList)

        builder.setTitle("Select Defect")
        builder.setMultiChoiceItems(mDefectList, defaultList) { _, which, isChecked ->
            defaultList[which] = isChecked
            val currentItem = mDefectList[which]
            //Toast.makeText(applicationContext, "$currentItem $isChecked", Toast.LENGTH_SHORT).show()
        }
        builder.setPositiveButton("OK") { _, _ ->

            var defects: String? = null
            var arr : ArrayList<Int> = ArrayList()
            var z = 0
            for (i in defaultList.indices) {
                val checked = defaultList[i]
                if (checked) {
                    z++
                    defects = defects + "," + defectList[i]

                    arr.add(defectIdList[i])
                    binding.vehicleCondition.isChecked = false
                }
            }


            if (defects != null) {
                view.text = defects.replace("null,", "")

                if(tuckRGroup){
                    defectsIdArrTruck = arr
                }else{
                    defectsIdArrVehicle = arr
                }

            }else{

                if(tuckRGroup) {
                    binding.radioNoDefectTruck.isChecked = true
                }else{
                    binding.radioNoDefectTrailer.isChecked = true
                }
            }
        }
        builder.setNeutralButton("Cancel") { _, _ ->

            if(tuckRGroup) {
                binding.radioNoDefectTruck.isChecked = true
            }else{
                binding.radioNoDefectTrailer.isChecked = true
            }

        }
        val dialog = builder.create()
        dialog.show()


    }


    private fun checkValidation() {

//        if( trailerId != 0 || vehicleId != 0){
//
            submitRequest()
//
//        }else{
//
//        }

    }

    private fun submitRequest() {

        val eventObj = JSONObject()
        eventObj.put("id", 0)
        eventObj.put("driverId", driverId)
        eventObj.put("vehicleId", vehicleId)
        eventObj.put("trailerId", trailerId)

        val list1: JSONArray = JSONArray(defectsIdArrVehicle)
        val list2: JSONArray = JSONArray(defectsIdArrTruck)

        eventObj.put("vehicleDefects", list1)
        eventObj.put("trailerDefects", list2)


        if (binding.radioHasDefectTruck.isChecked) {
            eventObj.put("vehiclHasDefect", 1)
        } else {
            eventObj.put("vehiclHasDefect", 0)
        }

        if (binding.radioHasDefectTrailer.isChecked) {
            eventObj.put("trailerHasDefect", 1)
        } else {
            eventObj.put("trailerHasDefect", 0)
        }

        eventObj.put("defect", binding.notesEt.text)

        if (binding.vehicleCondition.isChecked) {
            eventObj.put("status", 1)
        } else {
            eventObj.put("status", 0)
        }

        eventObj.put("clientId", clientId)
        eventObj.put("timeStamping", AppUtils.getServerDateTime())

        val requestBody = eventObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        Log.d("Request==>", eventObj.toString())

        newViewModel.createNewDvirReport(requestBody)

    }


    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(response: NewDvirResponse) {

        binding.progressBar.hide()
        dialog.show {
                title(text = getString(R.string.dialog_title_Msg))
                message(text = response.message)
                positiveButton(text = getString(R.string.ok)){
                    finish()
                }
            }

    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()

        message?.let{
            if(it.contains("${AppConstants.ERROR_CODE_NO_INTERNET}")){
                dialog.show {
                    title(text = getString(R.string.dialog_title_alert))
                    message(text = getString(R.string.internet_error))
                    positiveButton(text = getString(R.string.ok))
                }
            }else{

                toast(message)
//                dialog.show {
//                    title(text = getString(R.string.dialog_title_alert))
//                    message(text = getString(R.string.server_error))
//                    positiveButton(text = getString(R.string.ok))
//                }
            }
        }

    }


    private fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd:MM:yyyy")
        return sdf.format(Date())
    }

    private fun getCurrentTime():String{
        val sdf = SimpleDateFormat("hh:mm AA")
        return sdf.format(Date())
    }


}