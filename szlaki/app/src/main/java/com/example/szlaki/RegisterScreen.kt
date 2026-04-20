package com.example.szlaki

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController, vm: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    val message by vm.message.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rejestracja", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Login") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Hasło") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Imię") })
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Nazwisko") })
        OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Data urodzenia (DD-MM-YYYY)") })

        if (message != null) {
            Text(message!!, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { vm.register(login, password, firstName, lastName, birthDate) }) {
            Text("Załóż konto")
        }
        TextButton(onClick = {
            vm.clearMessage()
            navController.popBackStack()
        }) {
            Text("Powrót do logowania")
        }
    }
}