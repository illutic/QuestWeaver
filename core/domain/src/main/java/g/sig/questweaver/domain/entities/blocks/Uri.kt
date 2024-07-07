package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.DomainEntity

@JvmInline
value class Uri(
    val value: String,
) : DomainEntity {
    init {
        require(value.matches("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?".toRegex())) { "Invalid Uri" }
    }
}
