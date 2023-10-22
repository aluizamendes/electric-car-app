package com.example.eletriccarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.databinding.ActivityMainBinding
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.example.eletriccarapp.presentation.adapter.CarAdapter
import com.example.eletriccarapp.presentation.adapter.TabsAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(binding.bottomNavigation, navController)

        setupListener()
    }

    private fun setupListener() {
        binding.fabCalcular.setOnClickListener {
            // definir uma intenção: de onde, e para onde irá ser direcionado
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }
    }



}