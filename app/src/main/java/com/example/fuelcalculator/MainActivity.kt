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

    var woc by remember {mutableStateOf("")}
    var tboc by remember { mutableStateOf("") }
    var zpera by remember { mutableStateOf("") }
    var zperp by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Введення даних для розрахунку:", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = tboc,
            onValueChange = { tboc = it },
            label = { Text("Середня тривалість відновлення") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = woc,
            onValueChange = { woc = it },
            label = { Text("Частота відмов одноколової системи") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = zpera,
            onValueChange = { zpera = it },
            label = { Text("Зпер.а (грн/кВт*год)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = zperp,
            onValueChange = { zperp = it },
            label = { Text("Зпер.п (грн/кВт*год)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(12.dp))


        Button(onClick = {
            try {
                val wocVal = woc.toDouble()
                val tbocVal = tboc.toDouble()
                val zperaVal = zpera.toDouble()
                val zperpVal = zperp.toDouble()


                val kaoc = wocVal * tbocVal / 8760;
                val kpoc = 1.2 * 43/8760;
                val wdk = 2 * wocVal * (kaoc + kpoc);
                val wdc = wdk + 0.02;

                val wneda = 0.01 * 45 / 1000 * 5.12 * 1000 * 6451;
                val wnedp = 4 * 5.12 * 6451;
                val zper = wneda * zperaVal + wnedp * zperpVal;


                result = """
                    Коефіцієнт аварійного простою одноколової системи = ${"%.2f".format(kaoc * 10000)} * 10^(-4);
                    Коефіцієнт планового простою одноколової системи = ${"%.2f".format(kpoc * 10000)} * 10^(-4);
                    Частота відмов одночасно двох кіл двоколової системи = ${"%.2f".format(wdk * 10000)} * 10^(-4);
                    Частота відмов двоколової системи з урахуванням секційного вимикача = ${"%.4f".format(wdc)}

                    Математичне сподівання аварійного недовідпущення електроенергії = ${"%.2f".format(wneda)} кВт*год;
                    Математичне сподівання планового недовідпущення електроенергії = ${"%.2f".format(wnedp)} кВт*год;
                    Математичне сподівання збитків від переривання електропостачання = ${"%.2f".format(zper)} грн.
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