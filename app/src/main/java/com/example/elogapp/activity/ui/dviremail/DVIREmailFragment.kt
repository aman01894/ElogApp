package com.example.elogapp.activity.ui.dviremail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DVIREmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DVIREmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var data : ArrayList<DvirEmailData> = ArrayList();
    private lateinit var rAdapter: DVIR_EmailListAdapter
    lateinit var rView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_dvir_email, container, false)


        rView = view.findViewById(R.id.rv_list_history)
        rAdapter = DVIR_EmailListAdapter(data)
        rView.adapter = rAdapter
        rAdapter.notifyDataSetChanged()

        data.clear()
        data.add(DvirEmailData("26 Jan 2021", false))
        data.add(DvirEmailData("26 Jan 2021", true))
        data.add(DvirEmailData("27 Jan 2021", false))
        data.add(DvirEmailData("28 Jan 2021", true))

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DVIREmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DVIREmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}