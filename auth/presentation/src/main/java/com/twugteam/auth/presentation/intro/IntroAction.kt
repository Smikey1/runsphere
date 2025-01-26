package com.twugteam.auth.presentation.intro

sealed interface IntroAction {
    data object OnRegisterClick: IntroAction
    data object OnLoginClick: IntroAction
//    data object OnBackClick: IntroAction
}