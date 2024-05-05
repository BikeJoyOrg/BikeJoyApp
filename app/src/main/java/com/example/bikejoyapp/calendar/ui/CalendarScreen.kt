package com.example.bikejoyapp.calendar.ui

import android.os.Build
import android.provider.Settings.Global.getString
import android.text.Layout
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.bikejoyapp.calendar.data.ForecastItem
import com.example.bikejoyapp.calendar.viewmodel.CalendarViewModel
import com.example.bikejoyapp.utils.MainViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import androidx.compose.foundation.lazy.items



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel, mainViewModel: MainViewModel) {
    val forecastData by calendarViewModel.forecastData.observeAsState(initial = emptyList())
    val groupedByDay = forecastData.groupBy {
        Instant.ofEpochSecond(it.dt).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    var selectedDate by remember { mutableStateOf(groupedByDay.keys.firstOrNull()) }
    var hourIndex by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        // Selector de días
        DaySelector(groupedByDay.keys.toList(), selectedDate) { day ->
            selectedDate = day
            hourIndex = 0  // Reset hour index when day changes
        }

        // Detalles del día seleccionado
        selectedDate?.let { day ->
            val dayForecasts = groupedByDay[day]
            dayForecasts?.let {
                DayDetailHeader(it[hourIndex])
                DayDetailBody(it[hourIndex])

                // Navegación entre horas
                HourNavigation(hourIndex, it.size) { newIndex ->
                    hourIndex = newIndex
                }
            }
        }
    }
}

@Composable
fun DaySelector(days: List<LocalDate>, selectedDay: LocalDate?, onDaySelected: (LocalDate) -> Unit) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(days) { day ->
            Button(onClick = { onDaySelected(day) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (day == selectedDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )) {
                Text(day.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun HourNavigation(currentIndex: Int, size: Int, onIndexChange: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        IconButton(onClick = { if (currentIndex > 0) onIndexChange(currentIndex - 1) }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Anterior")
        }
        IconButton(onClick = { if (currentIndex < size - 1) onIndexChange(currentIndex + 1) }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Siguiente")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayDetailHeader(forecast: ForecastItem) {
    val date = Instant.ofEpochSecond(forecast.dt).atZone(ZoneId.systemDefault()).toLocalDate()
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = date.toString(), style = MaterialTheme.typography.titleMedium)
        Text(text = forecast.weather[0].main, style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
fun DayDetailBody(forecast: ForecastItem) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Temperatura: ${forecast.main.temp} °C", style = MaterialTheme.typography.bodyLarge)
        Text("Sensación térmica: ${forecast.main.feels_like} °C", style = MaterialTheme.typography.bodyLarge)
        Text("Descripción: ${forecast.weather[0].description}", style = MaterialTheme.typography.bodyLarge)
        Text("Viento: ${forecast.wind.speed} m/s, dirección ${forecast.wind.deg}°", style = MaterialTheme.typography.bodyLarge)
        Text("Humedad: ${forecast.main.humidity}%", style = MaterialTheme.typography.bodyLarge)
        Text("Visibilidad: ${forecast.visibility} metros", style = MaterialTheme.typography.bodyLarge)
    }
}

