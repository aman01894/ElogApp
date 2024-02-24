package com.example.elogapp.activity.ui.shippingdocs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.elogapp.R
import com.example.elogapp.database.ShippingDocs
import com.example.elogapp.databinding.FragmentShippingDocsBinding
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocResponse
import com.example.elogapp.util.*
import com.example.elogapp.util.apilistner.ApiResponseListener
import com.example.elogapp.util.pref.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ShippingDocsFragment : Fragment(), KodeinAware, ApiResponseListener {

    override val kodein by kodein()
    private var docListServer: List<ShippingDocs> = ArrayList();
    private var docListDb: List<ShippingDocs> = ArrayList()
    private lateinit var rAdapter: ShippingDocsAdapter

    private lateinit var userPreference: UserPreference
    private val factory: ShippingDocsViewModelFactory by instance()
    private lateinit var viewModel: ShippingDocsViewModel
    private lateinit var binding: FragmentShippingDocsBinding
    private lateinit var dialog: MaterialDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShippingDocsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory)[ShippingDocsViewModel::class.java]

        dialog = MaterialDialog(requireContext())
        userPreference = UserPreference(requireContext())
        binding.shippingDocsViewModel = viewModel
        viewModel.responseListener = this

        binding.shippingDocsRV.layoutManager = LinearLayoutManager(
            context, RecyclerView.VERTICAL, false)
        rAdapter = ShippingDocsAdapter(docListDb)
        binding.shippingDocsRV.adapter = rAdapter

        val featureEld = PreferenceHelper().getBoolean(requireContext(),AppConstants.PREF_FEATURE_ELD, false)
        val featureDispatch = PreferenceHelper().getBoolean(requireContext(), AppConstants.PREF_FEATURE_DISPATCH, false)

        initObservers()

        binding.backBtn.isVisible = false
        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        if(featureDispatch){
            binding.docNoEditTxt.isVisible = false
            binding.submitBtn.isVisible = false
        }


        binding.submitBtn.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                ShippingDocs().apply {

                    documentNumber = binding.docNoEditTxt.text.toString()
                    saveDate = AppUtils.getServerDateTime()
                    isSync = false

                }.also {
                    viewModel.insertShippingDocInDb(it)
                }

            }
        }

        return binding.root
    }


    private fun initObservers() {

        viewModel.getUserDetails().observe(viewLifecycleOwner, Observer { userInfo ->

                run {
                    userInfo?.let {

                        if(userInfo.featureEld || userInfo.featureDispatch){
                            viewModel.getShippingDocListFromServer()
                        }

                        if(userInfo.featureDispatch){
                            viewModel.getShippingDocsListFromDb()
                            binding.submitBtn.isEnabled = true
                            binding.docNoEditTxt.isFocusableInTouchMode = true
                        }else{
                            binding.submitBtn.isEnabled = false
                            binding.docNoEditTxt.isFocusableInTouchMode = false
                        }

                    }
                }
        })

        viewModel.getShippingDocsListFromDb().observe(viewLifecycleOwner) { list ->
            run {
                list?.let {
                    binding.docNoEditTxt.setText("")
                    docListDb = it

                    val finalList = docListServer + docListDb
                    rAdapter.updateDocList(finalList)
                }
            }

        }

    }

    override fun onStarted() {
        binding.progressBar.show()
    }

    override fun onSuccess(response: Any) {
        binding.progressBar.hide()

        val obj = response as? ShippingDocResponse
        docListServer = obj?.data!!
        val finalList = docListServer + docListDb
        rAdapter.updateDocList(finalList)

    }

    override fun onFailure(message: String) {
        binding.progressBar.hide()

        message?.let {
            if (it.contains("${AppConstants.ERROR_CODE_NO_INTERNET}")) {
                dialog.show {
                    title(text = getString(R.string.dialog_title_alert))
                    message(text = getString(R.string.internet_error))
                    positiveButton(text = getString(R.string.ok))
                }
            } else {

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