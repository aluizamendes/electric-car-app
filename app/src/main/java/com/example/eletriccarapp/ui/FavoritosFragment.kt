package com.example.eletriccarapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.presentation.adapter.CarAdapter

class FavoritosFragment : Fragment() {

    lateinit var listaCarrosFavoritos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favoritos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        setupList()
    }

    private fun getCarsOnLocalDb(): List<Carro> {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        return carList
    }

    fun setupView(view: View) {
        listaCarrosFavoritos = view.findViewById(R.id.rv_lista_carros_favoritos)
    }
    fun setupList() {
        val cars = getCarsOnLocalDb()
        listaCarrosFavoritos.visibility = View.VISIBLE

        // criar um adapter
        val adapter = CarAdapter(cars, true)
        listaCarrosFavoritos.adapter = adapter

        adapter.carItemListener = { carro ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }
    }
}