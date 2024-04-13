package g.sig.common.utils

fun <T : Any> MutableList<T>.addOrReplace(predicate: (T) -> Boolean, element: T) {
    synchronized(this) {
        indexOfFirst(predicate).let { existingIndex ->
            if (existingIndex != -1) {
                set(existingIndex, element)
            } else {
                add(element)
            }
        }
    }
}

fun <T> MutableList<T>.addOrIgnore(predicate: (T) -> Boolean, element: T) {
    synchronized(this) {
        if (indexOfFirst(predicate) == -1) {
            add(element)
        }
    }
}

fun <T> MutableList<T>.update(predicate: (T) -> Boolean, transform: (T) -> T) {
    synchronized(this) {
        indexOfFirst(predicate).let { existingIndex ->
            if (existingIndex != -1) {
                set(existingIndex, transform(get(existingIndex)))
            }
        }
    }
}

