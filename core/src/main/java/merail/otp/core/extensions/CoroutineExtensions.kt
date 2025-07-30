package merail.otp.core.extensions

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> suspendableRunCatching(
    block: () -> R,
): Result<R> = try {
    Result.Companion.success(block())
} catch (c: CancellationException) {
    throw c
} catch (e: Throwable) {
    Result.Companion.failure(e)
}