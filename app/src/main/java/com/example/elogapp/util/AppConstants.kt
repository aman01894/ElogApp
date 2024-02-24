package com.example.elogapp.util


enum class Pref{

    REMAINING_ON_DUTY

}

object AppConstants {

    const val BASE_URL = "http://eldapi.migps.in/api/"
//    const val BASE_URL = "http://www.theroadlink.com/api/"



    const val TAG = "ELOG APP LOG =>"

    const val SPEED_TIME_INTERVAL = 2
//    const val SPEED_DIALOG_INTERVAL = 1000*60*2
    const val SPEED_CHECK = 5

    const val PREF_DB = "AUTH_DB"
    const val AUTH_KEY = "AUTH_KEY"
    const val USER_KEY = "USER_KEY"
    const val PASS_KEY = "PASS_KEY"
    const val TIME_ZONE_KEY = "TIME_ZONE_KEY"
    const val TRAILER_ID_KEY = "TRAILER_ID"
    const val TRAILER_NO_KEY = "TRAILER_NO"
    const val VEHICLE_ID_KEY = "VEHICLE_ID"
    const val VEHICLE_NO_KEY = "VEHICLE_NO"
    const val LAST_PACKET_RECEIVED_TIME = "LAST_PACKET_RECEIVED_TIME"
    const val LAST_DRIVER_EVENT = "LAST_DRIVER_EVENT"


    const val PREF_FEATURE_ELD = "featureEld"
    const val PREF_FEATURE_DISPATCH = "featureDispatch"
    const val PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN"
    const val PREF_DEVICE_NAME = "PREF_DEVICE_NAME"
    const val PREF_DEVICE_ID_MAC = "PREF_DEVICE_ID_MAC"
    const val PREF_SHOW_ON_DUTY_DIALOG = "PREF_SHOW_ON_DUTY_DIALOG"


    const val PREF_DRIVE_TIME_LEFT = "PREF_DRIVE_TIME_LEFT"
    const val PREF_IS_DUTY_VIOLATION_GENERATED = "PREF_IS_DUTY_VIOLATION_GENERATED"
    const val PREF_IS_DUTY_STARTED = "PREF_IS_DUTY_STARTED"
    const val PREF_IS_FOR_TESTING = "PREF_IS_FOR_TESTING"


    const val PREF_SLEEP_CLASS_OBJ = "PREF_SLEEP_CLASS_OBJ"
    const val PREF_IS_FIRST_SPLIT_SLEEP = "PREF_IS_FIRST_SPLIT_SLEEP"
    const val PREF_DRIVE_TIME_TILL_FIRST_SPLIT_SLEEP = "PREF_FIRST_SLEEP_DRIVE_TIME"
    const val PREF_DUTY_TIME_TILL_FIRST_SPLIT_SLEEP = "PREF_FIRST_SLEEP_DUTY_TIME"

    const val PREF_IS_SECOND_SPLIT_SLEEP = "PREF_IS_SECOND_SPLIT_SLEEP"
    const val PREF_SECOND_SLEEP_DRIVE_TIME = "PREF_SECOND_SLEEP_DRIVE_TIME"
    const val PREF_SECOND_SLEEP_DUTY_TIME = "PREF_SECOND_SLEEP_DUTY_TIME"
    const val PREF_OFF_DUTY_START_TIME = "PREF_OFF_DUTY_START_TIME"


    const val PREF_ON_DUTY_TIME_LEFT = "PREF_ON_DUTY_TIME_LEFT"
    const val PREF_TIME_DRIVE_LEFT = "PREF_TIME_DRIVE_LEFT"
    const val PREF_SLEEP_TIME_LEFT = "PREF_SLEEP_TIME_LEFT"
    const val PREF_CURRENT_EVENT_TYPE = "PREF_EVENT_TYPE"

    const val PREF_SLEEP_START_TIME_STAMP = "pref_sleep_start_time"
    const val PREF_DUTY_START_TIME_STAMP = "pref_duty_start_time"
    const val PREF_DRIVE_START_TIME_STAMP = "pref_drive_start_time_stamp"
    const val PREF_OFF_DUTY_START_TIME_STAMP = "pref_off_duty_start_time_stamp"
    const val PREF_70_HOUR_CLOCK_TIME_STAMP = "pref_70_hour_clock_time_stamp"


