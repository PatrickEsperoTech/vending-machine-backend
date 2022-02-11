package tech.espero.vendingmachine.exception

class ProductCouldNotBeDeletedException(override val message: String?) : RuntimeException(message) {
}