package com.example.elogapp.activity.ui.document

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.databinding.FragmentDocumentListBinding
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.DocumentLoadListener
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private var myData: Data? = null
class DocumentLoadFragment : Fragment(), KodeinAware, DocumentLoadListener {

    private lateinit var rAdapter: DocumentRecyclerViewAdapter
    private lateinit var documentLoadsListViewModel: DocumentLoadListViewModel
    private lateinit var binding: FragmentDocumentListBinding
    private lateinit var dialog : MaterialDialog
    private lateinit var dateStart: Calendar
    override val kodein by kodein()
    private val factory: DocumentLoadListViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_document_list, container, false);
        val view = binding.root
        documentLoadsListViewModel =
            ViewModelProvider(this, factory).get(DocumentLoadListViewModel::class.java)
        documentLoadsListViewModel.documentLoadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        binding.startDate.setOnClickListener {
            openStartDatePicker()
        }

        binding.endDate.setOnClickListener {
            openEndDatePicker(dateStart)

        }


        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_2, Locale.US)
        val today = Date()
        val c = Calendar.getInstance()
        c.time = today
        c.add(Calendar.DAY_OF_MONTH, -30)
        val currentDatePlusOne = c.time
        dateStart = Calendar.getInstance()

        binding.startDate.setText(sdf.format(today))
        binding.endDate.setText(sdf.format(currentDatePlusOne))

        callAiiForData();


        return view
    }

    private fun openStartDatePicker() {

        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_2, Locale.US)
        dateStart = Calendar.getInstance()
        val year = dateStart.get(Calendar.YEAR)
        val month = dateStart.get(Calendar.MONTH)
        val day = dateStart.get(Calendar.DAY_OF_MONTH)

        activity?.let {
            val dpd =  DatePickerDialog(it, { view, year, monthOfYear, dayOfMonth ->
                dateStart.set(Calendar.YEAR, year)
                dateStart.set(Calendar.MONTH, monthOfYear)
                dateStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.startDate.setText(sdf.format(dateStart.time))

            }, year, month, day)

            dpd.datePicker.maxDate = dateStart.timeInMillis;
            dpd.show()
        }
    }

    private fun openEndDatePicker(calendar: Calendar) {

        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_2, Locale.US)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        activity?.let {
            val dpd =  DatePickerDialog(it, { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.endDate.setText(sdf.format(calendar.time))
                callAiiForData();

            }, year, month, day)

//            dpd.datePicker.maxDate = calendar.timeInMillis
//            val left = 30*24*60*60*1000
//            dpd.datePicker.minDate = calendar.timeInMillis - left

            dpd.show()
        }
    }

    private fun callAiiForData() {
        documentLoadsListViewModel.getDocLoads(binding.endDate.text.toString(), binding.startDate.text.toString())
    }

    override fun onStarted() {
        binding.progressBar.show()
    }


    override fun onSuccess(loadDisRes: LoadDispatchResponse) {
        binding.progressBar.hide()

        val clone = ArrayList<Data>()
        for (list in loadDisRes.data){
            if (list.status == AppConstants.CLOSE){
                clone.add(list)
            }
        }

        if(clone.size > 0){

            rAdapter = activity?.let {
                DocumentRecyclerViewAdapter(
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
//                    activity?.onBackPressed()
                }
            }
        }


    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()
        //activity?.toast(message)

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
//                    message(text = message)
//                    positiveButton(text = getString(R.string.ok))
//                }
            }
        }
    }



    private fun listItemClicked(mData: Data) {
        //Toast.makeText(activity, "hi...", Toast.LENGTH_SHORT).show()

        val navController1 = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )
        navController1.navigate(R.id.fragment_document_list_gallery)
        myData = mData
    }


    companion object {

        fun myValue(): Data? {
            return myData
        }

    }
}