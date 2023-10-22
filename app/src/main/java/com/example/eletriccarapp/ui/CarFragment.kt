package com.example.eletriccarapp.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.CarFactory
import com.example.eletriccarapp.data.CarsApi
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.data.local.CarrosContract
import com.example.eletriccarapp.data.local.CarsDbHelper
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.presentation.CalcularAutonomiaActivity
import com.example.eletriccarapp.presentation.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var listaCarros: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noInternetText: TextView
    lateinit var carsApi: CarsApi

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

        setupRetrofit()
        setupView(view)

        if (checkForInternet(context)) {
            // callService() -> a outra forma de chamar serviço
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val url = "https://igorbag.github.io/cars-api/"
        val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>> {

            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {

                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE // depois de carregar os dados, a barra desaparece
                    noInternetImage.visibility = View.GONE
                    noInternetText.visibility = View.GONE

                    response.body()?.let {
                        setupList(it)
                    }
                } else {
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {

            }

        })
    }

    fun emptyState() {
        progressBar.visibility = View.GONE
        listaCarros.visibility = View.GONE
        noInternetImage.visibility = View.VISIBLE
        noInternetText.visibility = View.VISIBLE
    }

    fun setupView(view: View) {
        listaCarros = view.findViewById(R.id.rv_lista_carros)
        progressBar = view.findViewById(R.id.pb_loader)
        noInternetImage = view.findViewById(R.id.iv_empty_state)
        noInternetText = view.findViewById(R.id.tv_no_wifi)
    }
    fun setupList(lista: List<Carro>) {

        listaCarros.visibility = View.VISIBLE

        // criar um adapter
        val adapter = CarAdapter(lista)
        listaCarros.adapter = adapter

        adapter.carItemListener = { carro ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }
    }




    fun callService() {
        GetCarInformations().execute("https://igorbag.github.io/cars-api/cars.json")
    }

    fun checkForInternet(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    // Utilizar o retrofit como abstração do async task
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
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

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
                        urlPhoto = urlPhoto,
                        isFavorite = false
                    )

                    // adiciona para o array de carro
                    carrosArray.add(modelo)

                    Log.d("Model -> ", modelo.toString())
                }

                progressBar.visibility = View.GONE // depois de carregar os dados, a barra desaparece
                noInternetImage.visibility = View.GONE
                noInternetText.visibility = View.GONE

                // setupList()

            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }


}