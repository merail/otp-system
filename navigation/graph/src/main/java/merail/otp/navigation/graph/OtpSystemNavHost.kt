package merail.otp.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import merail.otp.emailinput.EmailInputContainer
import merail.otp.navigation.domain.NavigationRoute
import merail.otp.navigation.domain.error.ErrorDialog
import merail.otp.navigation.domain.errorType
import merail.otp.navigation.domain.navigateToError
import merail.otp.otpinput.OtpInputContainer
import merail.otp.password.passwordCreation.PasswordCreationContainer
import merail.otp.password.passwordEnter.PasswordEnterContainer

@Composable
fun OtpSystemNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.EmailInput,
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
                navigateToHome = {},
            )
        }

        composable<NavigationRoute.PasswordEnter> {
            PasswordEnterContainer(
                navigateToBack = {
                    navController.popBackStack()
                },
                onError = navController::navigateToError,
                navigateToHome = {},
            )
        }

        dialog<NavigationRoute.Error>(
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            ErrorDialog(
                errorType = it.errorType,
                onDismiss = navController::popBackStack,
            )
        }
    }
}