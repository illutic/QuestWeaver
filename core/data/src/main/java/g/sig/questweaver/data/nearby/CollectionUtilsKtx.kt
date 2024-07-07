package g.sig.questweaver.data.nearby

import androidx.collection.SimpleArrayMap

operator fun <K, V> SimpleArrayMap<K, V>.set(
    id: K,
    value: V,
) {
    put(id, value)
}
