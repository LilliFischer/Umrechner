package com.example.umrechner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.umrechner.ui.theme.UmrechnerTheme
import com.example.umrechner.ui.theme.UmrechnenButton
import com.example.umrechner.ui.theme.ResultCard


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UmrechnerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UmrechnerUI()
                }
            }
        }
    }
}

@Composable
fun UmrechnerUI() {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Alter in Minuten") }

    val options = listOf("Alter in Minuten", "Fläche in Fußballfelder", "Geld in Zeit")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedOption)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
        // Eingabe
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Eingabe") },
            modifier = Modifier.fillMaxWidth()

        )

        // Button
        UmrechnenButton(
            onClick = {
                val formatter = java.text.SimpleDateFormat("dd.MM.yyyy")
                val jetzt = java.util.Date()

                output = when (selectedOption) {
                    "Alter in Minuten" -> {
                        try {
                            val geburtstag = formatter.parse(input)
                            val differenzMillis = jetzt.time - geburtstag.time
                            val minuten = differenzMillis / (1000 * 60)
                            "Du bist ca. $minuten Minuten alt."
                        } catch (e: Exception) {
                            "Bitte gib ein gültiges Datum im Format TT.MM.JJJJ ein."
                        }
                    }

                    "Fläche in Fußballfelder" -> {
                        val flaeche = input.toDoubleOrNull()
                        if (flaeche != null) {
                            val fussballfelder = flaeche / 7140.0
                            "Das entspricht ca. %.2f Fußballfeldern.".format(fussballfelder)
                        } else {
                            "Bitte gib eine gültige Fläche in m² ein."
                        }
                    }

                    "Geld in Zeit" -> {
                        val betrag = input.toDoubleOrNull()
                        if (betrag != null) {
                            val sekunden = betrag
                            val tage = sekunden / (60 * 60 * 24)
                            val jahre = tage / 365.0
                            "Wenn du jede Sekunde 1 € ausgibst, reicht das für ca. %.1f Tage (%.2f Jahre).".format(tage, jahre)
                        } else {
                            "Bitte gib eine gültige Zahl für den Betrag ein."
                        }
                    }

                    else -> "Ungültige Auswahl"
                }
            }
        )
        // Ergebnisanzeige
        if (output.isNotEmpty()) {
            ResultCard(output)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UmrechnerPreview() {
    UmrechnerTheme {
        UmrechnerUI()
    }
}