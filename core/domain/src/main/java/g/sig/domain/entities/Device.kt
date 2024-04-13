package g.sig.domain.entities

data class Device(val id: String, val name: String, val connectionState: ConnectionState) {
    companion object {
        infix fun Device.hasTheSameIdAs(id: String?): Boolean {
            return this.id == id
        }

        infix fun Device.hasTheSameNameAs(name: String?): Boolean {
            return this.name == name
        }

        infix fun Device.isTheSameAs(device: Device): Boolean {
            return this.id == device.id || this.name == device.name
        }

        infix fun Device.isSimilarTo(device: Device): Boolean {
            return this.id == device.id || this.name == device.name
        }
    }
}
