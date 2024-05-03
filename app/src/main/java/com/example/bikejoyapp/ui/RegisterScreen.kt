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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.viewmodel.MainViewModel
import com.example.bikejoyapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontStyle

@Composable
fun RegisterScreen(userViewModel: UserViewModel, mainViewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var focusRequester1 = remember { FocusRequester() }
    var focusRequester2 = remember { FocusRequester() }
    var focusRequester3 = remember { FocusRequester() }
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
                .size(350.dp)
        )

        Row (
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
                keyboardActions = KeyboardActions(onNext = { focusRequester1.requestFocus() })
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .width(280.dp)
                    .focusRequester(focusRequester1),
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() })
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .width(280.dp)
                    .focusRequester(focusRequester2),
                value = password1,
                onValueChange = { password1 = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequester3.requestFocus() }),
                visualTransformation = if(passwordVisibility1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisibility1 = !passwordVisibility1
                        }) {
                        Icon(
                            painter = icon1,
                            contentDescription = "Password visibility"
                        )
                    }
                }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .width(280.dp)
                        .focusRequester(focusRequester3),
                    value = password2,
                    onValueChange = { password2 = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if(username != "" && email != "" && password1 != "" && password2 != "") {
                            coroutineScope.launch {
                                status =
                                    userViewModel.register(username, password1, password2, email)
                            }
                        }
                        else {
                            status = "Please fill all fields"
                        }
                    }),
                    visualTransformation = if(passwordVisibility2) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisibility2 = !passwordVisibility2
                            }) {
                            Icon(
                                painter = icon2,
                                contentDescription = "Password visibility"
                            )
                        }
                    }
            )

        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                if(username != "" && email != "" && password1 != "" && password2 != "") {
                    coroutineScope.launch {
                        status = userViewModel.register(
                            username,
                            email,
                            password1,
                            password2
                        )
                    }
                }
                    else {
                    status = "Please fill all fields"
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
            Text(status, color= Color.Red)

        }

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Already have an account?")
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue, fontStyle = FontStyle.Italic)) {
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
    if(status == "Success") {
        mainViewModel.navigateTo(MyAppRoute.Login)
    }
}