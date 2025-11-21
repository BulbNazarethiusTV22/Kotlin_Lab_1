package com.example.fuelcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EnergyCalculatorScreen() }
    }
}

@Composable
fun EnergyCalculatorScreen() {
    val scrollState = rememberScrollState()


    var Pc by remember { mutableStateOf("") }
    var sigma by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var penalty by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Введення даних для розрахунку:", style = MaterialTheme.typography.titleLarge)


        OutlinedTextField(
            value = Pc,
            onValueChange = { Pc = it },
            label = { Text("Середньодобова потужність, Pc (МВт)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )


        OutlinedTextField(
            value = sigma,
            onValueChange = { sigma = it },
            label = { Text("Середньоквадратичне відхилення, σ") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )


        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Ціна за МВт·год, B (тис. грн)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(12.dp))


        Button(onClick = {
            try {
                val PcVal = Pc.toDouble()
                val sigmaVal = sigma.toDouble()
                val B = price.toDouble()


                val deltaW1 = erfNormalized(PcVal, sigmaVal)


                val W1 = PcVal * 24 * deltaW1
                val W2 = PcVal * 24 * (1 - deltaW1)
                val P1 = W1 * B
                val SH1 = W2 * B


                result = """
                    δW1 = ${"%.4f".format(deltaW1)}
                    W1 = ${"%.2f".format(W1)} МВт·год
                    W2 = ${"%.2f".format(W2)} МВт·год
                    Прибуток = ${"%.2f".format(P1)} тис. грн
                    Штраф = ${"%.2f".format(SH1)} тис. грн
                    Чистий прибуток = ${"%.2f".format(P1 - SH1)} тис. грн
                """.trimIndent()
            } catch (e: Exception) {
                result = "Помилка у введенні даних"
            }
        }) {
            Text("Розрахувати")
        }

        Spacer(Modifier.height(12.dp))
        Text(result)
    }
}

fun erfNormalized(mean: Double, sigma: Double): Double {
    val a = mean - (mean * 0.05)
    val b = mean + (mean * 0.05)
    val n = 1000
    val dx = (b - a) / n
    var sum = 0.0
    for (i in 0 until n) {
        val x = a + i * dx
        val fx = (1 / (sigma * sqrt(2 * Math.PI))) * exp(-((x - mean).pow(2)) / (2 * sigma.pow(2)))
        sum += fx * dx
    }
    return (sum * 100).roundToInt() / 100.0
}