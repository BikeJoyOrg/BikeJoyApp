package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.example.bikejoyapp.UserState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.constraintlayout.compose.Visibility
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(userState: UserState, mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    mainViewModel.hideBottomBar()
    mainViewModel.hideTopBar()
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (!passwordVisibility) {
        painterResource(com.example.bikejoyapp.R.drawable.visibility_off)
    } else {
        painterResource(com.example.bikejoyapp.R.drawable.visibility_on)
    }

    Column (
        verticalArrangement =  Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = com.example.bikejoyapp.R.drawable.logo_fondo_invisible),
            contentDescription = "App Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )

            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 5.dp),
                onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = "Password visibility"
                    )
                }
            }
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.padding(20.dp),
                onClick = {
                    status = userState.login(username, password)

            }) {
                Text("Login")
            }
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Status: $status")
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Don't have an account?")
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Register")
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.CenterVertically)
            ) {
                mainViewModel.navigateTo(MyAppRoute.Register)
            }
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Entrar como invitado")
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.CenterVertically)
            ) {
                status = "invitado"
            }
        }
    }
    if(status == "success login" || status == "invitado") {
        mainViewModel.navigateTo(MyAppRoute.Map)
        mainViewModel.showBottomBar()
        mainViewModel.showTopBar()
    }
}