package merail.otp.navigation.domain

import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import merail.otp.navigation.domain.error.ErrorType
import merail.otp.navigation.domain.error.toType
import kotlin.jvm.java

fun NavController.navigateToError(error: Throwable?) = navigate(
    route = NavigationRoute.Error(error.toType()),
) {
    launchSingleTop = true
}

val NavBackStackEntry.errorType: ErrorType
    get() = if (arguments?.containsKey(NavigationRoute.Error.ERROR_TYPE_KEY) == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(
                NavigationRoute.Error.ERROR_TYPE_KEY,
                ErrorType::class.java,
            ) as ErrorType
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(NavigationRoute.Error.ERROR_TYPE_KEY) as ErrorType
        }
    } else {
        throw IllegalStateException("ErrorType available only in ErrorDestination!")
    }