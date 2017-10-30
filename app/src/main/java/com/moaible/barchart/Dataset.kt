package com.moaible.barchart

import android.content.Context
import android.util.Log
import com.univocity.parsers.csv.CsvParser
import com.univocity.parsers.csv.CsvParserSettings
import java.io.InputStreamReader
import java.util.*

/**
 * Created by moaible.
 */

class Dataset(internal var context: Context) {
    private val settings = CsvParserSettings()
    private val parser = CsvParser(settings)
    var models = ArrayList<Model>()

    init {
        settings.selectFields("id", "hdate", "product", "price", "time")
        settings.numberOfRowsToSkip = 1
    }

    /**
     * Parse CSV data from assets
     */
    fun parseCSV() {
        models.clear()
        try {
            val inputStreamReader = InputStreamReader(context.assets.open("data.csv"))
            val allRows = parser.parseAll(inputStreamReader)
            for (array in allRows) {
                if (!array[0].equals("id")) {
                    val model = Model()
                    model.parseArray(array)
                    models.add(model)
                }
            }
        } catch (e: Exception) {
            Log.e(Dataset::class.java.name, "Failed to read CSV", e)
        }

    }
}
