package com.example.foodplanner.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.foodplanner.R
import com.example.foodplanner.viewModel.NetworkViewModel

class HomeFragment : Fragment() {

    private lateinit var noInternetImage: ImageView
    private lateinit var mainContent: ScrollView
    private val networkViewModel: NetworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        networkViewModel.checkInternetConnection()
        networkViewModel.isConnected.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                noInternetImage.visibility = View.GONE
                mainContent.visibility = View.VISIBLE
            } else {
                noInternetImage.visibility = View.VISIBLE
                mainContent.visibility = View.GONE
            }
        })
    }

    private fun initUi(view: View) {
        noInternetImage = view.findViewById(R.id.noInternetImage)
        mainContent = view.findViewById(R.id.mainContent)
    }
}