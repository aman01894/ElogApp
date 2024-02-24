package com.example.elogapp.activity.auth

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.devicemanager.AppModel
import com.example.devicemanager.usb.DevicesFragment
import com.example.elogapp.R
import com.example.elogapp.activity.MainActivity
import com.example.elogapp.databinding.ActivityLoginBinding
import com.example.elogapp.repository.responses.login.AuthResponse
import com.example.elogapp.repository.responses.user.UserResponse
import com.example.elogapp.util.*
import com.example.elogapp.util.pref.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware {


override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()
    private lateinit var userPreferences: UserPreference
    private lateinit var binding : ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var dialog : MaterialDialog
    private var mPrivacyDlg: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = MaterialDialog(this)
        userPreferences = UserPreference(this)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        binding.viewmodel = authViewModel
        authViewModel.authListener = this


        val key = PreferenceHelper().getString(this,AppConstants.USER_KEY)
        val keyP = PreferenceHelper().getString(this, AppConstants.PASS_KEY)
        CoroutineScope(Dispatchers.Main).launch {
            key?.let {
                binding.username.setText(it)
            }
            keyP?.let {
                binding.password.setText(it)
            }
        }

//        authViewModel.getLoggedInUser().observe(this, { user ->
//            if(user!= null ){
//                Intent(this,MainActivity::class.java)
//                    .also {
////                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(it)
//                    }
//                }
//        })

        //viewModel.getUserDetail()

        supportActionBar?.hide()


        //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        AppModel.getInstance().privacyAccepted =   PreferenceHelper().getBoolean(this,"privacy_accepted", false)
        if (!AppModel.getInstance().privacyAccepted) {
            getPrivacyConsent(this)
        }
        // During the initial run, this would be treated as false
        AppModel.MODE_USB = PreferenceHelper().getBoolean(this,"usb_mode", false)

    }




    override fun onStarted() {
        //toast("Login Started")
        binding.progressBar.show()
    }

    override suspend fun onSuccess(user: AuthResponse) {
        binding.progressBar.hide()

        if(user.status.equals("success", true))   {

            withContext(Dispatchers.IO){
                user?.let {
                    authViewModel.saveUserDetails(user.data)

                    PreferenceHelper().save(applicationContext, AppConstants.PREF_FEATURE_ELD, user?.data!!.featureEld)
                    PreferenceHelper().save(applicationContext,AppConstants.PREF_FEATURE_DISPATCH, user?.data!!.featureDispatch)
                    PreferenceHelper().save(applicationContext,AppConstants.PREF_IS_LOGGED_IN,true)
                }
            }

            withContext(Dispatchers.IO){

                PreferenceHelper().save(applicationContext, AppConstants.AUTH_KEY,user.data.key!!)
                PreferenceHelper().save(applicationContext, AppConstants.TIME_ZONE_KEY, user.data.timeZoneInfo!!)

                if(binding.chkRemember.isChecked) {
                    PreferenceHelper().save(applicationContext, AppConstants.USER_KEY, binding.username.text.toString().trim())
                    PreferenceHelper().save(applicationContext, AppConstants.PASS_KEY, binding.password.text.toString().trim())
                }else{
                    PreferenceHelper().save(applicationContext, AppConstants.USER_KEY, "")
                    PreferenceHelper().save(applicationContext, AppConstants.PASS_KEY, "")
                }



            }

            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }

        }else{
            dialog.show {
                title(text = getString(R.string.dialog_title_alert))
                message(text = getString(R.string.server_error))
                positiveButton(text = getString(R.string.ok))
            }
        }
    }


    override fun onSuccess(user: UserResponse) {
       // toast("is logged In")
        binding.progressBar.hide()

    }

    override fun onSuccess() {
        //toast("is logged In")
        binding.progressBar.hide()
    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()

        message?.let{
            if(it.contains("${AppConstants.ERROR_CODE_NO_INTERNET}")){
                dialog.show {
                    title(text = getString(R.string.dialog_title_alert))
                    message(text = getString(R.string.internet_error))
                    positiveButton(text = getString(R.string.ok))
                }
            }else{
                toast(message)
//                dialog.show {
//                    title(text = getString(R.string.dialog_title_alert))
//                    message(text = message)
//                    positiveButton(text = getString(R.string.ok))
//                }
            }
        }

    }


    private fun getPrivacyConsent(activity: Activity?) {
        mPrivacyDlg = Dialog(this)
        mPrivacyDlg!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mPrivacyDlg!!.setContentView(R.layout.privacy_notice)
        mPrivacyDlg!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPrivacyDlg!!.setCanceledOnTouchOutside(false)
        mPrivacyDlg!!.setOwnerActivity(this)
        val link = mPrivacyDlg!!.findViewById<View>(R.id.privacy_link) as TextView
        link.movementMethod = LinkMovementMethod.getInstance()
        val modeUsb: SwitchCompat = mPrivacyDlg!!.findViewById(R.id.switchUsb)
        val accept = mPrivacyDlg!!.findViewById<Button>(R.id.privacy_accept)
        accept.setOnClickListener {
            PreferenceHelper().save(applicationContext, "privacy_accepted", true)
            if (modeUsb.isChecked) {
                PreferenceHelper().save(applicationContext, "usb_mode", true)

                // Set the USB mode
                AppModel.MODE_USB = true

                // Replace default fragment with the USB
                val fm = supportFragmentManager
                val fragment = fm.findFragmentById(R.id.fragment_container)
                if (fragment != null) {
                    val ft = fm.beginTransaction()
                    ft.replace(
                        R.id.fragment_container,
                        DevicesFragment.newInstance(),
                        DevicesFragment.FRAG_TAG
                    )
                    ft.commit()
                }
                //TODO Vaibhav Uncomment below line
                //this@LoginActivity.mConnectButton.visibility = View.INVISIBLE
            } else {
                PreferenceHelper().save(applicationContext, "usb_mode", false)
                AppModel.MODE_USB = false
            }

            AppModel.getInstance().privacyAccepted = true
            mPrivacyDlg!!.dismiss()
        }
        mPrivacyDlg!!.setOnCancelListener {
            mPrivacyDlg!!.dismiss()
            finish()
        }
        mPrivacyDlg!!.show()
    }
    

}