    const val PREF_PREVIOUS_EVENT = "pref_previous_event"
    const val PREF_CURRENT_EVENT = "pref_current_event"
    const val REMAINING_ON_DUTY_WHEN_SLEEP_START = "pref_remaining_on_duty_at_sleep"



    const val USER_ROLE = "DRIVER"

    const val START_DUTY_TITLE = "START_DUTY"
    const val OFF_DUTY_TITLE = "OFF_DUTY"
    const val DRIVE_TITLE = "DRIVE"
    const val SLEEP_TITLE = "SLEEP"
    const val SPLIT_SLEEP_TITLE = "SPLIT_SLEEP"
    const val PERSONAL_USE_TITLE = "PERSONAL_USE"
    const val YARD_MOVE_TITLE = "YARD_MOVE"

    const val VEHICLE = "VEHICLE"
    const val TRAILER = "TRAILER"
    const val CO_DRIVER = "DRIVER"
    const val DEFECTS = "DEFECT"

    const val START_DUTY = 20
    const val OFF_DUTY = 10
    const val DRIVE = 30
    const val SLEEP = 40

    const val START_DUTY_END = -20
    const val OFF_DUTY_END = -10
    const val DRIVE_END = -30
    const val SLEEP_END = -40

    const val PERSONAL_USE = 60
    const val YARD_MOVE = 50
    const val EVENT_TYPE = "EVENT_TYPE"
    const val TITLE = "TITLE"

    const val CUST_LOC = 1005
    const val CURR_LOC = 1006
    const val NOTES_ID = 1007
    const val ODOMETER = 1008

    const val ACCEPT = 10 //OPEN
    const val CLOSE = 20 //COMPLETED
    const val PENDING_LOAD = 1
    const val REJECT = -1 //REJECTED

    const val OPEN_PAYMENT = 0
    const val PAYMENT_HISTORY = 0

    const val PAID = 10
    const val UNPAID = 0

    const val ENROUTE = 10
    const val CHECK_IN = 20
    const val DOOR = 30
    const val CHECK_OUT = 40
    const val SHIPPER = "Shipper"

    const val DRIVE30_MIN_VIOLATION_HOURS = 8

    const val TOTAL_DUTY_HOURS_MILLI_SECONDS    = 14 * 60 * 60 *1000
    const val TOTAL_DRIVE_HOURS_MILL_SECONDS    = 11 * 60 * 60 * 1000
    const val TOTAL_SLEEP_HOURS_MILL_SECONDS    = 10 * 60 * 60 * 1000
    const val SPLIT_7_3_MIN_TIME                =  3 * 60 * 60 * 1000
    const val SPLIT_7_3_MAX_TIME                =  7 * 60 * 60 * 1000
    const val SPLIT_8_2_MIN_TIME                =  2 * 60 * 60 * 1000
    const val SPLIT_8_2_MAX_TIME                =  8 * 60 * 60 * 1000
    const val TOTAL_EIGHT_DAY_TO_MILLI_SECONDS  =  8 * 60 * 60 * 1000
    const val CLOCK_34_HOUR_TIME                = 34 * 60 * 60 * 1000
    const val TOTAL_70_HOURS_MILLI_SECONDS      = 70 * 60 * 60 * 1000 //70 Hours Clock Time


//    const val TOTAL_EIGHT_DAY_TO_MILLI_SECONDS    = 8 * 60 * 1000
//    const val TOTAL_DUTY_HOURS_MILLI_SECONDS    = 14 * 60 * 1000
//    const val TOTAL_DRIVE_HOURS_MILL_SECONDS    = 11 * 60 * 1000
//    const val TOTAL_SLEEP_HOURS_MILL_SECONDS    = 10 * 60 * 1000
//    const val SPLIT_7_3_MIN_TIME                = 3 * 60 * 1000
//    const val SPLIT_7_3_MAX_TIME                = 7 * 60 * 1000
//    const val SPLIT_8_2_MIN_TIME                = 2 * 60 * 1000
//    const val SPLIT_8_2_MAX_TIME                = 8 * 60 * 1000
//
//    const val CLOCK_34_HOUR_TIME              = 34 * 60 * 1000
//    const val TOTAL_70_HOURS_MILLI_SECONDS    = 70 * 60 * 1000 //70 Hours Clock Time


