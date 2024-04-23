package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.RutaUsuari
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel : ViewModel() {
    var selectedRoute: RutaUsuari? = null
    private val _isBottomBarVisible = MutableStateFlow(true) // Inicialmente visible
    val isBottomBarVisible: StateFlow<Boolean> = _isBottomBarVisible
    fun hideBottomBar() {
        _isBottomBarVisible.value = false
    }

    fun showBottomBar() {
        _isBottomBarVisible.value = true
    }


    private val _isTopBarVisible = MutableStateFlow(true) // Inicialmente visible
    val isTopBarVisible: StateFlow<Boolean> = _isTopBarVisible
    fun hideTopBar() {
        _isTopBarVisible.value = false
    }

    fun showTopBar() {
        _isTopBarVisible.value = true
    }

    private val _navigationCommands = Channel<NavigationCommand>(Channel.BUFFERED)
    val navigationCommands = _navigationCommands.receiveAsFlow()

    fun navigateTo(destination: MyAppRoute) {
        val result = _navigationCommands.trySend(NavigationCommand.ToDestination(destination))

        result.onFailure { throwable ->
            throwable?.printStackTrace()
        }
    }

    fun navigateToDynamic(destination: String) {
        _navigationCommands.trySend(NavigationCommand.ToDynamicDestination(destination))
            .onFailure { throwable ->
                throwable?.printStackTrace()
            }
    }

    fun navigateBack() {
        val result = _navigationCommands.trySend(NavigationCommand.Back)
        result.onFailure { throwable ->
            println("error go back")
            throwable?.printStackTrace()
        }
        showBottomBar()
    }
}

sealed class NavigationCommand {
    data class ToDestination(val destination: MyAppRoute) : NavigationCommand()

    data class ToDynamicDestination(val destination: String) : NavigationCommand()

    data object Back : NavigationCommand()
}