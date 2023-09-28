package com.example.eletriccarapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.example.eletriccarapp.presentation.adapter.CarAdapter

class CarFragment : Fragment() {

    lateinit var btnRedirect: Button
    lateinit var listaCarros: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList()
    }

    fun setupView(view: View) {
        btnRedirect = view.findViewById(R.id.btn_redirecionar)
        listaCarros = view.findViewById(R.id.rv_lista_carros)
    }

    fun setupList() {
        val dados = CarFactory.lista

        // criar um adapter
        val adapter = CarAdapter(dados)
        listaCarros.adapter = adapter
    }

    fun setupListener() {
        btnRedirect.setOnClickListener {
            // definir uma intenção: de onde, e para onde irá ser direcionado
            //startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }
    }
}