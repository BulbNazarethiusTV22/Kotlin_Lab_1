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

    var coalB by remember { mutableStateOf("") }
    var oilB by remember { mutableStateOf("") }
    var gasB by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Введіть значення В (витрат палива):", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = coalB,
            onValueChange = { coalB = it },
            label = { Text("Вугілля (Ввуг)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = oilB,
            onValueChange = { oilB = it },
            label = { Text("Мазут (Вмаз)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            value = gasB,
            onValueChange = { gasB = it },
            label = { Text("Газ (Вгаз)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            try {
                val coal = coalB.toDouble()
                val oil = oilB.toDouble()

                val coalE = 150 * coal * 20.47 / 1000000
                val oilE = 0.57 * oil * 39.48 / 1000000
                var total = oilE + coalE

                result = """
                    🔥 Валовий викид при спалюванні вугілля:
                    ${"%.2f".format(coalE)} т;
                    
                    🔥 Валовий викид при спалюванні мазуту:
                    ${"%.2f".format(oilE)} т;
                    
                    При спалюванні природного газу тверді частинки відсутні;
                    
                    🔥 Сумарний валовий викид при спалюванні усіх видів палива:
                    ${"%.2f".format(total)} т.
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