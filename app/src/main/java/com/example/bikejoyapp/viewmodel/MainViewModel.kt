package com.example.bikejoyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bikejoyapp.data.MyAppRoute
import com.example.bikejoyapp.data.MyAppTopLevelDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel : ViewModel() {
    private val _isBottomBarVisible = MutableStateFlow(true) // Inicialmente visible
    val isBottomBarVisible: StateFlow<Boolean> = _isBottomBarVisible
    fun hideBottomBar() { _isBottomBarVisible.value = false }
    fun showBottomBar() { _isBottomBarVisible.value = true }


    private val _isTopBarVisible = MutableStateFlow(true) // Inicialmente visible
    val isTopBarVisible: StateFlow<Boolean> = _isTopBarVisible
    fun hideTopBar() { _isTopBarVisible.value = false }
    fun showTopBar() { _isTopBarVisible.value = true }

    private val _navigationCommands = Channel<NavigationCommand>(Channel.BUFFERED)
    val navigationCommands = _navigationCommands.receiveAsFlow()

    fun navigateTo(destination: MyAppRoute) {
        val result = _navigationCommands.trySend(NavigationCommand.ToDestination(destination))

        // Traça opcional d'errors
        result.onFailure { throwable ->
            throwable?.printStackTrace()
        }
    }
}

sealed class NavigationCommand {
    data class ToDestination(val destination: MyAppRoute) : NavigationCommand()
}