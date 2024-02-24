package com.example.elogapp.activity.ui.logout

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentLogoutBinding
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.PreferenceHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class LogoutFragment : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: LogoutViewModelFactory by instance()
    private lateinit var binding: FragmentLogoutBinding
    private lateinit var logoutViewModel: LogoutViewModel
    private lateinit var userDetails: UserDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val view = binding.root
        logoutViewModel = ViewModelProvider(this, factory).get(LogoutViewModel::class.java)



        logoutViewModel.getUserDetail().observe(this, Observer{ user ->
            if(user!= null ){

                userDetails = user
            }
        })

        binding.logoutBtn.setOnClickListener {

            userDetails?.let {

                logoutViewModel.logoutUser(userDetails.id)
                PreferenceHelper().save(requireContext(), AppConstants.PREF_IS_LOGGED_IN,false)
                activity?.finish()

            }



        }
        return view
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

    fun logoutClick(view: View) {
        logoutViewModel.logoutUser(1)

    }

}