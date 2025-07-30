package merail.otp.password.passwordCreation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.otp.design.OtpSystemTheme
import merail.otp.design.components.BlockingSurface
import merail.otp.design.components.ContinueButton
import merail.otp.design.styles.TextFieldStyle
import merail.otp.password.R

@Composable
fun PasswordCreationContainer(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
) = PasswordCreationScreen(
    onError = onError,
    navigateToHome = navigateToHome,
)

@Composable
internal fun PasswordCreationScreen(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: PasswordCreationViewModel = hiltViewModel<PasswordCreationViewModel>(),
) {
    val state by viewModel.userCreatingState.collectAsState()

    when (state) {
        is UserCreatingState.Error -> onError((state as UserCreatingState.Error).exception)
        is UserCreatingState.Success -> LaunchedEffect(null) {
            navigateToHome()
        }
        is UserCreatingState.None,
        is UserCreatingState.Loading,
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
                    text = stringResource(R.string.password_creation_title),
                    style = OtpSystemTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 40.dp,
                            top = 40.dp,
                            end = 40.dp,
                        ),
                )

                Text(
                    text = stringResource(R.string.password_creation_description),
                    style = OtpSystemTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp,
                        ),
                )

                val passwordValueState by viewModel.passwordValueState.collectAsState()
                PasswordField(
                    passwordValueState = passwordValueState,
                    onChange = viewModel::updatePassword,
                    imeAction = ImeAction.Next,
                    label = stringResource(R.string.password_creation_label),
                    errorText = stringResource(R.string.password_creation_validation_error),
                )

                val repeatedPasswordValueState by viewModel.repeatedPasswordValueState.collectAsState()
                if (passwordValueState.password.isNotEmpty()) {
                    PasswordField(
                        passwordValueState = repeatedPasswordValueState,
                        onChange = viewModel::updateRepeatedPassword,
                        imeAction = ImeAction.Done,
                        label = stringResource(R.string.password_creation_repeated_label),
                        errorText = stringResource(R.string.password_creation_repeated_validation_error),
                    )
                }
            }

            ContinueButton(
                onClick = viewModel::validate,
                needToBlockUi = state.needToBlockUi,
                text = stringResource(R.string.password_creation_continue_button),
            )
        }

        if (state.needToBlockUi) {
            BlockingSurface()
        }
    }
}

@Composable
internal fun PasswordField(
    passwordValueState: PasswordValueState,
    onChange: (String) -> Unit,
    imeAction: ImeAction,
    label: String,
    errorText: String,
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        TextField(
            value = passwordValueState.password,
            onValueChange = onChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Password TextField Icon",
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Password Visibility Icon",
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = KeyboardType.Password,
            ),
            label = {
                Text(
                    text = label,
                )
            },
            colors = TextFieldStyle.Primary.colors(),
            singleLine = true,
            isError = passwordValueState.isValid.not(),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )
        if (passwordValueState.isValid.not()) {
            Text(
                text = errorText,
                style = OtpSystemTheme.typography.bodyMedium,
                color = OtpSystemTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
        }
    }
}