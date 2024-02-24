package com.example.elogapp.activity.ui.paymenthistory
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.elogapp.R
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//var listdata1: ArrayList<PaymentHistoryListData> = ArrayList();
//private lateinit var rAdapter: PaymentHistoryRecycleAdapter
//lateinit var ryView1: RecyclerView
///**
// * A simple [Fragment] subclass.
// * Use the [ClosedLoadFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class PaymentHistoryFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        val view = inflater.inflate(R.layout.fragment_payment_history, container, false)
//
//        ryView1 = view.findViewById(R.id.payment_recycle)
//        rAdapter = PaymentHistoryRecycleAdapter(listdata1);
//        ryView1.adapter = rAdapter
//        rAdapter.notifyDataSetChanged()
//
//        listdata1.clear()
//        listdata1.add(PaymentHistoryListData("26 Jan 2021", false))
//        listdata1.add(PaymentHistoryListData("26 Jan 2021", true))
//        listdata1.add(PaymentHistoryListData("27 Jan 2021", false))
//        listdata1.add(PaymentHistoryListData("28 Jan 2021", true))
//        return view
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ClosedLoadFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            PaymentHistoryFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}