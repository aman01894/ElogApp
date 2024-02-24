package com.example.elogapp.activity.auth


import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.ActivityReAuthPopupBinding
import com.example.elogapp.repository.responses.login.AuthResponse
import com.example.elogapp.repository.responses.user.UserResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


private var isPopUpActive = false
class ReAuthPopupActivity : AppCompatActivity(), AuthListener, KodeinAware {


    override val kodein by kodein()
    private val factory : ReAuthViewModelFactory by instance()
    private lateinit var binding : ActivityReAuthPopupBinding
    private lateinit var authViewModel: ReAuthViewModel
    private lateinit var dialog : MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = MaterialDialog(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_re_auth_popup)
        authViewModel = ViewModelProvider(this, factory)[ReAuthViewModel::class.java]

        binding.viewmodel = authViewModel
        authViewModel.authListener = this

        val sharedPreference =  getSharedPreferences(AppConstants.PREF_DB, Context.MODE_PRIVATE)
        val key = sharedPreference.getString(AppConstants.USER_KEY,"")
        val keyP = sharedPreference.getString(AppConstants.PASS_KEY,"")
        CoroutineScope(Dispatchers.Main).launch {
            key?.let {
                binding.username.setText(it)
            }
            keyP?.let {
                binding.password.setText(it)
            }
        }

        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()

        isPopUpActive = true
    }

    override fun onDestroy() {
        super.onDestroy()

        isPopUpActive = false
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStarted() {
        //toast("Login Started")
        binding.progressBar.show()
    }

    override suspend fun onSuccess(user: AuthResponse) {
        binding.progressBar.hide()

        if(user?.status.equals("success", true))   {

            withContext(Dispatchers.IO){
                authViewModel.saveUserDetails(user.data)
            }

                withContext(Dispatchers.IO){

                    val sharedPreference =  getSharedPreferences(
                        AppConstants.PREF_DB,
                        Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putString(AppConstants.AUTH_KEY,user.data.key)
                    editor.apply()

                }

            finish()

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

    companion object{

        fun isActiveScreen() : Boolean{
            return isPopUpActive
        }

    }

}