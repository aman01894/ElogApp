package com.example.elogapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import com.example.elogapp.R
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestingActivity : AppCompatActivity() {



    lateinit var onDutyTimer: CountDownTimer
    lateinit var driveTimer: CountDownTimer
    lateinit var sleepTimer: CountDownTimer
//    var remainingTimerTime = 0L
    var isFirstTime = true
    var event = ""


    lateinit var onDutyTv: TextView
    lateinit var driveTv: TextView
    lateinit var sleepTv: TextView

    lateinit var sleepBtn: TextView
    lateinit var onDutBtn: TextView
    lateinit var driveBtn: TextView
    lateinit var offDutyBtn: TextView
    lateinit var newDutyBtn: TextView
    lateinit var dutytv: TextView

    var dutyTimerStarted = false
    var driveTimerStarted = false
    var sleepTimerStarted = false
    var offDutyTimerStarted = false
    var allTimerStopped = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT).toString()

        var firstTime: Boolean = true
        PreferenceHelper().save(this,AppConstants.PREF_SLEEP_TIME_LEFT, AppConstants.TOTAL_SLEEP_HOURS_MILL_SECONDS.toString())

        onDutyTv = findViewById<TextView>(R.id.on_duty_time_left)
        onDutBtn = findViewById<TextView>(R.id.on_duty_btn)
        val resetBtn = findViewById<TextView>(R.id.reset)

        driveTv = findViewById<TextView>(R.id.drive_timer)
        driveBtn = findViewById<TextView>(R.id.drive_button)

        dutytv = findViewById<TextView>(R.id.duty_tv)
        sleepTv = findViewById<TextView>(R.id.sleep_timer)
        sleepBtn = findViewById<TextView>(R.id.sleep_clock_btn)
        offDutyBtn = findViewById<TextView>(R.id.off_duty_btn)
        newDutyBtn = findViewById<TextView>(R.id.newDutyStart)

        setDefaultClockTime()

        var currentEvent = currentEvent()
        var previousEvent = previousEvent()
        if(!previousEvent.isNullOrEmpty()) {
            stepDecider(previousEvent, currentEvent)
        }

        onDutBtn.setOnClickListener{

            event = currentEvent()
            stepDecider(event, AppConstants.START_DUTY_TITLE)

            onDutBtn.isEnabled = false
            driveBtn.isEnabled = true
            sleepBtn.isEnabled = true
            offDutyBtn.isEnabled = true
        }

        driveBtn.setOnClickListener{

            event = currentEvent()
            if(event == AppConstants.DRIVE_TITLE) {
                return@setOnClickListener
            }
            stepDecider(event, AppConstants.DRIVE_TITLE)

            driveBtn.isEnabled = false
            onDutBtn.isEnabled = true
            sleepBtn.isEnabled = true
            offDutyBtn.isEnabled = true

        }

        sleepBtn.setOnClickListener {
            event = currentEvent()
            if(event == AppConstants.SLEEP_TITLE) {
                return@setOnClickListener
            }
            stepDecider(event, AppConstants.SLEEP_TITLE)

            driveBtn.isEnabled = true
            onDutBtn.isEnabled = true
            sleepBtn.isEnabled = false
            offDutyBtn.isEnabled = true
        }


        offDutyBtn.setOnClickListener {

            event = currentEvent()
            stepDecider(event, AppConstants.OFF_DUTY_TITLE)

            driveBtn.isEnabled = true
            onDutBtn.isEnabled = true
            sleepBtn.isEnabled = true
            offDutyBtn.isEnabled = false

        }

        resetBtn.setOnClickListener {
            clearData()
            reloadData()
        }

        newDutyBtn.setOnClickListener {

            if(!isDutyStarted()) {
                startNewDuty(true)
                dutytv.text = "ON DUTY"
            }else{
                startNewDuty(false)

                dutytv.text = "OFF DUTY"
            }
        }
    }



    private fun stepDecider(current: String, newStatus: String){


        when(current){
            AppConstants.START_DUTY_TITLE ->{
                executeOnDutyToNewStus(newStatus)
            }
            AppConstants.DRIVE_TITLE ->{
                executeDriveToNewStatus(newStatus)
            }
            AppConstants.SLEEP_TITLE ->{
                executeSleepToNewStatus(current, newStatus)
            }
            AppConstants.OFF_DUTY_TITLE ->{
                executeOffDutyToNewStatus(newStatus)
            }
        }

    }

    private fun setEventState(currentEvent: String?, newEvent: String?){

//        var current = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT)
        PreferenceHelper().save(this,AppConstants.PREF_PREVIOUS_EVENT, currentEvent?:"")
        PreferenceHelper().save(this,AppConstants.PREF_CURRENT_EVENT, newEvent?:"")

    }

    fun currentEvent(): String {
        return PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT).toString()
    }

    private fun previousEvent(): String {
        return PreferenceHelper().getString(this, AppConstants.PREF_PREVIOUS_EVENT).toString()
    }




    private fun executeOnDutyToNewStus(newStatus: String){

        when(newStatus){

            AppConstants.DRIVE_TITLE ->{
                if(!dutyTimerStarted){
                    startDuty(newStatus)
                }
                driveStart(newStatus)
            }
            AppConstants.SLEEP_TITLE ->{
                if(!dutyTimerStarted){
                    startDuty(newStatus)
                }
                driveEnd()
                sleepStart(newStatus)
            }
            AppConstants.OFF_DUTY_TITLE ->{

                offDutyStart(newStatus)
                if(isDutyStarted()){
                    if(!dutyTimerStarted){
                        startDuty(newStatus)
                    }
                }else{
                    endDuty()
                    sleepEnd()
                    driveEnd()
                }

            }
        }

    }

    private fun executeDriveToNewStatus(newStatus: String){

        when(newStatus){
            AppConstants.START_DUTY_TITLE ->{
                if(!dutyTimerStarted){
                    startDuty(newStatus)
                }
                driveEnd()
            }
            AppConstants.SLEEP_TITLE ->{

                if(isDutyStarted()){
                    if(!dutyTimerStarted) {
                        startDuty(newStatus)
                    }
                }else{
                    endDuty()
                }
                if(!sleepTimerStarted) {
                    sleepStart(newStatus)
                }
                driveEnd()

            }
            AppConstants.OFF_DUTY_TITLE ->{

                offDutyStart(newStatus)
                if(isDutyStarted()){
                    if(!dutyTimerStarted) {
                        startDuty(newStatus)
                    }
                }else{
                    endDuty()
                    sleepEnd()
                    driveEnd()
                }

            }
        }

    }

    private fun executeSleepToNewStatus(current: String, newStatus: String){

        when(newStatus){
            AppConstants.START_DUTY_TITLE ->{

                if(isDutyStarted()){
                    if(!dutyTimerStarted) {
                        startDuty(newStatus)
                    }
                }else{
                    startDuty(newStatus)
                }

                driveEnd()
                sleepEnd()
            }
            AppConstants.DRIVE_TITLE ->{
                if(sleepTimerStarted) {
                    sleepEnd()
                }
                if(!driveTimerStarted) {
                    driveStart(newStatus)
                }
            }

            AppConstants.OFF_DUTY_TITLE ->{

                offDutyStart(newStatus)
                if(isDutyStarted()){
                    if(!dutyTimerStarted) {
                        startDuty(newStatus)
                    }
                }else{
                    endDuty()
                    sleepEnd()
                    driveEnd()
                    dutyTimerStarted = false
                    driveTimerStarted = false
                    sleepTimerStarted = false
                }

            }
        }
    }

    private fun executeOffDutyToNewStatus(newStatus: String){

        when(newStatus){
            AppConstants.START_DUTY_TITLE ->{
                PreferenceHelper().save(this,"DUTY_STARTED", true)
                    startDuty(newStatus)
            }
            AppConstants.DRIVE_TITLE ->{
                if(!dutyTimerStarted) {
                    startDuty(newStatus)
                }
                if(!driveTimerStarted) {
                    driveStart(newStatus)
                }

            }
            AppConstants.SLEEP_TITLE ->{

                if(isDutyStarted()){
                    if(!dutyTimerStarted) {
                        startDuty(newStatus)
                    }
                }else{
                    endDuty()
                    sleepEnd()
                    driveEnd()
                }
                if(!sleepTimerStarted) {
                    sleepStart(newStatus)
                }
            }

            AppConstants.OFF_DUTY_TITLE ->{
                dutyTimerStarted = false
                driveTimerStarted = false
                sleepTimerStarted = false
            }
        }


        if(offDutyTimerStarted) {
            offDutyEnd()
        }

    }


    private fun startDuty(newEvent: String) {

        var remainingTimerTime = 0L
        var event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT)
        if(!event.equals(newEvent,true)){
            setEventState(event, newEvent)
            PreferenceHelper().save(this, AppConstants.PREF_DUTY_START_TIME_STAMP, System.currentTimeMillis().toString())
            AppUtils.logger("=>Updating start time")
        }


        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_DUTY_START_TIME_STAMP).toString()
        var timeStampStartMill = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStartMill = timeTemp.toLong()
        }

        var remainingTimeFromPref = PreferenceHelper().getString(this, "REMAINING_ON_DUTY").toString()

        if(!remainingTimeFromPref?.isNullOrEmpty()!!) {
            remainingTimerTime =
                remainingTimeFromPref?.toLong()!! - (System.currentTimeMillis() - timeStampStartMill)
        }

        AppUtils.millisecondToTime("Timer remaining Time ", remainingTimerTime)

        startOnDutyTimer(remainingTimerTime)

        }

        private fun endDuty() {

            dutyTimerStarted = false

            var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_DUTY_START_TIME_STAMP).toString()
            var timeStampStart = 0L
            if(!timeTemp.isNullOrEmpty()){
                timeStampStart = timeTemp.toLong()
            }


            var remainingPref = PreferenceHelper().getString(this, "REMAINING_ON_DUTY")
            val consumed = System.currentTimeMillis() - timeStampStart?.toLong()!!
            val remaining = remainingPref?.toLong()!! - consumed

            PreferenceHelper().save(this, "REMAINING_ON_DUTY", remaining.toString())

            if(this::onDutyTimer.isInitialized) {
                onDutyTimer.cancel()
            }
        }




    private fun driveStart(newEvent: String) {

        var remainingTimerTime = 0L
        var event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT)
        if(!event.equals(newEvent,true)){
            setEventState(event, newEvent)
            PreferenceHelper().save(this, AppConstants.PREF_DRIVE_START_TIME_STAMP, System.currentTimeMillis().toString())
            AppUtils.logger("=>Updating drive time")
        }


        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_DRIVE_START_TIME_STAMP).toString()
        var timeStampStartMill = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStartMill = timeTemp.toLong()
        }

        var remainingTimeFromPref = PreferenceHelper().getString(this, "REMAINING_DRIVE")
        remainingTimerTime = remainingTimeFromPref?.toLong()!! - (System.currentTimeMillis() - timeStampStartMill)

        AppUtils.millisecondToTime("Timer remaining Time ", remainingTimerTime)


        startDriveTimer(remainingTimerTime)

    }

    private fun driveEnd() {

        driveTimerStarted = false

        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_DRIVE_START_TIME_STAMP).toString()
        var timeStampStart = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStart = timeTemp.toLong()
        }

        var remainingPref = PreferenceHelper().getString(this, "REMAINING_DRIVE")
        val consumed = System.currentTimeMillis() - timeStampStart
        val remaining = remainingPref?.toLong()!! - consumed

        PreferenceHelper().save(this, "REMAINING_DRIVE", remaining.toString())

        if(this::driveTimer.isInitialized) {
            driveTimer.cancel()
        }

    }


    private fun sleepStart(newEvent: String) {

        var remainingTimerTime = 0L
        var event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT)
        if(!event.equals(newEvent,true)){
            setEventState(event, newEvent)
            PreferenceHelper().save(this, AppConstants.PREF_SLEEP_START_TIME_STAMP, System.currentTimeMillis().toString())
            AppUtils.logger("=>Updating sleep time")
        }

        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_SLEEP_START_TIME_STAMP).toString()
        var timeStampStartMill = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStartMill = timeTemp.toLong()
        }

        var remainingTimeFromPref = PreferenceHelper().getString(this, "REMAINING_SLEEP")
        remainingTimerTime = remainingTimeFromPref?.toLong()!! - (System.currentTimeMillis() - timeStampStartMill)

        AppUtils.millisecondToTime("Timer remaining Time ", remainingTimerTime)

        startSleepTimer(remainingTimerTime)

    }

    private fun sleepEnd() {

        sleepTimerStarted = false

        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_SLEEP_START_TIME_STAMP).toString()
        var timeStampStart = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStart = timeTemp.toLong()
        }

        var remainingPref = PreferenceHelper().getString(this, "REMAINING_SLEEP")
        val consumed = System.currentTimeMillis() - timeStampStart
        val remaining = remainingPref?.toLong()!! - consumed

        PreferenceHelper().save(this, "REMAINING_SLEEP", remaining.toString())

        if(consumed > AppConstants.TOTAL_SLEEP_HOURS_MILL_SECONDS){
            Toast.makeText(applicationContext, getString(R.string.complete_sleep_hours), Toast.LENGTH_LONG).show()
        }

        if(this::sleepTimer.isInitialized) {
            sleepTimer.cancel()
        }
    }


    private fun offDutyStart(newEvent: String) {

        offDutyTimerStarted = true
        var remainingTimerTime = 0L
        var event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT)
        if(!event.equals(newEvent,true)){
            setEventState(event, newEvent)
            PreferenceHelper().save(this, AppConstants.PREF_OFF_DUTY_START_TIME_STAMP, System.currentTimeMillis().toString())
            AppUtils.logger("=>Updating Offduty time")
        }

        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_OFF_DUTY_START_TIME_STAMP).toString()
        var timeStampStartMill = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStartMill = timeTemp.toLong()
        }

        var remainingTimeFromPref = PreferenceHelper().getString(this, "REMAINING_OFF_DUTY")
        remainingTimerTime = remainingTimeFromPref?.toLong()!! - (System.currentTimeMillis() - timeStampStartMill)

        AppUtils.millisecondToTime("Timer remaining Time ", remainingTimerTime)



    }

    private fun offDutyEnd() {

        offDutyTimerStarted = false

        var timeTemp = PreferenceHelper().getString(this, AppConstants.PREF_OFF_DUTY_START_TIME_STAMP).toString()
        var timeStampStart = 0L
        if(!timeTemp.isNullOrEmpty()){
            timeStampStart = timeTemp.toLong()
        }

        var remainingPref = PreferenceHelper().getString(this, "REMAINING_OFF_DUTY")
        val consumed = System.currentTimeMillis() - timeStampStart
        val remaining = remainingPref?.toLong()!! - consumed

        PreferenceHelper().save(this, "REMAINING_SLEEP", remaining.toString())

        if(consumed > AppConstants.TOTAL_SLEEP_HOURS_MILL_SECONDS){
            Toast.makeText(applicationContext, getString(R.string.complete_off_duty_hours), Toast.LENGTH_LONG).show()
        }


    }


    private fun startOnDutyTimer(time: Long) {

            if(this::onDutyTimer.isInitialized){
                onDutyTimer.cancel()
            }
            onDutyTimer = object : CountDownTimer(time, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    dutyTimerStarted = true

                    val hours: Int = (millisUntilFinished / (60 * 60 * 1000)).toInt()
                    val minutes: Int = ((millisUntilFinished / (60 * 1000)) % 60).toInt()
                    val sec: Int = ((millisUntilFinished / 1000) % 60).toInt()
                    AppUtils.logger("Step: OnDuty Timer$hours:$minutes:$sec\"")
                    var dutyHour = String.format("%02d:%02d:%02d", hours, minutes, sec)
                    CoroutineScope(Dispatchers.Main).launch {
                        onDutyTv.text = dutyHour
                    }
                }

                override fun onFinish() {
//                    dutyTimerStarted = false
                    AppUtils.logger("Step: onFinish CountDownTimer")
                    var dutyHour = String.format("%02d:%02d", 0, 0)
                    CoroutineScope(Dispatchers.Main).launch {
                        onDutyTv.text = dutyHour
                    }
                }
            }

            onDutyTimer.start()
        }


    private fun startDriveTimer(time: Long) {

        if(this::driveTimer.isInitialized){
            driveTimer.cancel()
        }
        driveTimer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                driveTimerStarted = true
                val hours: Int = (millisUntilFinished / (60 * 60 * 1000)).toInt()
                val minutes: Int = ((millisUntilFinished / (60 * 1000)) % 60).toInt()
                val sec: Int = ((millisUntilFinished / 1000) % 60).toInt()
                AppUtils.logger("Step: Drive Timer$hours:$minutes:$sec\"")
                var dutyHour = String.format("%02d:%02d:%02d", hours, minutes, sec)
                CoroutineScope(Dispatchers.Main).launch {
                    driveTv.text = dutyHour
                    //AppUtils.logger("===> $dutyHour")
                }
            }

            override fun onFinish() {
                //driveTimerStarted = false
                AppUtils.logger("Step: onFinish CountDownTimer")
                var dutyHour = String.format("%02d:%02d", 0, 0)
                CoroutineScope(Dispatchers.Main).launch {
                    driveTv.text = dutyHour
                }
            }
        }

        driveTimer.start()
    }

    private fun startSleepTimer(time: Long) {

        if(this::sleepTimer.isInitialized){
            sleepTimer.cancel()
        }
        sleepTimer = object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                sleepTimerStarted = true
                val hours: Int = (millisUntilFinished / (60 * 60 * 1000)).toInt()
                val minutes: Int = ((millisUntilFinished / (60 * 1000)) % 60).toInt()
                val sec: Int = ((millisUntilFinished / 1000) % 60).toInt()
                AppUtils.logger("Step: Sleep Timer$hours:$minutes:$sec\"")
                var dutyHour = String.format("%02d:%02d:%02d", hours, minutes, sec)
                CoroutineScope(Dispatchers.Main).launch {
                    sleepTv.text = dutyHour
                }
            }

            override fun onFinish() {
                //sleepTimerStarted = false
                AppUtils.logger("Step: onFinish CountDownTimer")
                var dutyHour = String.format("%02d:%02d", 0, 0)
                CoroutineScope(Dispatchers.Main).launch {
                    sleepTv.text = dutyHour
                }
            }
        }

        sleepTimer.start()
    }


    private fun clearData() {
        PreferenceHelper().save(this,AppConstants.PREF_CURRENT_EVENT, "")
        PreferenceHelper().save(this,"REMAINING_ON_DUTY", "")
        PreferenceHelper().save(this,"REMAINING_DRIVE","")
        PreferenceHelper().save(this,"REMAINING_SLEEP", "")
        PreferenceHelper().save(this,"REMAINING_OFF_DUTY", "")

        driveBtn.isEnabled = true
        onDutBtn.isEnabled = true
        sleepBtn.isEnabled = true
        offDutyBtn.isEnabled = true

        if(this::onDutyTimer.isInitialized)
        onDutyTimer.cancel()
        if(this::driveTimer.isInitialized)
        driveTimer.cancel()
        if(this::sleepTimer.isInitialized)
        sleepTimer.cancel()



        onDutyTv.text = "00:00"
        sleepTv.text = "00:00"
        driveTv.text = "00:00"
    }

    private fun setDefaultClockTime(){

        var remainingTimerTime = 0L
        var t1 = PreferenceHelper().getString(this, AppConstants.PREF_DUTY_START_TIME_STAMP).toString()
        var tm1 = 0L
        if(!t1.isNullOrEmpty()){
            tm1 = t1.toLong()
        }
        var remainingTimeOnduty = PreferenceHelper().getString(this, "REMAINING_ON_DUTY").toString()

        if(!remainingTimeOnduty?.isNullOrEmpty()!!) {
            remainingTimerTime =
                remainingTimeOnduty?.toLong()!! - (System.currentTimeMillis() - tm1)

            onDutyTv.text = remainingTimeOnduty?.toLong()?.let { AppUtils.millisecondToTime(it) }
        }


        var t2 = PreferenceHelper().getString(this, AppConstants.PREF_SLEEP_START_TIME_STAMP).toString()
        var tm2 = 0L
        if(!t2.isNullOrEmpty()){
            tm2 = t2.toLong()
        }

        var remainingTimeSleep = PreferenceHelper().getString(this, "REMAINING_SLEEP")
        val rt2 = remainingTimeSleep?.toLong()!! - (System.currentTimeMillis() - tm2)
        sleepTv.text = rt2?.let { AppUtils.millisecondToTime(it) }

        var t3 = PreferenceHelper().getString(this, AppConstants.PREF_DRIVE_START_TIME_STAMP).toString()
        var tm3 = 0L
        if(!t3.isNullOrEmpty()){
            tm3 = t3.toLong()
        }

        var remainingTimeFromPref = PreferenceHelper().getString(this, "REMAINING_DRIVE")
        val rt3 = remainingTimeFromPref?.toLong()!! - (System.currentTimeMillis() - tm3)

        driveTv.text = rt3?.let { AppUtils.millisecondToTime(it) }

    }


    fun reloadData() {

        PreferenceHelper().save(this,"DUTY_STARTED", false)
        PreferenceHelper().save(this,AppConstants.PREF_CURRENT_EVENT, AppConstants.OFF_DUTY_TITLE)
        PreferenceHelper().save(this,"REMAINING_ON_DUTY", AppConstants.TOTAL_DUTY_HOURS_MILLI_SECONDS.toString())
        PreferenceHelper().save(this,"REMAINING_DRIVE", AppConstants.TOTAL_DRIVE_HOURS_MILL_SECONDS.toString())
        PreferenceHelper().save(this,"REMAINING_SLEEP", AppConstants.TOTAL_SLEEP_HOURS_MILL_SECONDS.toString())
        PreferenceHelper().save(this,"REMAINING_OFF_DUTY", AppConstants.TOTAL_SLEEP_HOURS_MILL_SECONDS.toString())

        event = PreferenceHelper().getString(this, AppConstants.PREF_CURRENT_EVENT).toString()
    }

    private fun isDutyStarted(): Boolean{
        return PreferenceHelper().getBoolean(this,"DUTY_STARTED", false)
    }

    private fun startNewDuty(start: Boolean): Boolean{
        return PreferenceHelper().getBoolean(this,"DUTY_STARTED", start)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(this::onDutyTimer.isInitialized)
            onDutyTimer.cancel()
        if(this::driveTimer.isInitialized)
            driveTimer.cancel()
        if(this::sleepTimer.isInitialized)
            sleepTimer.cancel()

    }



}