    const val TIME_FORMAT = "hh:mm aa"
    const val DATE_FORMAT = "dd:MM:yyyy"
    const val DATE_TIME_FORMAT = "dd:MM:yyyy hh:mm:ss"
    const val DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss"
    const val DATE_TIME_FORMAT_2 = "yyyy-MM-dd"
    const val DATE_TIME_FORMAT_3 = "yyyyMMddHHmmssSSS"

    const val DEALY = 500L

    const val NO_SPLIT = 1
    const val SPLIT_7_3 = 2
    const val SPLIT_8_2 = 3

    const val KEY_SPLIT_SLEEP = "KEY_SPLIT_SLEEP"



    const val KEY_CUR_LOC = 0
    const val KEY_CUSTOM_LOC = 1
    const val KEY_ODOMETER = 2
    const val KEY_NOTES = 3
    const val KEY_EVENT_TYPE = 4
    const val KEY_EVENT_ID = 5
    const val KEY_SLEEP_TYPE = 6
    const val EVENT_TYPE_DUTY = "DUTY"
    const val EVENT_TYPE_DOC_UPLOAD = "DOC_UPLOAD"
    const val KEY_EXCEPTION = "EXCEPTION"
    const val KEY_TIME_TRANS_MISSION = "TIME_TRANS_MISSION"
//    const val KEY_UNIDENTIFIED_EVENT = "UnIdentifiedEvent"

    const val EVENT_TYPE_ELD_UNIDENTIFIED = "UNIDENTIFIED"
    const val EVENT_TYPE_ELD = "ELD"
    const val EVENT_TYPE_VIOLATION = "VIOLATION"
    const val EVENT_TYPE_EXCEPTION = "EXCEPTION"


    const val SPLIT_SLEEP_VIOLATION = 110
    const val ON_DUTY_ND_VIOLATION = 120
    const val DRIVE_VIOLATION = 130
    const val SLEEP_VIOLATION = 140
    const val MIN30_BREAK_VIOLATION = 150
    const val HOUR70_CYCLE_VIOLATION = 160
    const val HOUR60_CYCLE_VIOLATION = 170
    const val HOUR340_RESET_VIOLATION = 180
    const val SHIPPING_DOCS_VIOLATION = 190


    const val KEY_EVENTTYPE = "eventType"
    const val KEY_EVENT_DATA = "eventData"

    const val API_KEY_DRIVER_LOGS = 10
    const val API_KEY_CERTIFICATION = 20
    const val API_KEY_DAILY_LOG_CHART = 30


    const val ERROR_CODE_NO_INTERNET = 999
    const val API_CODE = 101
    const val API_CODE_DEL = 102


    const val DUTY_STARTED = "DUTY_STARTED"
    const val DAY_EIGHT_STARTED = "DAY_EIGHT_STARTED"
    const val DAY_EIGHT_START_TIME_STAMP = "EIGHT_DAY_START_TIME_STAMP"


    const val REMAINING_SLEEP = "REMAINING_SLEEP"
    const val REMAINING_ON_DUTY = "REMAINING_ON_DUTY"
    const val REMAINING_DRIVE = "REMAINING_DRIVE"
    const val REMAINING_OFF_DUTY = "REMAINING_OFF_DUTY"
    const val REMAINING_70_HOUR = "REMAINING_70_HOUR"

    const val SPLIT_DURATION = "SPLIT_DURATION"
    const val CLOCK_70_COMPLETED = "CLOCK_70_COMPLETE"
    const val CLOCK_34_STARTED = "CLOCK_34_STARTED"
    const val CLOCK_34_START_TIME_STAMP = "CLOCK_34_START_TIME_STAMP"






}