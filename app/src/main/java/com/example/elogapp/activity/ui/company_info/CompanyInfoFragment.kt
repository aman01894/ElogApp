package com.example.elogapp.activity.ui.company_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentCompanyInfoBinding
import com.example.elogapp.repository.responses.companyinfo.CompanyInfoResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class CompanyInfoFragment : Fragment(), KodeinAware, CompanyInfoListener {

    override val kodein by kodein()
    private lateinit var binding: FragmentCompanyInfoBinding
    private lateinit var companyInfoViewModel: CompanyViewModel
    private lateinit var dialog: MaterialDialog
    private lateinit var userPreference: UserPreference
    private var driverId: Int = 0

    private val factory: CompanyInfoViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCompanyInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        companyInfoViewModel = ViewModelProvider(this, factory)[CompanyViewModel::class.java]
        companyInfoViewModel.responseListener = this

        binding.companyInfoViewModel = companyInfoViewModel
        dialog = MaterialDialog(requireContext())
        userPreference = UserPreference(requireContext())



        companyInfoViewModel.backButtonLiveData.observe(viewLifecycleOwner) {

        }

        userPreference.clientId.asLiveData().observe(viewLifecycleOwner) {

            Log.d("===driverId=====>", "$it")

            if (it != null) {
                driverId = it
                companyInfoViewModel.getCompanyInfo(driverId!!)
            }
        }




        userPreference.driverName.asLiveData().observe(viewLifecycleOwner) {

            Log.d("===driverName=====>", "$it")

            if (it != null) {
                binding.driverName.text = it
            }
        }


        companyInfoViewModel.backButtonLiveData.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }


        return view
    }

    private fun updateUI(response: CompanyInfoResponse){
        val myUrl = response.data.logo
        Glide.with(requireActivity()).load(myUrl).into(binding.companyLogo);

        binding.driverCompanyName.text = response.data.clientName
        binding.driverEmail.text = response.data.emailID
        binding.timezone.text = response.data.timeZone
        binding.language.text = response.data.language
        binding.mainOfficeAddress.text = response.data.address
        binding.homeTerminalAddress.text = response.data.homeTerminal
        binding.driverPhone.text = response.data.contactNo
        binding.timezone.text = response.data.timeZone
    }


   

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(response: CompanyInfoResponse) {
        binding.progressBar.hide()
        updateUI(response)
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
                activity?.toast(message)
//                dialog.show {
//                    title(text = getString(R.string.dialog_title_alert))
//                    message(text = getString(R.string.server_error))
//                    positiveButton(text = getString(R.string.ok))
//                }
            }
        }

    }


}