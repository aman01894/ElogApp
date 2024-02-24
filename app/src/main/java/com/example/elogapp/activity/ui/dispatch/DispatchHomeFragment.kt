package com.example.elogapp.activity.ui.dispatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.elogapp.R
import com.example.elogapp.activity.MainActivity
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentDispatchPayrollBinding
import com.example.elogapp.util.pref.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DispatchHomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var dispatchHomeViewModel : DispatchHomeViewModel
//    private lateinit var dispatchHomeViewModelFactory: DispatchHomeViewModelFactory by instance()
    private val factory: DispatchHomeViewModelFactory by instance()
    private lateinit var binding: FragmentDispatchPayrollBinding
    private lateinit var userPreference: UserPreference
    private lateinit var userDetails: UserDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDispatchPayrollBinding.inflate(inflater, container, false)
        dispatchHomeViewModel = ViewModelProvider(this, factory)[DispatchHomeViewModel::class.java]

        userPreference = UserPreference(requireContext())

        getUserDetails()

        binding.pendingBtn.setOnClickListener {
            findNavController().navigate(R.id.fragment_pending_list)
        }
        binding.openRel.setOnClickListener {
            findNavController().navigate(R.id.fragment_open_load_list)
        }
        binding.closeRel.setOnClickListener {
            findNavController().navigate(R.id.fragment_close_list)
        }
        binding.relDocument.setOnClickListener {
            findNavController().navigate(R.id.fragment_document_list)
        }
        binding.relPayment.setOnClickListener {
            val bundle: Bundle = bundleOf("From" to "HistoryPayment")
            findNavController().navigate(R.id.fragment_payment_list, bundle)
        }
        binding.relOpenPayment.setOnClickListener {
            val bundle: Bundle = bundleOf("From" to "OpenPayment")
            findNavController().navigate(R.id.fragment_open_payment_list, bundle)
        }
        binding.butExit.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun getUserDetails(){

        CoroutineScope(Dispatchers.Main).launch {

            dispatchHomeViewModel.getUserDetail().observe(viewLifecycleOwner, Observer {

                it?.let {

                    userDetails = it
                    val driverName = userDetails.displayName
                    if (driverName != null) {
                        (activity as MainActivity).setActionBarTitle(driverName)
                    }

                    CoroutineScope(Dispatchers.Main).launch {

                        userPreference.saveDriverId(it.id)
                        userPreference.saveClientId(it.clientId)
                        userDetails.displayName?.let { name ->
                            userPreference.saveCoDriverName(name)
                        }
                    }
                }

            })
        }
    }

}