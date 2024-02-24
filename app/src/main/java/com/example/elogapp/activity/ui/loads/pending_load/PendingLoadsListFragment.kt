package com.example.elogapp.activity.ui.loads.pending_load

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentPendingListBinding
import com.example.elogapp.repository.responses.load.LoadListener
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance



private var myData: Data? = null
class PendingLoadsListFragment : Fragment(), KodeinAware, LoadListener {

    override val kodein by kodein()
    private val factory: PendingLoadsListViewModelFactory by instance()
    private lateinit var rAdapter: PendingLoadsListAdapter
    private lateinit var pendingLoadsListViewModel: PendingLoadsListViewModel
    private lateinit var binding: FragmentPendingListBinding

    private lateinit var dialog : MaterialDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pending_list, container, false);
        val view = binding.root
        pendingLoadsListViewModel = ViewModelProvider(this, factory).get(PendingLoadsListViewModel::class.java)
        pendingLoadsListViewModel.loadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        callAiiForData(AppConstants.PENDING_LOAD);
        return view
    }

    private fun callAiiForData(status: Int) {
        pendingLoadsListViewModel.getLoadData(status)
    }

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(loadDisRes: LoadDispatchResponse) {
        binding.progressBar.hide()

        val clone = ArrayList<Data>()

        for (list in loadDisRes.data){
            if (list.status == AppConstants.PENDING_LOAD){ //Pending Load
                clone.add(list)
            }
        }

        if(clone.size > 0){
            rAdapter = activity?.let {
                PendingLoadsListAdapter(
                    it.applicationContext, clone
                ) { mData -> listItemClicked(mData) }
            }!!

            binding.pendingRecycle.adapter = rAdapter
            rAdapter.notifyDataSetChanged()

        }else{

            dialog.show {
                title(text = getString(R.string.dialog_title_Msg))
                message(text = getString(R.string.no_pending_load))
                positiveButton(text = getString(R.string.ok)){
                    activity?.onBackPressed()
                }
            }
        }


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


    private fun listItemClicked(mData: Data) {
//        Toast.makeText(activity, "Clicked: ${mData.name}", Toast.LENGTH_SHORT).show()


        val navController1 = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )
        navController1.navigate(R.id.fragment_pending_load_accept_reject)
        myData = mData

    }

    companion object {

        fun myValue(): Data? {
            return myData
        }

    }



}