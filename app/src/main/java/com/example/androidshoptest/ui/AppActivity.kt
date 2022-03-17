package com.example.androidshoptest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.androidshoptest.R
import com.example.androidshoptest.databinding.ActivityAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    private var _binding:ActivityAppBinding?=null
    private val binding get() = _binding!!
    var navHostFragment: NavHostFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
//        setupWithNavController(binding.activityAppBottomNavBar, navHostFragment!!.navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}