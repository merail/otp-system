package merail.otp.otpinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import merail.otp.core.extensions.toCountdownTime
import merail.otp.design.OtpTheme

@Composable
fun OtpInputContainer(
    navigateToBack: (String) -> Unit,
    navigateToPasswordCreation: (String) -> Unit,
) = OtpInputScreen(
    navigateToBack = navigateToBack,
    navigateToPasswordCreation = navigateToPasswordCreation,
)

@Composable
internal fun OtpInputScreen(
    navigateToBack: (String) -> Unit,
    navigateToPasswordCreation: (String) -> Unit,
    viewModel: OtpInputViewModel = hiltViewModel<OtpInputViewModel>(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 40.dp,
                        end = 40.dp,
                    ),
            ) {
                IconButton(
                    onClick = {
                        navigateToBack(viewModel.email)
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to EmailInput from OtpInput Icon",
                        tint = OtpTheme.colors.graphicPrimary,
                        modifier = Modifier
                            .size(36.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.otp_input_title),
                    style = OtpTheme.typography.displaySmall,
                    textAlign = TextAlign.Companion.Center,
                    modifier = Modifier
                        .weight(1f),
                )
            }

            Text(
                text = stringResource(R.string.otp_input_description),
                style = OtpTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 20.dp,
                    ),
            )

            OtpField(
                viewModel = viewModel,
                onOtpTextChange = {
                    viewModel.updateOtp(it)
                    if (it.length == 4) {
                        if (viewModel.verifyOtp()) {
                            navigateToPasswordCreation(viewModel.email)
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun OtpField(
    viewModel: OtpInputViewModel,
    onOtpTextChange: (String) -> Unit,
) {
    val otpValueState by viewModel.otpValueState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        BasicTextField(
            value = otpValueState.otp,
            onValueChange = {
                if (viewModel.isInputAvailable) {
                    if (it.length <= 4) {
                        onOtpTextChange(it)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Companion.NumberPassword,
            ),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(4) { index ->
                        OtpCell(
                            index = index,
                            text = otpValueState.otp,
                            isError = otpValueState.isOtpVerified.not(),
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        if (viewModel.isValid.not()) {
            Text(
                text = stringResource(
                    when {
                        otpValueState.hasAvailableAttempts.not() -> R.string.otp_input_block_error
                        otpValueState.isOtpNotExpired.not() -> R.string.otp_input_expired_error
                        else -> R.string.otp_input_validation_error
                    },
                ),
                style = OtpTheme.typography.bodyMedium,
                color = OtpTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
        }

        if (viewModel.isInputAvailable) {
            Text(
                text = stringResource(
                    R.string.otp_input_email_hint,
                    viewModel.email,
                ),
                style = OtpTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 4.dp,
                    end = 12.dp,
                ),
        ) {
            val isCountdownTextVisible by viewModel.isCountdownTextVisible.collectAsState()
            Text(
                text = stringResource(R.string.otp_input_resend_countdown_hint_text_part),
                style = OtpTheme.typography.bodyMedium,
                textDecoration = if (isCountdownTextVisible) {
                    TextDecoration.Companion.None
                } else {
                    TextDecoration.Companion.Underline
                },
                modifier = Modifier.clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = if (viewModel.isOtpResendingAvailable) {
                        ripple()
                    } else {
                        null
                    },
                    onClick = {
                        if (viewModel.isOtpResendingAvailable) {
                            viewModel.resendOtp()
                        }
                    },
                ),
            )

            if (isCountdownTextVisible) {
                val otpResendRemindTime by viewModel.otpResendRemindTime.collectAsState()
                Text(
                    text = stringResource(
                        R.string.otp_input_resend_countdown_hint_seconds_part,
                        otpResendRemindTime.toCountdownTime(),
                    ),
                    style = OtpTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(
                            start = 4.dp,
                        ),
                )
            }

            val otpResendState by viewModel.otpResendState.collectAsState()
            if (otpResendState.needToBlockUi) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .align(Alignment.Companion.CenterVertically)
                        .padding(
                            start = 4.dp,
                        )
                        .size(16.dp),
                )
            }
        }
    }

    LaunchedEffect(null) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun OtpCell(
    index: Int,
    text: String,
    isError: Boolean,
    modifier: Modifier,
) {
    val isCursorVisible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isCursorVisible.value) {
        delay(500)
        isCursorVisible.value = isCursorVisible.value.not()
    }
    val isFocused = text.length == index
    val char = if (index >= text.length) {
        ""
    } else {
        text[index].toString()
    }

    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = modifier
            .requiredSize(72.dp)
            .border(
                width = 1.dp,
                color = if (isError) {
                    OtpTheme.colors.graphicNegativePrimary
                } else {
                    OtpTheme.colors.graphicPrimary
                },
                shape = RoundedCornerShape(8.dp),
            )
            .padding(2.dp),
    ) {
        if (isFocused) {
            AnimatedVisibility(
                visible = isCursorVisible.value,
                enter = fadeIn(
                    animationSpec = TweenSpec(
                        durationMillis = 500,
                    ),
                ),
                exit = fadeOut(
                    animationSpec = TweenSpec(
                        durationMillis = 500,
                    ),
                ),
            ) {
                VerticalDivider(
                    thickness = 2.5.dp,
                    color = OtpTheme.colors.graphicPrimary,
                    modifier = Modifier
                        .padding(
                            vertical = 16.dp,
                        ),
                )
            }
        } else {
            Text(
                text = char,
                style = OtpTheme.typography.displayMedium,
                textAlign = TextAlign.Companion.Center,
                color = if (isError) {
                    OtpTheme.colors.textNegative
                } else {
                    OtpTheme.colors.textPrimary
                }
            )
        }
    }
}