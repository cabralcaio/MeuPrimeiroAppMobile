package com.caiocabrall.monetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById<TextView>(R.id.txt_result)

        val buttonconverter = findViewById<Button>(R.id.btn_converter)

        buttonconverter.setOnClickListener {
            converter()
        }

    }

    private fun converter() {
        val selectedCurrency2 = findViewById<RadioGroup>(R.id.radio_group2)

        val checked2 = selectedCurrency2.checkedRadioButtonId

        val currency2 = when (checked2) {
            R.id.radio_usd2 -> {
                "USD"
            }
            R.id.radio_eur2 -> {
                "EUR"
            }
            R.id.radio_clp2 -> {
                "CLP"
            }
            R.id.radio_brl2 -> {
                "BRL"
            }else -> {
                "GBP"
            }
        }

        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when (checked) {
            R.id.radio_usd -> {
                "USD"
            }
            R.id.radio_eur -> {
                "EUR"
            }
            R.id.radio_clp -> {
                "CLP"
            }
            R.id.radio_brl -> {
                "BRL"
            } else -> {
                "GBP"
            }
        }
        val editField = findViewById<EditText>(R.id.edit_field)
        val value = editField.text.toString()

        if (value.isEmpty()) return

        result.text = value
        result.visibility = View.VISIBLE

        Thread {
            // aqui acontece em paralelo

            val url =
                URL("https://free.currconv.com/api/v7/convert?q=${currency}_${currency2}&compact=ultra&apiKey=2df8c70666762f6b85c2")
            val conn = url.openConnection() as HttpsURLConnection

            try {
                val data = conn.inputStream.bufferedReader().readText()

                // {"jsdfnsj": 12332412} isso eh um formato json
                val obj = JSONObject(data)

                runOnUiThread {
                    val res = obj.getDouble("${currency}_${currency2}")
                    result.text = "${"%.2f".format(value.toDouble() * res)} ${currency2}"
                    result.visibility = View.VISIBLE
                }


            } finally {
                conn.disconnect()
            }
        }.start()

    }

}