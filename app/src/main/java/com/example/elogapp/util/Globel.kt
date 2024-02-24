package com.example.elogapp.util

class Global {


    companion object{

        var timeZone =  ""
        fun setMyTimeZone(timeZone: String){
            this.timeZone = timeZone
        }
        fun getMyTimeZone(): String{
            return timeZone
        }

    }
}