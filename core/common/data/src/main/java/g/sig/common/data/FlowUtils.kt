package g.sig.common.data

import kotlinx.coroutines.channels.SendChannel

fun <E> SendChannel<E>.trySendAndClose(value: E) {
    trySend(value)
    close()
}