package com.example.elogapp.activity.ui.loads.pending_load

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentPendingLoadListBinding
import com.example.elogapp.repository.model.ShipperConsigneeList
import com.example.elogapp.repository.responses.load.*
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

private lateinit var rAdapter: PendingLoadItemAdapter
private lateinit var pendingLoadsItemViewModel: PendingLoadItemViewModel
private lateinit var binding: FragmentPendingLoadListBinding

@Suppress("NAME_SHADOWING")
class PendingLoadItemFragment : Fragment(), KodeinAware, LoadAcceptRejectListener {

    override val kodein by kodein()
    private val factory: PendingLoadItemViewModelFactory by instance()
    private var mData: Data? = null
    private lateinit var dialog : MaterialDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pending_load_list,
                container,
                false
            );
        val view = binding.root
        pendingLoadsItemViewModel = ViewModelProvider(this, factory).get(PendingLoadItemViewModel::class.java)
        pendingLoadsItemViewModel.loadListener = this
        binding.fragment = this

        dialog = activity?.let { MaterialDialog(it) }!!

        try{
            mData = PendingLoadsListFragment.myValue()
            mData?.let { Log.d("=", it.name) }

            init()

        }catch (e: Exception){

        }

        return view
    }

    private fun init() {

        val list = ArrayList<ShipperConsigneeList>()

        for (i in mData?.shippers!!) {
            val obj = ShipperConsigneeList()
            obj.type = "Shipper"
            obj.shipper = i
            list.add(obj)
        }

        for (i in mData?.consignees!!) {
            val obj = ShipperConsigneeList()
            obj.type = "consignee"
            obj.consignee = i
            list.add(obj)
        }

        rAdapter = PendingLoadItemAdapter(list)
        binding.pendingRecycle.adapter = rAdapter
        rAdapter.notifyDataSetChanged()


        try {
            binding.distance.text = mData!!.totalMiles
            binding.load.text = mData!!.loadNo
        }catch (e : Exception){
            e.printStackTrace()
        }

    }


    override fun onStarted() {
        binding.progressBar.show()
    }



    override fun onSuccess(response: LoadAcceptRejectResponse, responseCode: Int) {
        binding.progressBar.visibility = View.GONE

        if(response.status.equals("success", true))   {


            //init()
            Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()

            if(responseCode == AppConstants.ACCEPT) {
                activity?.onBackPressed();
                findNavController(this).navigate(R.id.fragment_open_load_list)
            }

        }else{
            dialog.show {
                title(text = getString(R.string.dialog_title_alert))
                message(text = response.message)
                positiveButton(text = getString(R.string.ok))
            }
        }
    }

    override fun onFailure(message: String) {
        binding.progressBar.visibility = View.GONE
//        activity?.toast(message)
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
        Toast.makeText(activity, "Clicked: ${mData.name}", Toast.LENGTH_SHORT).show()
    }


     fun onAcceptButtonClick(view: View){

         dialog.show {
             title(text = getString(R.string.dialog_title_alert))
             message(text = "Are you sure want to Accept Load")
             negativeButton (text = getString(R.string.no))
             positiveButton(text = getString(R.string.yes)){
                 mData?.let {
                     pendingLoadsItemViewModel.rejectAcceptLoad(it, AppConstants.ACCEPT, binding.comment.text.toString())
                 }
             }
         }

    }

     fun onRejectButtonClick(view: View){

         dialog.show {
             title(text = getString(R.string.dialog_title_alert))
             message(text = "Are you sure want to Reject Load")
             negativeButton (text = getString(R.string.no))
             positiveButton(text = getString(R.string.yes)){
                 mData?.let {
                     pendingLoadsItemViewModel.rejectAcceptLoad(it, AppConstants.REJECT, binding.comment.text.toString())
                 }
             }
         }
    }

}