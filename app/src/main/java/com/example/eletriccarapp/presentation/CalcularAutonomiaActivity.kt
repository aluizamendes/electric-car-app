package com.example.eletriccarapp.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eletriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var precoKwh: EditText
    lateinit var btnCalcular: Button
    lateinit var kmPercorrido: EditText
    lateinit var resultado: TextView
    lateinit var btnClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)

        setupView()
        setupListeners()
    }

    fun setupView() {
        // seleciona os elementos através do id
        precoKwh = findViewById(R.id.et_preco_kwh)
        btnCalcular = findViewById(R.id.btn_calcular)
        kmPercorrido = findViewById(R.id.et_km_percorrido)
        resultado = findViewById(R.id.tv_resultado)
        btnClose = findViewById(R.id.iv_close)
    }

    fun setupListeners() {

        btnCalcular.setOnClickListener {

            val autonomia = calcular()
             resultado.text = "A autonomia é ${autonomia.toString()}"

            // Log.d("Resultado", resultado.toString())
        }

        btnClose.setOnClickListener {
            // `finish()` -> encerra a tela atual e volta pra anterior a pilha
            finish()
        }
    }

    fun calcular(): Float {
        val preco = precoKwh.text.toString().toFloat()
        val km = kmPercorrido.text.toString().toFloat()
        val autonomia = preco / km

        return autonomia
    }
}