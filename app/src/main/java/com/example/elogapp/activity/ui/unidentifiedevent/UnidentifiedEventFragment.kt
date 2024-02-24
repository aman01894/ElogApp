package com.example.elogapp.activity.ui.unidentifiedevent


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.activity.ui.home.myAddress
import com.example.elogapp.database.Outbound
import com.example.elogapp.database.UnidentifiedEvents
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentUnidentifiedDataBinding
import com.example.elogapp.gps.RetrofitClient
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.hide
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.util.show
import com.pt.sdk.TelemetryEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private var myData: UnidentifiedEvents? = null

class UnidentifiedEventFragment : Fragment(), KodeinAware, UnidentifiedEventListAdapter.EventListener  {

    override val kodein by kodein()
    private val factory: UnidentifiedEventViewModelFactory by instance()
    private lateinit var rAdapter: UnidentifiedEventListAdapter
    private lateinit var viewModel: UnidentifiedEventViewModel
    private lateinit var binding: FragmentUnidentifiedDataBinding
    private lateinit var userDetails: UserDetails
    private lateinit var dialog : MaterialDialog
    private lateinit var userPreference: UserPreference
    private var mVehicleId: Int = 0
    private var dutyIdPref: String? = null
    private lateinit var exceptionList: List<UnidentifiedEvents>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_unidentified_data, container, false);
        val view = binding.root
        viewModel = ViewModelProvider(this, factory)[UnidentifiedEventViewModel::class.java]

        userPreference = UserPreference(requireContext())
        dialog = activity?.let { MaterialDialog(it) }!!

        binding.btnAccept.setOnClickListener {

            acceptOrRejectEvents(true)
        }

        binding.btnReject.setOnClickListener {

            acceptOrRejectEvents(false)
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }


        viewModel.getAllUnidentifiedEvents().observe(viewLifecycleOwner,Observer{list ->
            if(list!= null ){
                exceptionList = list
                updateView()
            }
        })

        viewModel.getUserDetails().observe(viewLifecycleOwner,Observer{user ->
            if(user!= null ){
                userDetails = user
            }
        })

        userPreference.truckId.asLiveData().observe(viewLifecycleOwner, Observer{
            Log.d("===TruckNoID=====>", "$it")
            if (it != null) {
                mVehicleId = it
            }
        })

        userPreference.dutyId.asLiveData().observe(viewLifecycleOwner, Observer{
            Log.d("===DutyId=====>", "$it")
            if (it != null) {
                dutyIdPref = it
            }
        })

        return view
    }

    private fun checkedUnidentifiedEvent(myData: UnidentifiedEvents){
        CoroutineScope(Dispatchers.IO).launch {
            val userId = if(myData.checked) userDetails.id else 0
            viewModel.checkedUnidentifiedEvent(myData.ID, myData.seq, myData.checked, userId)
        }
    }

    private fun getUnidentifiedEventsByStatus(selectStatus: Boolean): List<UnidentifiedEvents>? {

        return viewModel.getUnidentifiedEventsByStatus(selectStatus)

    }

    /**
     * Send Duty Information @ Server via HTTP
     */
    private fun acceptOrRejectEvents(accepted: Boolean){

        try {

            var eventsList: List<UnidentifiedEvents>? = null
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.IO).async {
                    eventsList = getUnidentifiedEventsByStatus(accepted)
                }.await()

                if (eventsList?.size == 0) {

                    dialog.show {
                        title(text = getString(R.string.dialog_title_Msg))
                        message(text = getString(R.string.select_unidentified))
                        positiveButton(text = getString(R.string.ok)) {

                        }
                    }
                } else if (eventsList?.size!! > 0) {

                    CoroutineScope(Dispatchers.IO).async {
                        eventsList?.forEach {
                            saveUnidentifiedEvents(it, accepted)
                        }
                    }.await()

                    dialog.show {
                        title(text = getString(R.string.dialog_title_Msg))
                        message(text = getString(R.string.unidentified_event_added))
                        positiveButton(text = getString(R.string.ok)) {
                            activity?.onBackPressed()
                        }
                    }
                }
            }

            } catch (e:Exception){
                AppUtils.logger("Exception in save event in outbound${e.message}")
            }


    }


    /**
     * Send Duty Information @ Server via HTTP
     */
    private fun saveUnidentifiedEvents(event: UnidentifiedEvents, accepted: Boolean) {
        try {


            val eventObj = JSONObject()

            val id = if(accepted) event.userId else "0"
            eventObj.put("userId", id)
            eventObj.put("vehicleId", mVehicleId)

            eventObj.put("eventTime", AppUtils.getServerDateTime())
            eventObj.put("dateTimeDevice", event.eventTimeDevice)
            eventObj.put("engineAge", event.engineAge)
            eventObj.put("engineHours", event.engineHours)
            eventObj.put("latitude", event.latitude)
            eventObj.put("longitude", event.longitude)

            eventObj.put("ignitionStatus", "NA")
            eventObj.put("odometer", event.odometer)

            eventObj.put("rpm", event.rpm)
            eventObj.put("seq", event.seq)
            eventObj.put("velocity", event.velocity)
            eventObj.put("odb2", event.odb2)
            eventObj.put("engineTemp", "NA")
            eventObj.put("speed", event.speed)
            eventObj.put("ignitionStatus",  event.ignitionStatus)
            eventObj.put("vin", event.vin)

            val mainObj = JSONObject()
            mainObj.put(AppConstants.KEY_EVENTTYPE, AppConstants.EVENT_TYPE_ELD_UNIDENTIFIED)
            mainObj.put(AppConstants.KEY_EVENT_DATA, eventObj)

                    Outbound().apply {

                        eventType = AppConstants.EVENT_TYPE_ELD_UNIDENTIFIED
                        data = mainObj.toString()
                        synced = false

                    }.also {

                        viewModel.insertOutboundData(it)
                        AppUtils.logger("DATA Inserted in Outbound ${eventObj.toString()}")

                        val status = viewModel.deleteEvent(event)
                        AppUtils.logger("Event Deleted ${status}")

                    }

        } catch (e: Exception) {
            AppUtils.logger("Exception in save event in outbound${e.message}")
        }

    }

    private fun kilometerToMiles(valInKm: String?): String? {

        val miles = (valInKm?.toDouble()?.div(1.609))

        return miles?.toString()
    }


    private fun updateView(){

        if(exceptionList?.size!! > 0){
            rAdapter = activity?.let {
                UnidentifiedEventListAdapter(
                    it.applicationContext, exceptionList,this
                ) { mData -> listItemClicked(mData) }
            }!!

            binding.exceptionRv.adapter = rAdapter
            rAdapter.notifyDataSetChanged()

        }else{

            dialog.show {
                title(text = getString(R.string.dialog_title_Msg))
                message(text = getString(R.string.no_unidentified))
                positiveButton(text = getString(R.string.ok)){
                    activity?.onBackPressed()
                }
            }
        }
    }



    private fun getGeoAddress(latitude: Double, longitude: Double) {

        AppUtils.logger("Step: getGetAddress, latitude $latitude longitude $longitude")
        binding.progressBar.show()
        val call =
            RetrofitClient.getInstance().myApi.getAddress(latitude.toString(), longitude.toString())
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                myAddress = response.body().toString()
                binding.progressBar.hide()
                AppUtils.logger("Step: getGetAddress $myAddress")

                dialog.show {
                    title(text = getString(R.string.dialog_title_address))
                    message(text = myAddress)
                    positiveButton(text = getString(R.string.ok)){

                    }
                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                binding.progressBar.hide()
                Toast.makeText(activity, "An error has occurred", Toast.LENGTH_LONG).show()
                AppUtils.logger("Step: getmAddress Error ")
            }
        })
    }

    private fun listItemClicked(mData: UnidentifiedEvents) {
        myData = mData
    }

    override fun onCheckBoxClickEvent(myData: UnidentifiedEvents) {
        checkedUnidentifiedEvent(myData)
    }
    override fun onLocationButtonClickEvent(myData: UnidentifiedEvents) {
        myData.let {
            getGeoAddress(it.latitude.toDouble(), it.longitude.toDouble())
        }

    }


}