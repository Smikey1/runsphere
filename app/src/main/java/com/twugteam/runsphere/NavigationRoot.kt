package com.twugteam.runsphere

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.twugteam.auth.presentation.intro.IntroScreenRoot
import com.twugteam.auth.presentation.register.RegisterScreenRoot


@Composable
fun NavigationRoot(navController: NavHostController): Unit {
    NavHost(
        navController=navController,
        startDestination = "auth"
    ) {
        authGraph(navController)
    }
}


private fun NavGraphBuilder.authGraph(navController: NavHostController){
    navigation(
        startDestination = "intro",
        route="auth"
    ) {
        composable(route = "intro"){
            IntroScreenRoot(
                onRegisterClick =  {
                    navController.navigate("register")
                },
                onLoginClick = {
                }
            )
        }
        composable(route="register"){
            RegisterScreenRoot(
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo(route = "register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true  // restoreState for login screen, if with mistakenly navigate
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate("intro")
                },
            )
        }

        composable(route="login"){
            Text("LOGIN DONE")
        }

    }
}