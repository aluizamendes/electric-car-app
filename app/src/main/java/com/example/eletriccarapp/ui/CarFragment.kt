package com.example.eletriccarapp.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.example.eletriccarapp.presentation.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var fabCalcular: FloatingActionButton
    lateinit var listaCarros: RecyclerView
    lateinit var progressBar: ProgressBar

    var carrosArray: ArrayList<Carro> = ArrayList()

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
        setupListener()
        callService()
    }

    fun setupView(view: View) {
        fabCalcular = view.findViewById(R.id.fab_calcular)
        listaCarros = view.findViewById(R.id.rv_lista_carros)
        progressBar = view.findViewById(R.id.pb_loader)
    }

    fun setupList() {

        listaCarros.visibility = View.VISIBLE

        // criar um adapter
        val adapter = CarAdapter(carrosArray)
        listaCarros.adapter = adapter
    }

    fun setupListener() {
        fabCalcular.setOnClickListener {
            // definir uma intenção: de onde, e para onde irá ser direcionado
//            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }

    fun callService() {
        GetCarInformations().execute("https://igorbag.github.io/cars-api/cars.json")
    }

    inner class GetCarInformations : AsyncTask<String, String, String>() {
        fun OnPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            Log.d("Minha task", "iniciando....")
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty("Accept", "application/json")

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)

                } else {
                    Log.e("Erro", "Servico indisponivel no momento...")
                }

            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao realizar processamento")

            } finally {
                urlConnection?.disconnect()
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                // converter pra um array de json
                val jsonArray= JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("id ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("preco ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")



                    val modelo = Carro(
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlFoto = urlPhoto
                    )

                    // adiciona para o array de carro
                    carrosArray.add(modelo)

                    Log.d("Model -> ", modelo.toString())
                }

                progressBar.visibility = View.GONE // depois de carregar os dados, a barra desaparece
                setupList()

            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}