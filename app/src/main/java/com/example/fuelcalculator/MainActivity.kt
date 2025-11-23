package com.example.fuelcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "calc1",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("calc1") { Calculator1Screen() }
                        composable("calc2") { Calculator2Screen() }
                        composable("calc3") { Calculator3Screen() }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Calculate, contentDescription = null) },
            label = { Text("Кабель") },
            selected = navController.currentDestination?.route == "calc1",
            onClick = { navController.navigate("calc1") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ElectricalServices, contentDescription = null) },
            label = { Text("Струми") },
            selected = navController.currentDestination?.route == "calc2",
            onClick = { navController.navigate("calc2") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.FlashOn, contentDescription = null) },
            label = { Text("КЗ") },
            selected = navController.currentDestination?.route == "calc3",
            onClick = { navController.navigate("calc3") }
        )
    }
}

@Composable
fun Calculator1Screen() {
    val scrollState = rememberScrollState()
    var I by remember { mutableStateOf("") }
    var t by remember { mutableStateOf("") }
    var J by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Калькулятор перерізу кабелю", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = I,
            onValueChange = { I = it },
            label = { Text("Iкз (A)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = t,
            onValueChange = { t = it },
            label = { Text("Час вимкнення t (с)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = J,
            onValueChange = { J = it },
            label = { Text("Густина струму Jек (A/мм²)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val i = I.toDouble()
                    val time = t.toDouble()
                    val j = J.toDouble()
                    val S = i * sqrt(time) / j
                    result = "S = %.2f мм²".format(S)
                } catch (e: Exception) {
                    result = "Помилка у введенні"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Розрахувати")
        }

        Spacer(Modifier.height(20.dp))
        Text(result, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun Calculator2Screen() {
    val scrollState = rememberScrollState()
    var U by remember { mutableStateOf("") }
    var Skz by remember { mutableStateOf("") }
    var Snom by remember { mutableStateOf("") }
    var Sb by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Калькулятор струмів КЗ на шинах 10кВ ГПП", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = U, onValueChange = { U = it }, label = { Text("Середня номінальна напруга Uс.н (кВ)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = Skz, onValueChange = { Skz = it }, label = { Text("Потужність КЗ Sкз (МВА)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = Snom, onValueChange = { Snom = it }, label = { Text("Номінальне значення потужності Sном (МВА)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = Sb, onValueChange = { Sb = it }, label = { Text("Базисна потужність Sб (МВА)") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            try {
                val u = U.toDouble()
                val skz = Skz.toDouble()
                val snom = Snom.toDouble()
                val sb = Sb.toDouble()

                val xc = u.pow(2) / skz
                val xt = u.pow(3) / (100 * snom)
                val x = xc + xt
                val startAmperage = u / (sqrt(3.0) * x) * 1000  // в кА
                val basicAmperage = sb / (sqrt(3.0) * u)

                result = """
                    Опори елементів заступної схеми:
                    Xc = ${"%.2f".format(xc)} Ом
                    Xт = ${"%.2f".format(xt)} Ом
                    Сумарний опір для точки К1: X = ${"%.2f".format(x)} Ом
                    
                    Початкове діюче значення струму трифазного КЗ:
                    Iп = ${"%.2f".format(startAmperage)} кА
                    
                    Базисне значення струму трифазного КЗ:
                    Iб = ${"%.2f".format(basicAmperage)} кА
                """.trimIndent()
            } catch (e: Exception) {
                result = "Помилка у введенні"
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Розрахувати")
        }

        Spacer(Modifier.height(20.dp))
        Text(result)
    }
}

@Composable
fun Calculator3Screen() {
    val scrollState = rememberScrollState()
    var voltageInput by remember { mutableStateOf("") }
    var powerInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Завдання 3: Розрахунок струмів КЗ", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = voltageInput,
            onValueChange = { voltageInput = it },
            label = { Text("Середня номінальна напруга (Uв.н), кВ") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = powerInput,
            onValueChange = { powerInput = it },
            label = { Text("Номінальна потужність (Sном.т), МВА") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            try {
                val voltage = voltageInput.replace(',', '.').toDouble()
                val power = powerInput.replace(',', '.').toDouble()

                val xt = 11.1 * voltage.pow(2.0) / (100 * power)
                val xsh = xt + 24.02
                val zsh = sqrt(10.65.pow(2.0) + xsh.pow(2.0))
                val xshMin = xt + 65.58
                val zshMin = sqrt(34.88.pow(2.0) + xshMin.pow(2.0))

                val i1_3 = voltage * 1000 / (sqrt(3.0) * zsh)
                val i1_2 = i1_3 * sqrt(3.0) / 2
                val i1_3_min = voltage * 1000 / (sqrt(3.0) * zshMin)
                val i1_2_min = i1_3_min * sqrt(3.0) / 2

                val k = 11.0.pow(2.0) / 115.0.pow(2.0)
                val xshNew = xsh * k
                val zshNew = sqrt((10.65 * k).pow(2.0) + xshNew.pow(2.0))
                val xshMinNew = xshMin * k
                val zshMinNew = sqrt((34.88 * k).pow(2.0) + xshMinNew.pow(2.0))

                val i2_3 = 11 * 1000 / (sqrt(3.0) * zshNew)
                val i2_2 = i2_3 * sqrt(3.0) / 2
                val i2_3_min = 11 * 1000 / (sqrt(3.0) * zshMinNew)
                val i2_2_min = i2_3_min * sqrt(3.0) / 2

                val rSum = 10.65 * k + 7.91
                val xSum = xshNew + 4.49
                val zSum = sqrt(rSum.pow(2.0) + xSum.pow(2.0))
                val rSumMin = 34.88 * k + 7.91
                val xSumMin = xshMinNew + 4.49
                val zSumMin = sqrt(rSumMin.pow(2.0) + xSumMin.pow(2.0))

                val i3_3 = 11 * 1000 / (sqrt(3.0) * zSum)
                val i3_2 = i3_3 * sqrt(3.0) / 2
                val i3_3_min = 11 * 1000 / (sqrt(3.0) * zSumMin)
                val i3_2_min = i3_3_min * sqrt(3.0) / 2

                result = """
                    Нормальний режим:
                    Струм трифазного КЗ (I1-3): ${"%.2f".format(i1_3)} A
                    Струм двофазного КЗ (I1-2): ${"%.2f".format(i1_2)} A
                    
                    Дійсний струм трифазного КЗ (I2-3): ${"%.2f".format(i2_3)} A
                    Дійсний струм двофазного КЗ (I2-2): ${"%.2f".format(i2_2)} A
                    
                    Струм короткого замикання трифазного КЗ (I3-3): ${"%.2f".format(i3_3)} A
                    Струм короткого замикання двофазного КЗ (I3-2): ${"%.2f".format(i3_2)} A
                    
                    Мінімальний режим:
                    Струм трифазного КЗ (I1-3): ${"%.2f".format(i1_3_min)} A
                    Струм двофазного КЗ (I1-2): ${"%.2f".format(i1_2_min)} A
                    Дійсний струм трифазного КЗ (I2-3): ${"%.2f".format(i2_3_min)} A
                    Дійсний струм двофазного КЗ (I2-2): ${"%.2f".format(i2_2_min)} A
                    Струм короткого замикання трифазного КЗ (I3-3): ${"%.2f".format(i3_3_min)} A
                    Струм короткого замикання двофазного КЗ (I3-2): ${"%.2f".format(i3_2_min)} A
                """.trimIndent()
            } catch (e: Exception) {
                result = "Помилка у введенні. Перевірте правильність чисел."
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Обрахувати")
        }

        Spacer(Modifier.height(20.dp))
        Text("Результати:", style = MaterialTheme.typography.titleMedium)
        Text(result)
    }
}
