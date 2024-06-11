package g.sig.questweaver.domain.entities.blocks

@JvmInline
value class Uri(val value: String) {
    init {
        require(value.matches("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?".toRegex())) { "Invalid Uri" }
    }
}
