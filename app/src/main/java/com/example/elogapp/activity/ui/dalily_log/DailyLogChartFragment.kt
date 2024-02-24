package com.example.elogapp.activity.ui.dalily_log

import TableData
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentDailyLogsChartBinding
import com.example.elogapp.repository.model.ChartInfo
import com.example.elogapp.repository.responses.dailylog.DailyLogChartResponse
import com.example.elogapp.repository.responses.dailylog.DailyLogListener
import com.example.elogapp.repository.responses.dailylog.DailyLogResponse
import com.example.elogapp.util.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


private lateinit var chartInfoLog: List<ChartInfo>
class DailyLogChartFragment : Fragment(), KodeinAware, DailyLogListener {

    override val kodein by kodein()
    private val factory: DailyLogChartViewModelFactory by instance()
    private var rAdapter: DailyLogChartAdapter? = null
    private lateinit var dailyLogChartViewModel: DailyLogChartViewModel
    private lateinit var binding: FragmentDailyLogsChartBinding
    private lateinit var dialog: MaterialDialog
    private lateinit var result: MutableList<String>
    private lateinit var myData: TableData
    private var mDriverId: Int = 0
    private lateinit var userDetails: UserDetails
    private lateinit var webView: WebView
    private lateinit var currentDate: Date
    private var chartDataValue = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_logs_chart, container, false);
        val view = binding.root
        dailyLogChartViewModel = ViewModelProvider(this, factory)[DailyLogChartViewModel::class.java]
        dailyLogChartViewModel.loadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        val myDate = arguments?.getString("date").toString()

        currentDate = AppUtils.getStringToDate(myDate)

        val timeZone = PreferenceHelper().getString(requireActivity(),AppConstants.TIME_ZONE_KEY)

        binding.dateTxt.text = timeZone?.let { AppUtils.timeZoneConverter(myDate, it) }

        dailyLogChartViewModel.getUserDetails().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                userDetails = user

                callApiForData(myDate, userDetails.id, AppConstants.API_KEY_DAILY_LOG_CHART);
            }
        })


        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.arrowLeft.setOnClickListener {

            currentDate = AppUtils.decrementDateByOne(currentDate)
            val serverDate = AppUtils.getServerDate(currentDate)

            Log.d("PREVIOUS DATE:", "$serverDate")

            binding.arrowLeft.isEnabled = false
            binding.arrowRight.isEnabled = false

            binding.dateTxt.text = serverDate//timeZone?.let { AppUtils.timeZoneConverter(serverDate, it) }
            callApiForData(serverDate, userDetails.id, AppConstants.API_KEY_DAILY_LOG_CHART);

        }

        binding.arrowRight.setOnClickListener {

            currentDate = AppUtils.incrementDateByOne(currentDate)
            val serverDate = AppUtils.getServerDate(currentDate)

            binding.arrowLeft.isEnabled = false
            binding.arrowRight.isEnabled = false

            Log.d("NEXT DATE:", "$serverDate")
            //Log.d("Server DATE:", "${AppUtils.getServerDateTime()}")

            binding.dateTxt.text = serverDate//timeZone?.let { AppUtils.timeZoneConverter(serverDate, it) }
            callApiForData(serverDate, userDetails.id, AppConstants.API_KEY_DAILY_LOG_CHART);

        }



       // createChart()

        return view
    }

    private fun callApiForData(eventDate: String, driverId: Int,API_KEY: Int) {
        dailyLogChartViewModel.getDailyLog(eventDate,driverId, API_KEY)
    }

    private fun sendCertificationDateToServer(event: RequestBody, API_KEY: Int){
        dailyLogChartViewModel.sendCertificationDateToServer(event, API_KEY)
    }

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(response: DailyLogResponse, API_KEY: Int) {
        TODO("Not yet implemented")
    }

    override fun onSuccess(loadDisRes: DailyLogChartResponse, API_KEY: Int) {
        binding.progressBar.hide()
        binding.arrowLeft.isEnabled = true
        binding.arrowRight.isEnabled = true

        try {

            when (API_KEY) {

                AppConstants.API_KEY_DAILY_LOG_CHART -> {

                    val any = loadDisRes.data.tableData.size
                    val myList = loadDisRes.data.tableData
                    chartDataValue = loadDisRes.data.chartData.toString()
                    chartInfoLog = loadDisRes.data.chartData

                    if (any > 0) {
                        try {
                            rAdapter = null
                            rAdapter = activity?.let {
                                DailyLogChartAdapter(
                                    it.applicationContext,
                                    myList
                                ) { mData -> listItemClicked(mData) }
                            }!!
                            binding.dailyLogRecycle.adapter = rAdapter
                            rAdapter!!.notifyDataSetChanged()
                        }catch (e: Exception){
                            e.printStackTrace()
                        }

                    } else {


                        try {
                            rAdapter = null
                            rAdapter = activity?.let {
                                DailyLogChartAdapter(
                                    it.applicationContext,
                                    myList
                                ) { mData -> listItemClicked(mData) }
                            }!!
                            binding.dailyLogRecycle.adapter = rAdapter
                            rAdapter!!.notifyDataSetChanged()
                        }catch (e: Exception){
                            e.printStackTrace()
                        }

                        dialog.show {
                            title(text = getString(R.string.dialog_title_Msg))
                            message(text = getString(R.string.no_daily_log))
                            positiveButton(text = getString(R.string.ok)) {
                                //activity?.onBackPressed()
                            }
                        }
                    }


                    if(chartInfoLog.isNotEmpty()){
                        createChart(chartInfoLog)
                        //prepareChartData(chartInfoLog)
                    }

                }
            }
        }
            catch (e: Exception){
                e.printStackTrace()
            }


    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()
        binding.arrowLeft.isEnabled = true
        binding.arrowRight.isEnabled = true
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

            }
        }

    }


    private fun listItemClicked(mData: TableData) {
//        Toast.makeText(activity, "Clicked: ${mData.name}", Toast.LENGTH_SHORT).show()

        myData = mData

    }


    private fun createChart(chartInfoLog: List<ChartInfo>) {

        webView = binding.canvasChart
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        context?.let {
            webView.addJavascriptInterface(JsWebInterface(it), "Interface")
        }
        webView.loadUrl("file:///android_asset/dailylog-chart.html")

        prepareChartData(chartInfoLog)

    }

    private fun prepareChartData(chartInfo : List<ChartInfo>) {

        val sharedPreference =
            activity?.getSharedPreferences(AppConstants.PREF_DB, Context.MODE_PRIVATE)
        val timeZone = sharedPreference?.getString(AppConstants.TIME_ZONE_KEY, "").toString()

        Log.d("Tag1", chartInfo.toString())
        //val timeZone = "America/Los_Angeles"
        CoroutineScope(Dispatchers.Main).async {
            try {

                chartInfo.forEach {
                    it.x = getDate(it.x, timeZone)
                }

            } catch (ex: Exception) {
                ex.printStackTrace();
                //Log.d("Tag", ex.message.toString())
            }
        }

        Log.d("Tag2", chartInfo.toString())

    }


    class JsWebInterface(context: Context) {
        val mContext = context

        @JavascriptInterface
        fun makeToast(str: String) {
            //Log.i("TAG==>", str)
            Toast.makeText(mContext, str, Toast.LENGTH_LONG).show()
        }

        @JavascriptInterface
        fun chartData(): String {
            val result: JsonArray = Gson().toJsonTree(
                chartInfoLog,
                object : TypeToken<List<ChartInfo?>?>() {}.getType()
            ) as JsonArray
            return result.toString()

        }
    }



    private fun getDate(ourDate: String, timeZone: String?): String {
        var ourDate: String = ourDate
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //this format changeable
            dateFormatter.timeZone = TimeZone.getTimeZone(timeZone)
            ourDate = dateFormatter.format(value)

            Log.i("ourDate", ourDate);
        } catch (e: java.lang.Exception) {
            ourDate = "00-00-0000 00:00"
        }
        return ourDate
    }









}