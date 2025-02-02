package com.twugteam.runsphere

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.twugteam.auth.presentation.intro.IntroScreenRoot
import com.twugteam.auth.presentation.login.LoginScreenRoot
import com.twugteam.auth.presentation.register.RegisterScreenRoot
import com.twugteam.run.presentation.active_run.ActiveRunScreenRoot
import com.twugteam.run.presentation.run_overview.RunOverviewScreenRoot


@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggingPreviously: Boolean
): Unit {
    NavHost(
        navController = navController,
        startDestination = if (isLoggingPreviously) "auth" else "run"
    ) {
        authGraph(navController)
        runGraph(navController)
    }
}


private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = "intro",
        route = "auth"
    ) {
        composable(route = "intro") {
            IntroScreenRoot(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }
        composable(route = "register") {
            RegisterScreenRoot(
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo(route = "register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState =
                            true  // restoreState for login screen, if with mistakenly navigate
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate("intro")
                },
            )
        }

        composable(route = "login") {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulLogin = {
                    navController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(
        startDestination = "run_overview",
        route = "run"
    ) {
        composable(route = "run_overview") {
            RunOverviewScreenRoot(
                onStartRunClick = {
                    navController.navigate("active_run")
                }
            )
        }
        composable(route = "active_run") {
            ActiveRunScreenRoot()
        }
    }
}