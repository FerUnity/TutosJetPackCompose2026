package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ContactViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: ContactViewModel = viewModel()

                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) {
                    AnimatedContent(
                        targetState = viewModel.currentScreen,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                        },
                        label = "screen_routing"
                    ) { screen ->
                        when (screen) {
                            Screen.Welcome -> {
                                WelcomeScreen(
                                    onVerContactosClicked = {
                                        viewModel.currentScreen = Screen.ContactsList
                                    }
                                )
                            }
                            Screen.ContactsList -> {
                                ContactsListScreen(viewModel = viewModel)
                            }
                            Screen.ActiveCall -> {
                                val activeContact = viewModel.activeCallContact
                                CallScreen(
                                    contactName = activeContact?.name ?: "Desconocido",
                                    photoUri = activeContact?.photoUri ?: "",
                                    callDurationSeconds = viewModel.activeCallDuration,
                                    callState = viewModel.activeCallState,
                                    onEndCallClicked = {
                                        viewModel.endCall()
                                    }
                                )
                            }
                            Screen.CallEnded -> {
                                val activeContact = viewModel.activeCallContact
                                CallEndedScreen(
                                    contactName = activeContact?.name ?: "Desconocido",
                                    photoUri = activeContact?.photoUri ?: "",
                                    callDurationSeconds = viewModel.activeCallDuration,
                                    isFavorite = activeContact?.isFavorite ?: false,
                                    onBackToContactsClicked = {
                                        viewModel.currentScreen = Screen.ContactsList
                                    },
                                    onToggleFavorite = {
                                        activeContact?.let { viewModel.toggleFavorite(it) }
                                    }
                                )
                            }
                            Screen.CallFailed -> {
                                val activeContact = viewModel.activeCallContact
                                CallFailedScreen(
                                    contactName = activeContact?.name ?: "Desconocido",
                                    photoUri = activeContact?.photoUri ?: "",
                                    onRetryClicked = {
                                        viewModel.retryCall()
                                    },
                                    onCloseClicked = {
                                        viewModel.currentScreen = Screen.ContactsList
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
