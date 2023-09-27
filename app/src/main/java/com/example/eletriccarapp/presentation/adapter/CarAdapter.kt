package com.example.eletriccarapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.domain.Carro
import org.w3c.dom.Text

class CarAdapter(private val carros: List<Carro>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    // cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false)
        return ViewHolder(view)
    }

    // pega o conteudo da view e troca pela informaçao de item de uma lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.preco.text = carros[position].preco
        holder.bateria.text = carros[position].bateria
        holder.potencia.text = carros[position].potencia
        holder.recarga.text = carros[position].recarga

    }

    // quantidade de itens da lista
    override fun getItemCount(): Int = carros.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preco: TextView
        val bateria: TextView
        val potencia: TextView
        val recarga: TextView

        init {
            preco = view.findViewById(R.id.tv_preco_value)
            bateria = view.findViewById(R.id.tv_bateria_value)
            potencia = view.findViewById(R.id.tv_potencia_value)
            recarga = view.findViewById(R.id.tv_recarga_value)

        }
    }

}

