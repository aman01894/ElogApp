package com.example.elogapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.Outbound
import com.google.gson.Gson
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    @JvmStatic
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat(AppConstants.DATE_FORMAT)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(Date())
    }
    @JvmStatic
    fun getCurrentTime():String{
        val sdf = SimpleDateFormat(AppConstants.TIME_FORMAT)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(Date())
    }

    @JvmStatic
    fun getDateTime():String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(Date())
    }

    @JvmStatic
    fun getServerDateTime():String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_1)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(Date())
    }

    @JvmStatic
    fun generateRowId():String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_3)
        sdf.timeZone = TimeZone.getTimeZone("utc")

        val rand = Random()
        val id = String.format("%04d", rand.nextInt(10000))

        return id + "_" + sdf.format(Date())
    }


    @JvmStatic
    fun generateRandom6Digit():Int{

        val rand = Random()
        val id = String.format("%06d", rand.nextInt(1000000))

        return id.toInt()
    }

    @JvmStatic
    fun getServerDateTime(date: Date):String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_1)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(date)
    }

    @JvmStatic
    fun getServerDate(date: Date):String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_2)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(date)
    }

    @JvmStatic
    fun getServerDateTimeFromMilliSec(mill: Long):String{
        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_1)
        sdf.timeZone = TimeZone.getTimeZone("utc")
        return sdf.format(Date(mill))
    }

    fun getStringToDate(strDate: String): Date {

        //val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(strDate)
        val date = SimpleDateFormat("yyyy-MM-dd").parse(strDate)
        println(date.time)

        return Date(date.time)

    }


    fun findDutyNumber(): Long {
        val date = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return formatter.format(calendar.time).toLong()
    }

    @JvmStatic
    fun logger(log: String){
        Log.d("ELOG =>", log)
    }

    @JvmStatic
    fun loggerRequest(log: String){
        Log.d("HTTP Request : ", log)
        sentryLoggerInfo(log)
    }

    @JvmStatic
    fun loggerResponse(log: String){
        Log.d("HTTP Response : ", log)
        sentryLoggerInfo(log)
    }

    @JvmStatic
    fun loggerError(log: String){
        Log.d("HTTP Response : ", log)
        sentryLoggerError(log)
    }

    fun sentryLoggerError(msg: String){

        val breadcrumb = Breadcrumb().apply {
            category = "ERROR"
            message = msg
            level = SentryLevel.ERROR
        }
        Sentry.addBreadcrumb(msg, "ERROR")

    }

    fun sentryLoggerInfo(msg: String){

        val breadcrumb = Breadcrumb().apply {
            category = "INFO"
            message = "Authenticated user"
            level = SentryLevel.INFO
        }
        //Sentry.addBreadcrumb(breadcrumb)
        Sentry.addBreadcrumb(msg, "INFO")

    }

    /**
     * Return Android Device ID
     */
    fun deviceId(mContext: Context): String {

        return Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     *
     */
    fun timeZoneConverter(time: String, timeZone: String): String{

        var result = ""
        try {
        val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//            val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

            val date: Date = utcFormat.parse(time)

            val pstFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            pstFormat.setTimeZone(TimeZone.getTimeZone(timeZone))

            System.out.println(pstFormat.format(date))

            result = pstFormat.format(date).toString()
        }catch (e: Exception){
            e.printStackTrace()
        }

        return result
    }

    /**
     *
     */
    fun dateToTimeWithTimeZone(time: String, timeZone: String): String{

        var result = ""
        try {
            val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//            val utcFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

            val date: Date = utcFormat.parse(time)

            val pstFormat: DateFormat = SimpleDateFormat("HH:mm")
            pstFormat.setTimeZone(TimeZone.getTimeZone(timeZone))

            System.out.println(pstFormat.format(date))

            result = pstFormat.format(date).toString()
        }catch (e: Exception){
            e.printStackTrace()
        }

        return result
    }


    /**
     * To Check weather network is available or not
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    /**
     * Get next date from current selected date
     *
     * @param date date
     */
    fun incrementDateByOne(date: Date): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 1)
        return c.time
    }

    /**
     * Get previous date from current selected date
     *
     * @param date date
     */
    fun decrementDateByOne(date: Date): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -1)
        return c.time
    }


    fun deleteDataFromOutBound(mEvent : Outbound){

        if(mEvent.eventType == AppConstants.EVENT_TYPE_DOC_UPLOAD){
            val jsonInString: String = Gson().toJson(mEvent)
            val json = JSONObject(jsonInString)
            val gsonObj = JSONObject(json.get("gson").toString())
            val eventObj = JSONObject(gsonObj.get("eventData").toString())
            val docId = eventObj.get("docId").toString()
            MyRoomDb.instance?.getDocGalleryDao()?.deleteDocGallery(docId)
        }
        MyRoomDb.instance?.getOutboundDao()?.delete(mEvent)
    }


    fun millisecondToTime(msg: String, milliSeconds: Long){

        val hours: Int = (milliSeconds / (60 * 60 * 1000)).toInt()
        val minutes: Int = ((milliSeconds / (60 * 1000)) % 60).toInt()
        val sec: Int = ((milliSeconds / 1000) % 60).toInt()
        logger("$msg HH:mm:ss: $hours:$minutes:$sec\"")

    }

    fun millisecondToTime(milliSeconds: Long): String {

        val hours: Int = (milliSeconds / (60 * 60 * 1000)).toInt()
        val minutes: Int = ((milliSeconds / (60 * 1000)) % 60).toInt()
        val sec: Int = ((milliSeconds / 1000) % 60).toInt()
        logger("HH:mm:ss: $hours:$minutes:$sec\"")

        if (hours > 0 || minutes > 0) {
            return "$hours:$minutes:$sec"
        }

        return String.format("%02d:%02d", 0, 0)


    }


}