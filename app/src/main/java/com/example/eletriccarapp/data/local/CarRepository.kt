package com.example.eletriccarapp.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.eletriccarapp.domain.Carro

class CarRepository(private val context: Context) {
    fun save(carro: Carro): Boolean {
        var isSaved = false

        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(CarrosContract.CarEntry.COLUMN_NAME_CAR_ID, carro.id)
                put(CarrosContract.CarEntry.COLUMN_NAME_PRECO, carro.preco)
                put(CarrosContract.CarEntry.COLUMN_NAME_BATERIA, carro.bateria)
                put(CarrosContract.CarEntry.COLUMN_NAME_POTENCIA, carro.potencia)
                put(CarrosContract.CarEntry.COLUMN_NAME_RECARGA, carro.recarga)
                put(CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
            }

            val inserted = db?.insert(CarrosContract.CarEntry.TABLE_NAME, null, values)

            if (inserted != null) {
                isSaved = true
            }

        } catch (ex: Exception) {
            ex.message?.let { Log.e("Erro -> ", it) }
        }
        return isSaved
    }

    fun findCarById(id: Int): Carro {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase

        // listagem das colunas a serem exibidas no resultado da query
        val columns = arrayOf(
            BaseColumns._ID,
            CarrosContract.CarEntry.COLUMN_NAME_CAR_ID,
            CarrosContract.CarEntry.COLUMN_NAME_PRECO,
            CarrosContract.CarEntry.COLUMN_NAME_BATERIA,
            CarrosContract.CarEntry.COLUMN_NAME_POTENCIA,
            CarrosContract.CarEntry.COLUMN_NAME_RECARGA,
            CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO)

        val filter = "${CarrosContract.CarEntry.COLUMN_NAME_CAR_ID} = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            CarrosContract.CarEntry.TABLE_NAME, // nome da tabela
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )

        var itemId: Long = 0
        var preco: String = ""
        var bateria: String = ""
        var potencia: String = ""
        var recarga: String = ""
        var urlPhoto: String = ""

        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_CAR_ID))
                Log.d("ID ->", itemId.toString())

                preco = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_PRECO))
                bateria = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_BATERIA))
                potencia = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_POTENCIA))
                recarga = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_RECARGA))
                urlPhoto = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO))
            }
        }

        cursor.close()
        return Carro(
            id = itemId.toInt(),
            preco = preco,
            bateria = bateria,
            potencia = potencia,
            recarga = recarga,
            urlPhoto = urlPhoto,
            isFavorite = true
        )
    }

    fun saveIfNotExist(carro: Carro) {
        val car = findCarById(carro.id)
        if (car.id == ID_WHEN_NO_CAR) {
            save(carro)
        }
    }

    fun getAll(): List<Carro> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase

        // listagem das colunas a serem exibidas no resultado da query
        val columns = arrayOf(
            BaseColumns._ID,
            CarrosContract.CarEntry.COLUMN_NAME_CAR_ID,
            CarrosContract.CarEntry.COLUMN_NAME_PRECO,
            CarrosContract.CarEntry.COLUMN_NAME_BATERIA,
            CarrosContract.CarEntry.COLUMN_NAME_POTENCIA,
            CarrosContract.CarEntry.COLUMN_NAME_RECARGA,
            CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO)

        val cursor = db.query(
            CarrosContract.CarEntry.TABLE_NAME, // nome da tabela
            columns,
            null,
            null,
            null,
            null,
            null
        )

        val carros = mutableListOf<Carro>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_CAR_ID))
                Log.d("ID ->", itemId.toString())

                val preco = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_PRECO))
                val bateria = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_BATERIA))
                val potencia = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_POTENCIA))
                val recarga = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_RECARGA))
                val urlPhoto = getString(getColumnIndexOrThrow(CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO))

                carros.add(
                    Carro(
                    id = itemId.toInt(),
                    preco = preco,
                    bateria = bateria,
                    potencia = potencia,
                    recarga = recarga,
                    urlPhoto = urlPhoto,
                    isFavorite = true
                ))
            }
        }

        cursor.close()
        return carros
    }

    companion object {
        const val ID_WHEN_NO_CAR = 0
    }
}