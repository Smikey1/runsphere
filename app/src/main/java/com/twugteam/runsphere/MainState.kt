package com.twugteam.runsphere

data class MainState(
    val isCheckingAuth: Boolean = false,
    val isLoggedInPreviously: Boolean = false
)
