package com.example.elogapp.activity.ui.document

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.aminography.choosephotohelper.ChoosePhotoHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.elogapp.R
import com.example.elogapp.database.DocumentGallery
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.Outbound
import com.example.elogapp.database.UserDetails
import com.example.elogapp.databinding.FragmentDocumentListGalleryBinding
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.DocGalleryInfo
import com.example.elogapp.repository.responses.load.DocLoadListener
import com.example.elogapp.repository.responses.load.DocLoadsResponse
import com.example.elogapp.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File


private var alertDialogAndroid: AlertDialog? = null
class DocGalleryFragment : Fragment(), KodeinAware, DocLoadListener,DocGalleryAdapter.OnShareClickedListener {
    private lateinit var db: MyRoomDb
    private var m_Data: Data? = null
    override val kodein by kodein()

    var mDataServer: List<DocGalleryInfo> = ArrayList()
    var mDataLocal: List<DocGalleryInfo> = ArrayList()
    private lateinit var binding: FragmentDocumentListGalleryBinding
    private val factory: DocGalleryViewModelFactory by instance()
    private lateinit var viewModel: DocGalleryViewModel
    private lateinit var choosePhotoHelper: ChoosePhotoHelper
    private lateinit var dialog : MaterialDialog
    private lateinit var rmAdapter: DocGalleryAdapter
    private lateinit var userDetails: UserDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_document_list_gallery, container, false);
        val view = binding.root
        viewModel =
            ViewModelProvider(this, factory)[DocGalleryViewModel::class.java]
        viewModel.loadListener = this

        dialog = activity?.let { MaterialDialog(it) }!!

        binding.docGalleryRv.layoutManager = GridLayoutManager(context, 3)

        try {
            m_Data = DocumentLoadFragment.myValue()
            m_Data?.let { Log.d("=", it.name) }
        } catch (e: Exception) {
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choosePhotoHelper = ChoosePhotoHelper.with(this)
            .asFilePath()
            .withState(savedInstanceState)
            .build {
                if (it != null) {
                    showImageDialog(it, true)
                };
                //Toast.makeText(activity, it, Toast.LENGTH_LONG).show();
            }

        binding.btnAdd.setOnClickListener {
            choosePhotoHelper.showChooser()
        }

        binding.finish.setOnClickListener {
            activity?.onBackPressed()
        }

        initLiveData()

    }

    private fun initLiveData() {


        viewModel.getUserDetail().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                userDetails = user
            }
        }


