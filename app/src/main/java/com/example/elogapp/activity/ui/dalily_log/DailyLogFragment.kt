package com.example.elogapp.activity.ui.dalily_log

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentDailyLogsBinding
import com.example.elogapp.repository.responses.dailylog.DailyLogChartResponse
import com.example.elogapp.repository.responses.dailylog.DailyLogListener
import com.example.elogapp.repository.responses.dailylog.DailyLogResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DailyLogFragment : Fragment(), KodeinAware, DailyLogListener,
    DailyLogAdapter.OnShareClickedListener {

    override val kodein by kodein()
    private val factory: DailyLogViewModelFactory by instance()
    private lateinit var rAdapter: DailyLogAdapter
    private lateinit var dailyLogViewModel: DailyLogViewModel
    private lateinit var binding: FragmentDailyLogsBinding
    private lateinit var dialog: MaterialDialog
    private lateinit var result: MutableList<String>
    private lateinit var myData: DailyLogData
    private lateinit var logList: List<DailyLogData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_logs, container, false);
        val view = binding.root
        dailyLogViewModel = ViewModelProvider(this, factory)[DailyLogViewModel::class.java]
        dailyLogViewModel.loadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        binding.backBtn.setOnClickListener {
            activity!!.onBackPressed()
        }

        callApiForData(AppConstants.API_KEY_DRIVER_LOGS);
        return view
    }

    private fun callApiForData(API_KEY: Int) {
        dailyLogViewModel.getDailyLog(API_KEY)
    }

    private fun sendCertificationDateToServer(event: RequestBody, API_KEY: Int){
        dailyLogViewModel.sendCertificationDateToServer(event, API_KEY)
    }

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(loadDisRes: DailyLogResponse, API_KEY: Int) {
        binding.progressBar.hide()

        try {

            when (API_KEY) {

                AppConstants.API_KEY_DRIVER_LOGS -> {

                    val any = loadDisRes!!.data.size

                    if (any > 0) {
                        logList = loadDisRes.data
                        rAdapter = activity?.let {
                            DailyLogAdapter(it.applicationContext, logList) {
                                    mData -> listItemClicked(mData)
                            }
                        }!!

                        binding.dailyLogRecycle.adapter = rAdapter
                        rAdapter.setOnShareClickedListener(this)
                        rAdapter.notifyDataSetChanged()

                    } else {

                        dialog.show {
                            title(text = getString(R.string.dialog_title_Msg))
                            message(text = getString(R.string.no_daily_log))
                            positiveButton(text = getString(R.string.ok)) {
                                activity?.onBackPressed()
                            }
                        }
                    }
                }

                AppConstants.API_KEY_CERTIFICATION ->{

                    if(loadDisRes.status == "Success"){
                        dialog.show {
                            title(text = getString(R.string.dialog_title_Msg))
                            message(text = "${loadDisRes.message}")
                            positiveButton(text = getString(R.string.ok)) {
                                callApiForData(AppConstants.API_KEY_DRIVER_LOGS);
                            }
                        }
                    }
                }
            }
        }
            catch (e: Exception){
                e.printStackTrace()
            }


    }

    override fun onSuccess(response: DailyLogChartResponse, API_KEY: Int) {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()
        //activity?.toast(message)

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


    private fun listItemClicked(mData: DailyLogData) {
//        Toast.makeText(activity, "Clicked: ${mData.name}", Toast.LENGTH_SHORT).show()

        myData = mData
        //openPostActivityCustom.launch(10)


        val navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.fragment_daily_log_chart)

        }

    private fun viewItemClicked(mData: DailyLogData) {
        Toast.makeText(activity, "Clicked:", Toast.LENGTH_SHORT).show()

        myData = mData
        openPostActivityCustom.launch(10)
    }

    private val openPostActivityCustom =
        registerForActivityResult(PostActivityContract()) { result ->

            if (result != null && result.size > 0 && result[0] != "null") {
                Log.d("==>", "Result : $result")
                this.result = result

                val mainObj = JSONObject()
                mainObj.put("logDate", myData.date)
                mainObj.put("isCertified", 1)
                mainObj.put("image", result)


                val requestBody = mainObj.toString()
                    ?.toRequestBody("application/json".toMediaTypeOrNull())

                Log.d("==>", "Result : ${mainObj.toString()}")
                sendCertificationDateToServer(requestBody!!, AppConstants.API_KEY_CERTIFICATION)

            }
        }


    class PostActivityContract : ActivityResultContract<Int, MutableList<String>>() {

        override fun createIntent(context: Context, input: Int): Intent {
                return Intent(context, SignatureActivity::class.java)
            }


        override fun parseResult(resultCode: Int, intent: Intent?): MutableList<String> {

            val result: MutableList<String> = ArrayList()
            val bundle: Bundle? = intent?.extras
            result.add(bundle?.get("image64Code").toString())

            return if (resultCode == Activity.RESULT_OK && result.size > 0)
                result
            else
                result
        }
    }

    override fun shareClicked(clickType: String?, mData: DailyLogData, pos: Int) {

        myData = mData

        if (clickType == "Certificate") {
            Log.d("=>", "Certificate")

            openPostActivityCustom.launch(10)

        } else if (clickType == "date") {
            Log.d("=>", "Date")

            var bundle = bundleOf("date" to mData.date)
            val navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment)
            navController.navigate(R.id.fragment_daily_log_chart, bundle)


        }

    }


    override fun clickNavigation(mData: DailyLogData, pos: Int) {
        TODO("Not yet implemented")
    }


}