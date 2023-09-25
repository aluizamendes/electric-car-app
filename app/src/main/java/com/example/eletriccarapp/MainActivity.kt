package com.example.eletriccarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity

class MainActivity : AppCompatActivity() {

    lateinit var btnRedirect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListener()
    }

    fun setupView() {
        btnRedirect = findViewById(R.id.btn_redirecionar)
    }

    fun setupListener() {
        btnRedirect.setOnClickListener {
            // definir uma intenção: de onde, e para onde irá ser direcionado
            startActivity(Intent(this, CalcularAutonomiaActivity::class.java))
        }
    }
}