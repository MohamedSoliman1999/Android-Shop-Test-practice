package com.example.androidshoptest.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener

class ResultListenerFragment : Fragment() {
    var result : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            result = bundle.getString("bundleKey")
        }
    }
}