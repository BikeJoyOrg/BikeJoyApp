package com.example.bikejoyapp.users.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.bikejoyapp.utils.MyAppRoute
import com.example.bikejoyapp.utils.MainViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.users.viewmodel.UserViewModel
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(userViewModel: UserViewModel, mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var focusRequester = remember { FocusRequester() }
    var showSnackbar by remember { mutableStateOf(false) }
    mainViewModel.hideBottomBar()
    mainViewModel.hideTopBar()
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (!passwordVisibility) {
        painterResource(com.example.bikejoyapp.R.drawable.visibility_off)
    } else {
        painterResource(com.example.bikejoyapp.R.drawable.visibility_on)
    }

    Box {
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(10.dp, top=30.dp),
                containerColor = Color.Transparent,
                contentColor = Color.Red.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(status)
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = com.example.bikejoyapp.R.drawable.logo_fondo_invisible),
                contentDescription = "App Icon",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .width(280.dp),
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .width(280.dp),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        coroutineScope.launch {
                            if (username != "" && password != "") {

                                status = userViewModel.login(username, password)
                                userViewModel.getCompletedRoutes()
                            } else {
                                status = "Please fill in all fields"
                                showSnackbar = true
                                delay(2000)
                                showSnackbar = false
                            }
                        }
                    }),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                            Icon(
                                painter = icon,
                                contentDescription = "Password visibility"
                            )
                        }
                    }
                )

            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        coroutineScope.launch {
                            if (username != "" && password != "") {
                                status = userViewModel.login(username, password)
                                userViewModel.getCompletedRoutes()
                            } else {
                                status = "Please fill in all fields"
                                showSnackbar = true
                                delay(2000)
                                showSnackbar = false
                            }
                        }
                    }
                ) {
                    Text("Login")
                }
            }

            /*Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(status, color= Color.Red)
            }*/

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Don't have an account?")
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue, fontStyle = FontStyle.Italic)) {
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
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue, fontStyle = FontStyle.Italic)) {
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
        if (status == "Success" || status == "invitado") {
            mainViewModel.navigateTo(MyAppRoute.Map)
            mainViewModel.showBottomBar()
            mainViewModel.showTopBar()
        }
    }
}