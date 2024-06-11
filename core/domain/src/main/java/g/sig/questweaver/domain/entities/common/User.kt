package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.io.PayloadData

data class User(val name: String, val id: String = "") : PayloadData
