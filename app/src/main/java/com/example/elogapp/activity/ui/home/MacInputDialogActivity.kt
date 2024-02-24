package com.example.elogapp.activity.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devicemanager.TrackerViewFragment
import com.example.elogapp.R
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.PreferenceHelper

class MacInputDialogActivity : AppCompatActivity() {

    lateinit var macIdEt : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mac_input_dialog)


        var closeBtn = findViewById<ImageView>(R.id.close_btn);
        var submitBtn = findViewById<Button>(R.id.submit_btn);
        macIdEt = findViewById(R.id.et_mac_address);

        var macIdPref = PreferenceHelper().getString(this,AppConstants.PREF_DEVICE_ID_MAC)
        if(macIdPref?.isEmpty() == false) {
            macIdEt.setText(macIdPref)
        }



        closeBtn.setOnClickListener {
            finish()
        }

        submitBtn.setOnClickListener {

            if (checkValidation()) {

                val intent = Intent().apply {
                    putExtra("mac_address", macIdEt.text.toString().trim())
                    PreferenceHelper().save(applicationContext,AppConstants.PREF_DEVICE_ID_MAC, macIdEt.text.toString())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }




    override fun onResume() {
        super.onResume()
        Log.v(TrackerViewFragment.TAG, "TVF: onResume:$this")

    }

    override fun onPause() {
        super.onPause()
        Log.v(TrackerViewFragment.TAG, "TVF: onPause:$this")
    }


    private fun checkValidation() : Boolean{

        when{
            macIdEt.text.isEmpty()->{
                Toast.makeText(applicationContext, "Enter MAC ID", Toast.LENGTH_LONG).show()
                return false
            }
        }

        return true
    }



}