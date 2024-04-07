package g.sig.domain.entities

@JvmInline
value class Uri(val value: String) {
    init {
        require(value.matches("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?".toRegex())) { "Invalid Uri" }
    }
}