package merail.otp.email

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.otp.design.OtpSystemTheme
import merail.otp.design.components.BlockingSurface
import merail.otp.design.components.ContinueButton
import merail.otp.design.styles.TextFieldStyle

@Composable
fun EmailInputContainer(
    onError: (Throwable?) -> Unit,
    navigateToOtp: (String) -> Unit,
    navigateToPasswordEnter: (String) -> Unit,
) = EmailInputScreen(
    onError = onError,
    navigateToOtp = navigateToOtp,
    navigateToPasswordEnter = navigateToPasswordEnter,
)

@Composable
internal fun EmailInputScreen(
    onError: (Throwable?) -> Unit,
    navigateToOtp: (String) -> Unit,
    navigateToPasswordEnter: (String) -> Unit,
    viewModel: EmailInputViewModel = hiltViewModel<EmailInputViewModel>(),
) {
    val state by viewModel.emailAuthState.collectAsState()
    val emailState by viewModel.emailValueState.collectAsState()

    when (state) {
        is EmailAuthState.Error -> LaunchedEffect(null) {
            onError((state as EmailAuthState.Error).exception)
            viewModel.resetState()
        }
        is EmailAuthState.OtpWasSent -> LaunchedEffect(null) {
            navigateToOtp(emailState.email)
            viewModel.resetState()
        }
        is EmailAuthState.UserExists -> LaunchedEffect(null) {
            navigateToPasswordEnter(emailState.email)
            viewModel.resetState()
        }
        is EmailAuthState.None,
        is EmailAuthState.Loading,
        -> Unit
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.email_input_title),
                    style = OtpSystemTheme.typography.displaySmall,
                    textAlign = TextAlign.Companion.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 40.dp,
                            top = 40.dp,
                            end = 40.dp,
                        ),
                )

                Text(
                    text = stringResource(R.string.email_input_description),
                    style = OtpSystemTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp,
                        ),
                )

                EmailField(
                    emailValueState = emailState,
                    onChange = viewModel::updateEmail,
                )
            }

            ContinueButton(
                onClick = viewModel::validateEmail,
                text = stringResource(R.string.email_input_continue_button),
                needToBlockUi = state.needToBlockUi,
            )
        }

        if (state.needToBlockUi) {
            BlockingSurface()
        }
    }
}

@Composable
private fun EmailField(
    emailValueState: EmailValueState,
    onChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        TextField(
            value = emailValueState.email,
            onValueChange = onChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Email User Icon",
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Companion.Done,
                keyboardType = KeyboardType.Companion.Email,
            ),
            label = {
                Text(
                    text = stringResource(R.string.email_input_label),
                )
            },
            colors = TextFieldStyle.Primary.colors(),
            singleLine = true,
            isError = emailValueState.isValid.not(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )
        if (emailValueState.isValid.not()) {
            Text(
                text = stringResource(R.string.email_input_validation_error),
                style = OtpSystemTheme.typography.bodyMedium,
                color = OtpSystemTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                    ),
            )
        }
    }
}