package com.example.eletriccarapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eletriccarapp.ui.CarFragment
import com.example.eletriccarapp.ui.FavoritosFragment

class TabsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {

        // a depender da tab(position), vai retornar um fragment
        return when (position) {
            0 -> CarFragment()
            1 -> FavoritosFragment()
            else -> CarFragment()
        }
    }

    // 2 tabs
    override fun getItemCount(): Int {
        return 2
    }
}