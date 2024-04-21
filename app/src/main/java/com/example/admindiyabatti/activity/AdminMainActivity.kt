package com.example.admindiyabatti.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.admindiyabatti.R
import com.example.admindiyabatti.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor()
        NavigationUI.setupWithNavController(binding.bottomMenu,Navigation.findNavController(this,R.id.fragmentContainerView2))
    }

    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors = ContextCompat.getColor(context,R.color.diyaFlame)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}