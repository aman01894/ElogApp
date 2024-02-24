package com.example.elogapp.activity.ui.exception


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.Outbound
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentExceptionBinding
import com.example.elogapp.repository.model.Exceptions
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.pref.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


private var myData: Exceptions? = null

class ExceptionFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: ExceptionsViewModelFactory by instance()
    private lateinit var rAdapter: ExceptionListAdapter
    private lateinit var viewModel: ExceptionViewModel
    private lateinit var binding: FragmentExceptionBinding
    private lateinit var userDetails: UserDetails
    private lateinit var dialog : MaterialDialog
    private lateinit var userPreference: UserPreference
    private var mVehicleId: Int = 0
    private var dutyIdPref: String? = null
    private lateinit var exceptionList: List<Exceptions>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_exception, container, false);
        val view = binding.root
        viewModel = ViewModelProvider(this, factory).get(ExceptionViewModel::class.java)

        userPreference = UserPreference(requireContext())
        dialog = activity?.let { MaterialDialog(it) }!!

        binding.btnSubmit.setOnClickListener {

            if(mVehicleId == 0){

                dialog.show {
                    title(text = getString(R.string.dialog_title_Msg))
                    message(text = getString(R.string.selected_vehicle))
                    positiveButton(text = getString(R.string.ok)) {

                    }
                }

            }else if(dutyIdPref == null || dutyIdPref == ""){

                dialog.show {
                    title(text = getString(R.string.dialog_title_Msg))
                    message(text = getString(R.string.start_duty))
                    positiveButton(text = getString(R.string.ok)) {

                    }
                }

            }else {
                insertException()
            }

        }

        binding.btnBack.setOnClickListener {

            activity?.onBackPressed()

        }


        viewModel.getDropdownList(AppConstants.KEY_EXCEPTION).observe(this,Observer{list ->
            if(list!= null ){
                exceptionList = list

                updateView()
            }
        })

        viewModel.getUserDetails().observe(this,Observer{user ->
            if(user!= null ){

                userDetails = user
            }
        })

        userPreference.truckId.asLiveData().observe(viewLifecycleOwner, {
            Log.d("===TruckNoID=====>", "$it")
            if (it != null) {
                mVehicleId = it
            }
        })

        userPreference.dutyId.asLiveData().observe(viewLifecycleOwner, {
            Log.d("===DutyId=====>", "$it")

            if (it != null) {
                dutyIdPref = it
            }
        })


        return view
    }


    /**
     * Send Duty Information @ Server via HTTP
     */
    private fun insertException(){


        try {


            var exceptionSelected = false
            var ids = ""
            exceptionList?.forEach {
                if (it.checked) {
                    exceptionSelected = true
                }
            }

            if (!exceptionSelected) {

                dialog.show {
                    title(text = getString(R.string.dialog_title_Msg))
                    message(text = getString(R.string.select_exception))
                    positiveButton(text = getString(R.string.ok)) {

                    }
                }

            } else {

            exceptionList?.forEach {

                if (it.checked) {

                    val eventObj = JSONObject()
                    eventObj.put("userId", userDetails.id)
                    eventObj.put("exceptionTypeId", it.exceptionId)
                    eventObj.put("exceptionDateTime", AppUtils.getServerDateTime())
                    eventObj.put("customLocation", "")
                    eventObj.put("vehicleId", mVehicleId)
                    eventObj.put("clientId", userDetails.clientId)
                    eventObj.put("Notes", binding.comment.text)
                    eventObj.put("dutyId", dutyIdPref)

                    val mainObj = JSONObject()
                    mainObj.put(AppConstants.KEY_EVENTTYPE, AppConstants.EVENT_TYPE_EXCEPTION)
                    mainObj.put(AppConstants.KEY_EVENT_DATA, eventObj)

                    Outbound().apply {

                        eventType = AppConstants.EVENT_TYPE_EXCEPTION
                        data = mainObj.toString()
                        synced = false

                    }.also {

                        viewModel.insertOutboundData(it)
                        AppUtils.logger("DATA Inserted in Outbound ${eventObj.toString()}")

                    }
                }
            }

                dialog.show {
                    title(text = getString(R.string.dialog_title_Msg))
                    message(text = getString(R.string.exception_added))
                    positiveButton(text = getString(R.string.ok)) {
                        activity?.onBackPressed()
                    }
                }

        }

            } catch (e:Exception){
                AppUtils.logger("Exception in save event in outbound${e.message}")
            }

    }


    private fun updateView(){

        if(exceptionList?.size!! > 0){
            rAdapter = activity?.let {
                ExceptionListAdapter(
                    it.applicationContext, exceptionList
                ) { mData -> listItemClicked(mData) }
            }!!

            binding.exceptionRv.adapter = rAdapter
            rAdapter.notifyDataSetChanged()

        }else{

            dialog.show {
                title(text = getString(R.string.dialog_title_Msg))
                message(text = getString(R.string.no_pending_load))
                positiveButton(text = getString(R.string.ok)){
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun listItemClicked(mData: Exceptions) {

        myData = mData

    }



}