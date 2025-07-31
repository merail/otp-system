package merail.otp.system.otpInput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import merail.otp.system.AppViewModelProvider

@Composable
internal fun OtpInputScreen(
    navigateToSuccess: () -> Unit,
    viewModel: OtpInputViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
    ),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 128.dp,
            ),
    ) {
        OtpField(
            viewModel = viewModel,
            onOtpTextChange = {
                viewModel.updateOtp(it)
                if (it.length == 4) {
                    if (viewModel.verifyOtp()) {
                        navigateToSuccess()
                    }
                }
            },
        )
    }
}

@Composable
private fun OtpField(
    viewModel: OtpInputViewModel,
    onOtpTextChange: (String) -> Unit,
) {
    val otpValueState by viewModel.otpValueState.collectAsState()
    val focusRequester = remember {
        FocusRequester()
    }
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        BasicTextField(
            value = otpValueState.otp,
            onValueChange = {
                if (it.length <= 4) {
                    onOtpTextChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(4) { index ->
                        OtpCell(
                            index = index,
                            text = otpValueState.otp,
                            isError = otpValueState.isVerified.not(),
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

        if (otpValueState.isVerified.not()) {
            Text(
                text = "Invalid OTP",
                color = Color.Red,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
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
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredSize(72.dp)
            .border(
                width = 1.dp,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(
                            vertical = 16.dp,
                        ),
                )
            }
        } else {
            Text(
                text = char,
                textAlign = TextAlign.Center,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}