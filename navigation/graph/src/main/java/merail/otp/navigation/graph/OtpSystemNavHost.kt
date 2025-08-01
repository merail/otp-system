package merail.otp.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import merail.otp.email.EmailInputContainer
import merail.otp.error.ErrorContainer
import merail.otp.error.navigateToError
import merail.otp.home.HomeContainer
import merail.otp.navigation.domain.NavigationRoute
import merail.otp.otp.OtpInputContainer
import merail.otp.password.passwordCreation.PasswordCreationContainer
import merail.otp.password.passwordEnter.PasswordEnterContainer

@Composable
fun OtpSystemNavHost(
    isUserAuthorized: Boolean,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserAuthorized) {
            NavigationRoute.Home
        } else {
            NavigationRoute.EmailInput
        },
    ) {
        composable<NavigationRoute.EmailInput> {
            EmailInputContainer(
                onError = navController::navigateToError,
                navigateToOtp = {
                    navController.navigate(NavigationRoute.OtpInput(it))
                },
                navigateToPasswordEnter = {
                    navController.navigate(NavigationRoute.PasswordEnter(it))
                },
            )
        }

        composable<NavigationRoute.OtpInput> {
            OtpInputContainer(
                navigateToBack = {
                    navController.popBackStack()
                },
                navigateToPasswordCreation = {
                    navController.navigate(NavigationRoute.PasswordCreation(it))
                },
            )
        }

        composable<NavigationRoute.PasswordCreation> {
            PasswordCreationContainer(
                onError = navController::navigateToError,
                navigateToHome = {
                    navController.navigate(NavigationRoute.Home)
                },
            )
        }

        composable<NavigationRoute.PasswordEnter> {
            PasswordEnterContainer(
                navigateToBack = {
                    navController.popBackStack()
                },
                onError = navController::navigateToError,
                navigateToHome = {
                    navController.navigate(NavigationRoute.Home)
                },
            )
        }

        composable<NavigationRoute.Home> {
            HomeContainer(
                onExit = {
                    navController.navigate(NavigationRoute.EmailInput)
                }
            )
        }

        dialog<NavigationRoute.Error>(
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            ErrorContainer(
                onDismiss = navController::popBackStack,
            )
        }
    }
}