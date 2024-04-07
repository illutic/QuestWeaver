package g.sig.data.utils

import g.sig.domain.entities.Uri

fun Uri.toUri(): android.net.Uri = android.net.Uri.parse(value)

fun android.net.Uri.toDomain(): Uri = Uri(toString())