//        m_Data?.id?.let{
//            viewModel.getLoadDocs(it).observe(this, { docList ->
//
//                run {
//
//                    docList?.let {
//                        Toast.makeText(activity, "DOC Found...", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
//        }

        m_Data?.id?.let{
                viewModel.getLoadDocs(it, AppConstants.API_CODE)
        }


    }



    private fun showImageDialog(pathUrl: String, showUpload: Boolean) {

        val layoutInflaterAndroid = LayoutInflater.from(activity)
        val mView: View =
            layoutInflaterAndroid.inflate(R.layout.dialog_document_gallery_image, null)

        val alertDialogBuilderUserInput =
            activity?.let { AlertDialog.Builder(it, R.style.mAlertDialog) }
        alertDialogBuilderUserInput?.setView(mView)

//        alertDialogBuilderUserInput.setOnItemSelectedListener()


        val btnUpload = mView.findViewById(R.id.btnUpload) as Button
        val spinnerLayout = mView.findViewById(R.id.spinnerLayout) as RelativeLayout
        val imgImageView = mView.findViewById(R.id.imgImageView) as ImageView
        val spinner = mView.findViewById(R.id.spinnerDocType) as AppCompatSpinner
        val text: String = spinner.selectedItem.toString()
         //spinner.setSelection(resources.getStringArray(R.array.doc_type).indexOf(text))
        val index = resources.getStringArray(R.array.doc_type).indexOf(text)

        if (alertDialogBuilderUserInput != null) {
            alertDialogAndroid = alertDialogBuilderUserInput.create()
        }

        if(showUpload){
            btnUpload.visibility = View.VISIBLE
            spinnerLayout.visibility = View.VISIBLE
        }else{
            btnUpload.visibility = View.GONE
            spinnerLayout.visibility = View.GONE
        }

        btnUpload.setOnClickListener {
            alertDialogAndroid!!.dismiss()
            viewModel.viewModelScope.async (Dispatchers.IO){

                DocumentGallery("" ,"", pathUrl, "", "",false).apply {
                    var rowId: String = userDetails.id.toString() + "_" + AppUtils.generateRowId()
                    id = rowId
                    displayName= userDetails.id.toString() + "_IMG"
                    saveDate = AppUtils.getServerDateTime()
                    isSync = false
                    loadId = m_Data?.id.toString()
                    imgPath = pathUrl
                }.also {
                    viewModel.insertImageInGallery(it)
                    saveImageDataInOutbound(it, index)

                    m_Data?.id?.let{
                        viewModel.getLoadDocs(it, AppConstants.API_CODE)
                    }
                }
            }

        }

        Glide.with(this)
            .load(pathUrl).centerCrop()
            .apply(RequestOptions.placeholderOf(R.drawable.ic_add_24))
            .into(imgImageView)

        alertDialogAndroid!!.show();


    }





    private fun listItemClicked(mData: DocGalleryInfo) {
//        Toast.makeText(activity, "hi...", Toast.LENGTH_SHORT).show()
        showImageDialog(mData?.imgPath.toString(), false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        choosePhotoHelper.onSaveInstanceState(outState)
    }



    /**
     * Send Duty Information @ Server via HTTP
     */
    private fun saveImageDataInOutbound(docData : DocumentGallery, itemCode: Int) {

        try {

            val bitmap: Bitmap = Glide
                .with(requireContext())
                .asBitmap()
                .load(docData.imgPath).override(640 ,800)
                .fitCenter()
                .submit()
                .get()

            val img = ImageUtil.convertBitmapToBase64(bitmap)

            val eventObj = JSONObject()
            eventObj.put("imgData", img)
            eventObj.put("userId", userDetails.id)
            eventObj.put("loadId", docData.loadId)
            eventObj.put("eventTime", AppUtils.getServerDateTime())
            eventObj.put("imgDateTime", docData.saveDate)
            eventObj.put("docId", docData.id)
            eventObj.put("documentType", itemCode)


            val mainObj = JSONObject()
            mainObj.put(AppConstants.KEY_EVENTTYPE, AppConstants.EVENT_TYPE_DOC_UPLOAD)
            mainObj.put(AppConstants.KEY_EVENT_DATA, eventObj)


            Outbound().apply {

                eventType = AppConstants.EVENT_TYPE_DOC_UPLOAD
                data = mainObj.toString()
                synced = false

            }.also {

                viewModel.insertOutboundData(it)
                AppUtils.logger("DATA Inserted in Outbound ${eventObj.toString()}")

                sendInstantDataToServer()
            }

        } catch (e: Exception) {
            AppUtils.logger("Exception in save event in outbound${e.message}")
        }

    }



    private fun sendInstantDataToServer() {

        activity?.let { mContext ->

            if (AppUtils.isNetworkAvailable(mContext)) {

                CoroutineScope(Dispatchers.IO).async {

                    try {
                        val mData = viewModel.getOutboundList(false)
                        AppUtils.logger("One Time Work Manager Start")
                        if (mData?.size!! > 0) {
                            for (event in mData) {

                                try {

                                    val requestBody =
                                        event.data?.toRequestBody()
                                    AppUtils.loggerRequest("$event.data")

                                    if (!event.synced) {
                                        val br = viewModel.sendOutBondDataToServer(requestBody!!)

                                        val status = br.status
                                        AppUtils.loggerResponse("$br")

                                        status?.let {
                                            if (status.equals("success", true))
                                                MyRoomDb.instance?.getOutboundDao()
                                                    ?.update(event.ID, true)


                                            AppUtils.deleteDataFromOutBound(event)

                                            m_Data?.id?.let{
                                                viewModel.getLoadDocs(it, AppConstants.API_CODE)
                                            }

                                        }
                                    }
                                } catch (err: Exception) {
                                    err.message?.let { AppUtils.sentryLoggerError(it) }
                                }
                            }
                        } else {

                            AppUtils.logger("No Outbound data to sync")
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        e.message?.let { AppUtils.sentryLoggerError(it) }
                    }

                }

            } else {
                AppUtils.logger("Network Not Available")
            }

        }
    }

    override fun onStarted() {
        binding.progressBar.show()
    }


    override fun onSuccess(response: DocLoadsResponse, apiCode: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar.hide()

            when (apiCode) {

                AppConstants.API_CODE -> {

                    mDataServer = response.data
                    getDataFromLocalDB()
                }
                AppConstants.API_CODE_DEL -> {

                    try {
                        mDataServer = response.data

                        getDataFromLocalDB()
                        Toast.makeText(activity, response?.message, Toast.LENGTH_LONG)

                        //rmAdapter.updateReceiptsList()
                        AppUtils.logger("Image Deleted..")
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    private fun getDataFromLocalDB(){

        viewModel.getGalleryDataFromDb().observe(this, { it ->

            var finalList: List<DocGalleryInfo>
            finalList = mDataServer

            if(it!= null) {

                if (it.isNotEmpty()) {
                    mDataLocal = it
                    finalList = finalList + mDataLocal
                }

                rmAdapter = activity?.let {
                    DocGalleryAdapter(
                        it, finalList
                    ) { mData -> listItemClicked(mData) }
                }!!

                binding.docGalleryRv.adapter = rmAdapter
                rmAdapter.deleteBtnListener(this)
                rmAdapter.notifyDataSetChanged()

            }
        })

    }

    override fun onFailure(message: String, apiCode: Int) {
        binding.progressBar.hide()
    }

    override fun deleteBtnClicked(itemId: String, path: String) {

        Toast.makeText(activity, "Deleted Pressed...", Toast.LENGTH_SHORT).show()

        dialog.show {

            title(text = getString(R.string.dialog_title_alert))
            message(text = getString(R.string.are_you_sure))
            positiveButton(text = getString(R.string.yes)){

                dialog.cancel()
                //getDataFromLocalDB()

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteGalleryDataFromDb(itemId)
                }

                try {
                    val obj = JSONObject()
                    obj.put("loadId", m_Data?.id?.toInt())
                    obj.put("documentId", itemId)
                    val requestBody = obj?.toString()!!.toRequestBody()
                    AppUtils.loggerRequest(obj!!.toString())

                    viewModel.deleteImageFromServer(requestBody, AppConstants.API_CODE_DEL)

                }catch (e: Exception){
                    e.printStackTrace()
                }

                try{
                    val fDelete = File(path)
                    if (fDelete.exists()) {
                        if (fDelete.delete()) {
                            AppUtils.logger("File Deleted :$fDelete")
                        } else {
                            AppUtils.logger("File not Deleted :$fDelete")
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            negativeButton(text = getString(R.string.no)){
            }
        }

    }

}