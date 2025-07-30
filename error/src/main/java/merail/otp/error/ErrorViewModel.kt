package merail.otp.error

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.otp.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
internal class ErrorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val errorType = savedStateHandle.toRoute<NavigationRoute.Error>().errorType
}