package merail.otp.system.errorDialog

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun ErrorDialog(
    errorText: String,
    onDismiss: () -> Unit,
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
        modifier = Modifier
            .wrapContentSize(),
    ) {
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = Color.Red,
            ),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier
                    .padding(20.dp),
            )
        }
    }
}