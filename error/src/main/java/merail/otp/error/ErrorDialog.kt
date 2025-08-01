package merail.otp.error

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import merail.otp.core.exceptions.ErrorType
import merail.otp.core.exceptions.toType
import merail.otp.design.OtpSystemTheme
import merail.otp.design.cardColors
import merail.otp.navigation.domain.NavigationRoute
import merail.otp.navigation.domain.R

fun NavController.navigateToError(error: Throwable?) = navigate(
    route = NavigationRoute.Error(error.toType()),
) {
    launchSingleTop = true
}

@Composable
fun ErrorContainer(
    onDismiss: () -> Unit,
) {
    ErrorDialog(
        onDismiss = onDismiss,
    )
}

@Composable
internal fun ErrorDialog(
    onDismiss: () -> Unit,
    viewModel: ErrorViewModel = hiltViewModel<ErrorViewModel>(),
) {
    (LocalView.current.parent as? DialogWindowProvider)?.run {
        window.setGravity(Gravity.TOP)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
    }

    val state = rememberSwipeToDismissBoxState()

    when (state.targetValue) {
        SwipeToDismissBoxValue.StartToEnd,
        SwipeToDismissBoxValue.EndToStart,
        -> onDismiss()
        SwipeToDismissBoxValue.Settled,
        -> Unit
    }

    SwipeToDismissBox(
        state = state,
        backgroundContent = {},
    ) {
        Card(
            colors = OtpSystemTheme.colors.cardColors.copy(
                containerColor = OtpSystemTheme.colors.elementNegative,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.error_title),
                        style = OtpSystemTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(viewModel.errorType.message),
                        style = OtpSystemTheme.typography.bodyMedium,
                    )
                }

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cross),
                    contentDescription = "Close Error Dialog Button",
                    modifier = Modifier
                        .align(Alignment.Top)
                        .clickable {
                            onDismiss()
                        },
                )
            }
        }
    }
}

private val ErrorType.message: Int
    get() = when (this) {
        ErrorType.INTERNET_CONNECTION -> R.string.error_internet_connection_subtitle
        ErrorType.OTHER -> R.string.error_common_subtitle
    }