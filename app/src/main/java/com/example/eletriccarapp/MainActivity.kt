package com.example.eletriccarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.example.eletriccarapp.presentation.adapter.CarAdapter

class MainActivity : AppCompatActivity() {

    lateinit var btnRedirect: Button
    lateinit var listaCarros: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListener()
        setupList()
    }

    fun setupView() {
        btnRedirect = findViewById(R.id.btn_redirecionar)
        listaCarros = findViewById(R.id.rv_lista_carros)
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
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }
    }
}