package com.example.myapplication

import android.os.Bundle
import android.widget.TextClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.sin
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.theme.ColorSamuel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme() {

                registro()


            }


        }
    }
}

class Personas(var usuario: String, var correo: String) {

    constructor(usuario: String, correo: String, clave: String) :
            this(usuario, correo) {
        this.clave = clave
    }

    constructor(usuario: String, correo: String, clave: String, reingresarClave: String) :
            this(usuario, correo, clave) {
        this.reingresarClave = reingresarClave
    }

    var clave: String = ""
        get() = field
    var reingresarClave: String = ""
        get() = field

}

@Composable
fun registro() {

    val listaPeronas = remember { mutableStateListOf<Personas>() }

    var usuario by rememberSaveable() { mutableStateOf("") }
    var correo by rememberSaveable() { mutableStateOf("") }
    var clave by rememberSaveable() { mutableStateOf("") }
    var reingresarClave by rememberSaveable() { mutableStateOf("") }

    var isErrorCorreo by rememberSaveable() { mutableStateOf(false) }
    var isErrorClave by rememberSaveable() { mutableStateOf(false) }
    var isErrorSegundaClave by rememberSaveable() { mutableStateOf(false) }
    var isUsuarioLargo by rememberSaveable() { mutableStateOf(false) }
    var conTexto = LocalContext.current


    // Regla para que todos los campos deban estar llenos
    val camposLlenos =
        usuario.isNotBlank() &&
                correo.isNotBlank() &&
                clave.isNotBlank() &&
                reingresarClave.isNotBlank()

    fun validarCorreo(correo: String) {
        isErrorCorreo = !correo.contains("@") || correo.length < 5 || !correo.contains(".")
    }

    fun claveRepetida(reingresarClave: String) {
        isErrorSegundaClave =
            clave.isNotEmpty() && reingresarClave.isNotEmpty() && clave != reingresarClave
    }

    fun validadorClave(clave: String) {
        val largoMaximo = clave.length in 6..10
        val tieneMayuscula = clave.any { it.isUpperCase() }
        val tieneMinuscula = clave.any { it.isLowerCase() }
        val tieneNumero = clave.any { it.isDigit() }

        isErrorClave = !(largoMaximo && tieneMayuscula && tieneMinuscula && tieneNumero)

    }

    fun validarUsuario(usuario: String) {
        isUsuarioLargo = usuario.length >= 20

    }

    @Composable
    fun horientacionPantalla(
        clave: String,
        claveDos: (String) -> Unit,
        reingresarClave: String,
        reingresaClaveDos: (String) -> Unit
    ) {
        val configuration = LocalConfiguration.current
        val estadoPantalla = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (estadoPantalla) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = clave,
                    onValueChange = claveDos,
                    label = { Text("Crea una clave") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    isError = isErrorSegundaClave || isErrorClave,
                    supportingText = {
                        when {
                            isErrorSegundaClave -> {
                                Text(
                                    "Las claves no coinciden, intente nuevamente",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            isErrorClave -> {
                                Text(
                                    "La clave debe tener entre 6 y 10 caracteres, incluir mayúscula, minúscula y número",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                        }
                    },
                    trailingIcon = {
                        when {
                            isErrorSegundaClave || isErrorClave -> {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Error en la clave",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                            clave.isNotEmpty() -> {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Clave correcta",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                        }

                    }

                )
                OutlinedTextField(
                    value = reingresarClave,
                    onValueChange = reingresaClaveDos,
                    label = { Text("Vuelve a ingresa la clave") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy((8.dp))
            ) {
                OutlinedTextField(
                    value = clave,
                    onValueChange = claveDos,
                    label = { Text("Crea una clave") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isErrorSegundaClave || isErrorClave,
                    supportingText = {
                        when {
                            isErrorSegundaClave -> {
                                Text(
                                    "Las claves no coinciden, intente nuevamente",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            isErrorClave -> {
                                Text(
                                    "La clave debe tener entre 6 y 10 caracteres, incluir mayúscula, minúscula y número",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                        }
                    },
                    trailingIcon = {
                        when {
                            isErrorSegundaClave || isErrorClave -> {
                                Icon(
                                    imageVector = Icons.Filled.Error,
                                    contentDescription = "Clave incorrecta",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                            clave.isNotEmpty() -> {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Clave correcta",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                )
                OutlinedTextField(
                    value = reingresarClave,
                    onValueChange = reingresaClaveDos,
                    label = { Text("Vuelve a ingresar la clave") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorSamuel)
                .padding(vertical = 15.dp)

        ){
            Text(
                text = "Nuevo usuario",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),

                )

        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {



            OutlinedTextField(
                value = usuario, onValueChange =
                    {
                        usuario = it
                        validarUsuario(it)
                    },
                label = { Text("Crea tu usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                isError = isUsuarioLargo,
                supportingText = {
                    if (isUsuarioLargo) {
                        Text(
                            "El nombre de $usuario es demaciado largo",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }


            )
            OutlinedTextField(
                value = correo, onValueChange = {
                    correo = it
                    validarCorreo(it)

                },
                isError = isErrorCorreo,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                label = { Text("Ingresa tu correo") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    if (isErrorCorreo) {
                        Text(
                            "Ingresa un correo valido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }
            )
            horientacionPantalla(
                clave = clave,
                claveDos = {
                    clave = it
                    validadorClave(it)
                },
                reingresarClave = reingresarClave,
                reingresaClaveDos = {
                    reingresarClave = it
                    claveRepetida(it)
                }
            )

            Button(
                enabled = !isErrorClave && !isErrorCorreo && !isErrorSegundaClave && camposLlenos,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    usuario = usuario.replace(" ", "")
                    if (camposLlenos) {
                    }
                    val nuevaPersona = Personas(usuario, correo, clave, reingresarClave)
                    listaPeronas.add(nuevaPersona)
                    Toast.makeText(
                        conTexto,
                        "El usuario $usuario a sido registrado correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                    listaPeronas.forEachIndexed { index, persona ->
                        println("${index + 1}) Usuario: ${persona.usuario} - Clave: ${persona.clave} - reingreseCalve: ${persona.reingresarClave} - Correo: ${persona.correo}")
                    }
                    usuario = ""
                    clave = ""
                    correo = ""
                    reingresarClave = ""
                }
            ) { Text("Registrar") }


        }
    }

}


