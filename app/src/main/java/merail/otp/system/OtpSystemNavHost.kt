package merail.otp.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import merail.otp.system.emailInput.EmailInputScreen
import merail.otp.system.errorDialog.ErrorDialog
import merail.otp.system.otpInput.OtpInputScreen
import merail.otp.system.successDialog.SuccessDialog

@Composable
fun OtpSystemNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Email,
    ) {
        composable<NavigationRoute.Email> {
            EmailInputScreen(
                onError = {
                    navController.navigate(
                        route = NavigationRoute.Error(
                            errorText = it,
                        ),
                    )
                },
                onOtpSend = {
                    navController.navigate(NavigationRoute.Otp)
                },
            )
        }

        composable<NavigationRoute.Otp> {
            OtpInputScreen(
                navigateToPassword = {
                    navController.navigate(NavigationRoute.Success)
                },
            )
        }

        dialog<NavigationRoute.Error>(
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) { backStackEntry ->
            val errorRoute: NavigationRoute.Error = backStackEntry.toRoute()

            ErrorDialog(
                errorText = errorRoute.errorText,
                onDismiss = {
                    navController.popBackStack()
                },
            )
        }

        dialog<NavigationRoute.Success>(
            dialogProperties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            SuccessDialog(
                onDismiss = {
                    navController.popBackStack()
                },
            )
        }
    }
}

sealed class NavigationRoute {
    @Serializable
    data object Email : NavigationRoute()

    @Serializable
    data object Otp : NavigationRoute()

    @Serializable
    data class Error(
        val errorText: String,
    ) : NavigationRoute()

    @Serializable
    data object Success : NavigationRoute()
}