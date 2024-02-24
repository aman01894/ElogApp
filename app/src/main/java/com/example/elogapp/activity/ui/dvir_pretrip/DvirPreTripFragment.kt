package com.example.elogapp.activity.ui.dvir_pretrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentPreTripBinding
import com.example.elogapp.repository.model.DvirPreTripData
import com.example.elogapp.activity.ui.dvir.NewDvir
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.repository.responses.predvir.PreDvirTripResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class DvirPreTripFragment : Fragment(), KodeinAware, DvirPreTripListener {

    override val kodein by kodein()
    var data : ArrayList<DvirPreTripData> = ArrayList();
    private lateinit var rAdapter: DVIR_ListAdapter
    lateinit var rView: RecyclerView

    private var dutyIdPref: String? = null
    private lateinit var userPreference: UserPreference
    private val factory: DvirPreTripViewModelFactory by instance()
    private lateinit var viewModel: DvirPreTripViewModel
    private lateinit var binding: FragmentPreTripBinding
    private lateinit var dialog: MaterialDialog

    private var driverId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPreTripBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory)[DvirPreTripViewModel::class.java]

        dialog = MaterialDialog(requireContext())
        userPreference = UserPreference(requireContext())
        binding.preDvirViewModel = viewModel
        viewModel.responseListener = this


        viewModel.backButtonLiveData.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        userPreference.clientId.asLiveData().observe(viewLifecycleOwner) {

            Log.d("===driverId=====>", "$it")

            if (it != null) {
                dutyIdPref = it.toString()
                viewModel.getPreDvirList(dutyIdPref!!)
            }

        }



        binding.newdvirBtn.setOnClickListener {

                val intent = Intent(activity, NewDvir::class.java)
                startActivity(intent)

        }

        return binding.root
    }


    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(response: PreDvirTripResponse) {

        binding.progressBar.hide()

        binding.rvPreTrip.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL, false)

        rAdapter = DVIR_ListAdapter(response.data);
        binding.rvPreTrip.adapter = rAdapter
        rAdapter.notifyDataSetChanged()


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