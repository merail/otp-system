package merail.otp.system.emailInput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.otp.system.AppViewModelProvider

@Composable
internal fun EmailInputScreen(
    onError: (String) -> Unit,
    onOtpSend: () -> Unit,
    viewModel: EmailInputViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
    ),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 12.dp,
            ),
    ) {
        val emailValueState by viewModel.emailState.collectAsState()
        EmailField(
            emailState = emailValueState,
            onChange = viewModel::updateEmail,
        )

        Button(
            onClick = viewModel::validateEmail,
            modifier = Modifier
                .padding(
                    top = 24.dp,
                )
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text(
                text = "Send OTP",
                textAlign = TextAlign.Center,
            )
        }
    }

    val sendOtpState by viewModel.sendOtpState.collectAsState()
    when (sendOtpState) {
        is SendOtpState.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                ),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
        is SendOtpState.Error -> {
            onError("OTP sending error!")
            viewModel.updateSendOtpState(SendOtpState.None)
        }
        is SendOtpState.Success -> {
            onOtpSend()
            viewModel.updateSendOtpState(SendOtpState.None)
        }
        is SendOtpState.None -> Unit
    }
}

@Composable
private fun EmailField(
    emailState: EmailState,
    onChange: (String) -> Unit,
) {
    Column {
        TextField(
            value = emailState.email,
            onValueChange = onChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            label = {
                Text(
                    text = "Enter email",
                )
            },
            singleLine = true,
            isError = emailState.isValid.not(),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )

        if (emailState.isValid.not()) {
            Text(
                text = "Invalid email",
                color = Color.Red,
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    )
            )
        }
    }
}