package tech.espero.vendingmachine.exception

class ProductNotAvailableException(override val message: String?) : RuntimeException(message) {
}