package com.example.elogapp.activity.ui.loads.open_load

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentOpenLoadListBinding
import com.example.elogapp.repository.model.ShipperConsigneeList
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.LoadAcceptRejectListener
import com.example.elogapp.repository.responses.load.LoadAcceptRejectResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

private lateinit var rAdapter: OpenLoadItemAdapter
private lateinit var openLoadsItemViewModel: OpenLoadItemViewModel
private lateinit var binding: FragmentOpenLoadListBinding
private var position : Int = 0

@Suppress("NAME_SHADOWING")
class OpenLoadItemFragment : Fragment(), KodeinAware, LoadAcceptRejectListener,
    OpenLoadItemAdapter.OnShareClickedListener {

    override val kodein by kodein()
    private val factory: OpenLoadItemViewModelFactory by instance()
    private var m_Data: Data? = null
    private lateinit var dialog: MaterialDialog
    private val recyclerViewState: Parcelable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_open_load_list,
                container,
                false
            );
        val view = binding.root
        openLoadsItemViewModel =
            ViewModelProvider(this, factory).get(OpenLoadItemViewModel::class.java)
        openLoadsItemViewModel.loadListener = this
        binding.fragment = this

        dialog = activity?.let { MaterialDialog(it) }!!

        try {
            m_Data = OpenLoadListFragment.myValue()
            m_Data?.let { Log.d("=", it.name) }

            init()

        } catch (e: Exception) {

        }

        return view
    }

    private fun init() {

        val list = ArrayList<ShipperConsigneeList>()

        for ((po, i) in m_Data?.shippers!!.withIndex()) {
            val obj = ShipperConsigneeList()
            obj.type = "Shipper"
            obj.shipper = i
            obj.pos = po
            list.add(obj)
        }

        for ((po, j) in m_Data?.consignees!!.withIndex()) {
            val obj = ShipperConsigneeList()
            obj.type = "consignee"
            obj.consignee = j
            obj.pos = po
            list.add(obj)
        }

        rAdapter = activity?.application?.let { OpenLoadItemAdapter(list, it) }!!
        rAdapter.setOnShareClickedListener(this)
        binding.pendingRecycle.adapter = rAdapter
//        binding.pendingRecycle.scrollToPosition(pos)
        rAdapter.notifyDataSetChanged()


        try {
            binding.distance.text = m_Data!!.totalMiles
            binding.load.text = m_Data!!.loadNo
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onStarted() {
        binding.progressBar.show()
    }


    override fun onSuccess(response: LoadAcceptRejectResponse,responseCode: Int) {
        binding.progressBar.hide()

        if (response.status.equals("success", true)) {

            init()
            Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onFailure(message: String) {
        binding.progressBar.visibility = View.GONE
//        activity?.toast(message)
        message?.let{
            if(it.contains("${AppConstants.ERROR_CODE_NO_INTERNET}")){
                dialog.show {
                    title(text = getString(R.string.dialog_title_alert))
                    message(text = getString(R.string.internet_error))
                    positiveButton(text = getString(R.string.ok))
                }
            }else{
                activity?.toast(message)
//                dialog.show {
//                    title(text = getString(R.string.dialog_title_alert))
//                    message(text = getString(R.string.server_error))
//                    positiveButton(text = getString(R.string.ok))
//                }
            }
        }
    }


    override fun shareClicked(clickType: String?, mData: ShipperConsigneeList, pos: Int) {

        dialog.show {
            title(text = getString(R.string.dialog_title_Msg))
            var msg = ""
            when(clickType){

                "CheckIn" -> msg = "Check-In"
                "Door" -> msg = "Press Door"
                "CheckOut" -> msg = "Check-Out"
                "Enroute" -> msg = "Enroute"
            }
            message(text = "Are you sure want to $msg?")
            positiveButton(text = getString(R.string.yes)){

                val jobj =  JSONObject();
                position = pos

                if (clickType == "CheckIn") {
                    Log.d("=>", "Click Check In")

                    if (mData.type == "Shipper") {
                        jobj.put("loadDetailId", m_Data?.shippers?.get(pos)?.id)
                        m_Data?.shippers?.get(pos)?.status = AppConstants.CHECK_IN
                    } else {
                        jobj.put("loadDetailId", m_Data?.consignees?.get(pos)?.id)
                        m_Data?.consignees?.get(pos)?.status = AppConstants.CHECK_IN
                    }
                    jobj.put("status", AppConstants.CHECK_IN)

                } else if (clickType == "Door") {
                    Log.d("=>", "Click Door")

                    if (mData.type == "Shipper") {
                        jobj.put("loadDetailId", m_Data?.shippers?.get(pos)?.id)
                        m_Data?.shippers?.get(pos)?.status = AppConstants.DOOR
                    } else {
                        jobj.put("loadDetailId", m_Data?.consignees?.get(pos)?.id)
                        m_Data?.consignees?.get(pos)?.status = AppConstants.DOOR
                    }
                    jobj.put("status", AppConstants.DOOR)
                } else if (clickType == "Enroute"){
                    Log.d("=>", "Click Enroute")

                    if (mData.type == "Shipper") {
                        jobj.put("loadDetailId", m_Data?.shippers?.get(pos)?.id)
                        m_Data?.shippers?.get(pos)?.status = AppConstants.ENROUTE
                    } else {
                        jobj.put("loadDetailId", m_Data?.consignees?.get(pos)?.id)
                        m_Data?.consignees?.get(pos)?.status = AppConstants.ENROUTE
                    }
                    jobj.put("status", AppConstants.ENROUTE)
                }else{

                    Log.d("=>", "Click Check Out")

                    if (mData.type == "Shipper") {
                        jobj.put("loadDetailId", m_Data?.shippers?.get(pos)?.id)
                        m_Data?.shippers?.get(pos)?.status = AppConstants.CHECK_OUT
                    } else {
                        jobj.put("loadDetailId", m_Data?.consignees?.get(pos)?.id)
                        m_Data?.consignees?.get(pos)?.status = AppConstants.CHECK_OUT
                    }
                    jobj.put("status", AppConstants.CHECK_OUT)
                }

                mData?.let { m_Data?.let { it1 -> openLoadsItemViewModel.rejectAcceptLoad(jobj) } }

            }
            negativeButton (text = getString(R.string.no))
        }

    }

    override fun clickNavigation(mData: ShipperConsigneeList, pos: Int) {

        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }

}