package com.example.bikejoyapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(userViewModel: UserViewModel, mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    mainViewModel.hideBottomBar()
    mainViewModel.hideTopBar()
    var passwordVisibility1 by remember { mutableStateOf(false) }
    val icon1 = if (!passwordVisibility1) {
        painterResource(R.drawable.visibility_off)
    } else {
        painterResource(R.drawable.visibility_on)
    }
    var passwordVisibility2 by remember { mutableStateOf(false) }
    val icon2 = if (!passwordVisibility2) {
        painterResource(R.drawable.visibility_off)
    } else {
        painterResource(R.drawable.visibility_on)
    }

    Column (
        verticalArrangement =  Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_fondo_invisible),
            contentDescription = "App Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp)
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
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            OutlinedTextField(
                value = password1,
                onValueChange = { password1 = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if(passwordVisibility1) VisualTransformation.None else PasswordVisualTransformation()
            )

            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    onClick = {
                    passwordVisibility1 = !passwordVisibility1
                }) {
                    Icon(
                        painter = icon1,
                        contentDescription = "Password visibility"
                    )
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
            ) {
                OutlinedTextField(
                    value = password2,
                    onValueChange = { password2 = it },
                    label = { Text("Confirm Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if(passwordVisibility2) VisualTransformation.None else PasswordVisualTransformation()
            )

            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    onClick = {
                    passwordVisibility2 = !passwordVisibility2
                }) {
                    Icon(
                        painter = icon2,
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
                coroutineScope.launch {
                    status = userViewModel.register(
                        username,
                        email,
                        password1,
                        password2
                    )
                }
            }) {
                Text("Register")
            }
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Status: $status")
            if(status == "Success") {
                mainViewModel.navigateTo(MyAppRoute.Login)
            }
        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Already have an account?")
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Log in")
                    }
                },
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.CenterVertically)
            ) {
                mainViewModel.navigateTo(MyAppRoute.Login)
            }
        }
    }
}