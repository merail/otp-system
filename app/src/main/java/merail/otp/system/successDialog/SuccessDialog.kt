package merail.otp.system.successDialog

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider

@Composable
internal fun SuccessDialog(
    onDismiss: () -> Unit,
) {
    (LocalView.current.parent as? DialogWindowProvider)?.run {
        window.setGravity(Gravity.CENTER)
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
        modifier = Modifier
            .wrapContentSize()
    ) {
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = Color.Green,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                ),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp),
            ) {
                Text(
                    text = "You are registered!",
                )

                Button(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = 12.dp,
                        )
                        .size(
                            width = 128.dp,
                            height = 36.dp,
                        ),
                ) {
                    Text(
                        text = "Ok",
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}