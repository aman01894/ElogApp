package com.example.elogapp.activity.ui.loads.payment_history

import android.app.DatePickerDialog
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
import com.example.elogapp.databinding.FragmentPaymentHistoryBinding
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import com.example.elogapp.repository.responses.load.LoadListener
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.hide
import com.example.elogapp.util.show
import com.example.elogapp.util.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


private var myData: Data? = null
private var fromOpenPayment : Boolean = false

class PaymentHistoryFragment : Fragment(), KodeinAware, LoadListener {

    override val kodein by kodein()
    private val factory: PaymentHistoryViewModelFactory by instance()
    private lateinit var rAdapter: PaymentHistoryAdapter
    private lateinit var openPaymentViewModel: PaymentHistoryViewModel
    private lateinit var binding: FragmentPaymentHistoryBinding
    private lateinit var dateStart: Calendar
    private lateinit var dialog: MaterialDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment_history, container, false);
        val view = binding.root
        openPaymentViewModel = ViewModelProvider(this, factory)[PaymentHistoryViewModel::class.java]
        openPaymentViewModel.loadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        val sdf = SimpleDateFormat(AppConstants.DATE_TIME_FORMAT_2, Locale.US)
        val today = Date()
        //System.out.println(sdf.format(today))

        val c = Calendar.getInstance()
        c.time = today
        c.add(Calendar.DAY_OF_MONTH, -30)
        val currentDatePlusOne = c.time
//        System.out.println(sdf.format(currentDatePlusOne))

        dateStart = Calendar.getInstance()
//        val year = dateStart.get(Calendar.YEAR)
//        val month = dateStart.get(Calendar.MONTH)
//        val day = dateStart.get(Calendar.DAY_OF_MONTH)
//        dateStart.set(Calendar.YEAR, year)
//        dateStart.set(Calendar.MONTH, monthOfYear)
//        dateStart.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        binding.startDate.setText(sdf.format(today))
        binding.endDate.setText(sdf.format(currentDatePlusOne))

        binding.startDate.setOnClickListener {
            openStartDatePicker()
        }

        binding.endDate.setOnClickListener {
            openEndDatePicker(dateStart)

        }

        binding.submitBtn.setOnClickListener{

            callAiiForData(binding.endDate.text.toString(),binding.startDate.text.toString());
        }


        callAiiForData(binding.endDate.text.toString(),binding.startDate.text.toString());

        return view
    }

    private fun callAiiForData(fromDate: String, toDate: String) {
        openPaymentViewModel.getLoadData(fromDate, toDate)
    }

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(loadDisRes: LoadDispatchResponse) {
        binding.progressBar.hide()

        val clone = ArrayList<Data>()

        for (list in loadDisRes.data) {

                if (list.status == AppConstants.CLOSE &&
                    list.paymentStatus == AppConstants.PAYMENT_HISTORY){

                    clone.add(list)
                    //binding.relLayout.visibility = View.GONE
                }

        }

        if (clone.size > 0) {
            rAdapter = activity?.let {
                PaymentHistoryAdapter(
                    it.applicationContext,
                    fromOpenPayment,
                    clone
                ) { mData -> listItemClicked(mData) }
            }!!

            binding.pendingRecycle.adapter = rAdapter
            rAdapter.notifyDataSetChanged()

        } else {

            dialog.show {
                title(text = getString(R.string.dialog_title_Msg))
                message(text = getString(R.string.no_Payment_history))
                positiveButton(text = getString(R.string.ok)) {
                    activity?.onBackPressed()
                }
            }
        }


    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()
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

            }, year, month, day)

            dpd.show()
        }
    }


    private fun listItemClicked(mData: Data) {
//        Toast.makeText(activity, "Clicked: ${mData.name}", Toast.LENGTH_SHORT).show()


        val navController1 = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )
        //navController1.navigate(R.id.fragment_pending_load_accept_reject)
        myData = mData

    }

    companion object {

        fun myValue(): Data? {
            return myData
        }

        fun fromOpenPaymentScreen() : Boolean{

            return fromOpenPayment
        }

    }


}