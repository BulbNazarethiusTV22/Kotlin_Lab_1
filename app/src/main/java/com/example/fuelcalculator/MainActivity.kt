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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FuelCalculatorScreen() }
    }
}

@Composable
fun FuelCalculatorScreen() {
    val scrollState = rememberScrollState()

    var H by remember { mutableStateOf("") }
    var C by remember { mutableStateOf("") }
    var S by remember { mutableStateOf("") }
    var N by remember { mutableStateOf("") }
    var O by remember { mutableStateOf("") }
    var W by remember { mutableStateOf("") }
    var A by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Склад палива (%):", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = H,
            onValueChange = { H = it },
            label = { Text("H (водень)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = C,
            onValueChange = { C = it },
            label = { Text("C (вуглець)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = S,
            onValueChange = { S = it },
            label = { Text("S (сірка)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = N,
            onValueChange = { N = it },
            label = { Text("N (азот)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = O,
            onValueChange = { O = it },
            label = { Text("O (кисень)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = W,
            onValueChange = { W = it },
            label = { Text("W (волога)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = A,
            onValueChange = { A = it },
            label = { Text("A (зола)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            try {
                val hp = H.toDouble()
                val cp = C.toDouble()
                val sp = S.toDouble()
                val np = N.toDouble()
                val op = O.toDouble()
                val wp = W.toDouble()
                val ap = A.toDouble()

                val krs = 100 / (100 - wp)
                val krg = 100 / (100 - wp - ap)

                val h_s = hp * krs
                val c_s = cp * krs
                val s_s = sp * krs
                val n_s = np * krs
                val o_s = op * krs
                val a_s = ap * krs

                val h_g = hp * krg
                val c_g = cp * krg
                val s_g = sp * krg
                val n_g = np * krg
                val o_g = op * krg

                val qh = (339 * cp + 1030 * hp - 108.8 * (op - sp) - 25 * wp) / 1000
                val qs = (qh + 0.025 * wp) * 100 / (100 - wp)
                val qg = (qh + 0.025 * wp) * 100 / (100 - wp - ap)

                result = """
                    КРС (до сухої): ${"%.2f".format(krs)}
                    КРГ (до горючої): ${"%.2f".format(krg)}

                    🔹 Суха маса:
                    H: ${"%.2f".format(h_s)}%
                    C: ${"%.2f".format(c_s)}%
                    S: ${"%.2f".format(s_s)}%
                    N: ${"%.2f".format(n_s)}%
                    O: ${"%.2f".format(o_s)}%
                    A: ${"%.2f".format(a_s)}%

                    🔹 Горюча маса:
                    H: ${"%.2f".format(h_g)}%
                    C: ${"%.2f".format(c_g)}%
                    S: ${"%.2f".format(s_g)}%
                    N: ${"%.2f".format(n_g)}%
                    O: ${"%.2f".format(o_g)}%

                    🔥 Нижча теплота згоряння робочої маси (Qрн):
                    ${"%.4f".format(qh)} МДж/кг
                    
                    🔥 Нижча теплота згоряння сухої маси (Qсн):
                    ${"%.4f".format(qs)} МДж/кг
                    
                    🔥 Нижча теплота згоряння горючої маси (Qгн):
                    ${"%.4f".format(qg)} МДж/кг
                """.trimIndent()
            } catch (e: Exception) {
                result = "❗ Перевірь правильність введення чисел"
            }
        }) {
            Text("Розрахувати")
        }

        Spacer(Modifier.height(12.dp))
        Text(result)
    }
}


