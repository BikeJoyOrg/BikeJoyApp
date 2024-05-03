package com.example.bikejoyapp.routes.ui

import com.example.bikejoyapp.theme.BikeJoyAppTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme


@Composable
fun RoutePreviewWidget(name: String?, description: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image (
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Column (Modifier.padding(8.dp)) {
                if (name != null) {
                    Text(text = name)
                }
                if (description != null) {
                    Text(text = description)
                }
            }
        }
    }
}