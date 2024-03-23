package g.sig.data.nearby.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

inline fun <T> Task<T>.doOnSuccess(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    crossinline onSuccess: suspend (T) -> Unit
) = addOnSuccessListener { coroutineScope.launch { onSuccess(it) } }

inline fun <T> Task<T>.doOnFailure(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    crossinline onFailure: suspend (Exception) -> Unit
) = addOnFailureListener { coroutineScope.launch { onFailure(it) } }

inline fun <T> Task<T>.doOnComplete(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    crossinline onComplete: suspend (Task<T>) -> Unit
) = addOnCompleteListener { coroutineScope.launch { onComplete(it) } }

inline fun <T> Task<T>.doOnCanceled(
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    crossinline onCanceled: suspend () -> Unit
) = addOnCanceledListener { coroutineScope.launch { onCanceled() } }