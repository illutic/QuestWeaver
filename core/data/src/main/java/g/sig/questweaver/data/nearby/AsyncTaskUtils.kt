package g.sig.questweaver.data.nearby

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
